package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.dto.lending.profile.CreateProfileResponse;
import com.kucingoyen.microlend.dto.lending.profile.UserProfileResponse;
import com.kucingoyen.microlend.exception.NotFoundException;
import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import com.kucingoyen.microlend.service.DamlLedgerService.ExerciseResponse;
import com.kucingoyen.microlend.util.DamlPayloadParser;
import com.kucingoyen.microlend.util.LevelCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service for user lending profile management.
 * Handles creating profiles, querying profiles, and calculating collateral
 * rates.
 */
@Service
@RequiredArgsConstructor
public class LendingProfileService {

    private static final Logger log = LoggerFactory.getLogger(LendingProfileService.class);

    private final DamlLedgerService damlService;
    private final UserRepository userRepository;
    private final LendingAdminService lendingAdminService;
    private final AdminSetupService adminSetupService;

    /**
     * Create a UserProfile for a new user.
     *
     * @param email User's email
     * @return Response with created profile
     */
    public CreateProfileResponse createUserProfile(String email) {
        log.info("Creating user profile for: {}", email);

        // Get user's party ID
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        String userPartyId = user.getDamlPartyId();
        if (userPartyId == null || userPartyId.isBlank()) {
            throw new IllegalStateException("User does not have a DAML party ID");
        }

        // Get UserProfileFactory contract ID
        String factoryContractId = lendingAdminService.getUserProfileFactoryContractId();
        if (factoryContractId == null) {
            throw new IllegalStateException("Lending system not initialized. Please run admin setup first.");
        }

        String operatorPartyId = adminSetupService.getAdminPartyId();

        try {
            // Exercise CreateUserProfile choice
            ExerciseResponse response = damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_USER_PROFILE_FACTORY,
                    factoryContractId,
                    "CreateUserProfile",
                    Map.of("user", userPartyId),
                    operatorPartyId);

            // Extract created profile contract ID
            String profileContractId = extractContractId(response);

            // Query the created profile to get details
            UserProfileResponse profile = getUserProfile(email);

            log.info("User profile created successfully: {}", profileContractId);

            return new CreateProfileResponse(true, profileContractId, profile);

        } catch (Exception e) {
            log.error("Failed to create user profile: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user profile: " + e.getMessage(), e);
        }
    }

    /**
     * Get user's lending profile.
     *
     * @param email User's email
     * @return User profile response
     */
    public UserProfileResponse getUserProfile(String email) {
        log.info("Querying user profile for: {}", email);

        // Get user's party ID
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        String userPartyId = user.getDamlPartyId();
        if (userPartyId == null) {
            throw new IllegalStateException("User does not have a DAML party ID");
        }

        // Query UserProfile contracts
        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_USER_PROFILE,
                Map.of("user", userPartyId),
                userPartyId);

        if (results.isEmpty()) {
            throw new NotFoundException("User profile not found. Please create a profile first.");
        }

        // Take the first result (should only be one profile per user)
        ContractResult profileContract = results.get(0);
        Map<String, Object> payload = profileContract.payload();

        // Extract profile data
        String level = (String) payload.get("level");
        Integer loansCompleted = DamlPayloadParser.parseInteger(payload.get("loansCompleted"));
        Integer loansDefaulted = DamlPayloadParser.parseInteger(payload.get("loansDefaulted"));
        BigDecimal totalBorrowed = DamlPayloadParser.parseBigDecimal(payload.get("totalBorrowed"));
        BigDecimal totalLent = DamlPayloadParser.parseBigDecimal(payload.get("totalLent"));

        // Get base collateral rate from LendingService (or use default)
        BigDecimal baseCollateralRate = getBaseCollateralRate();

        // Calculate collateral rate
        BigDecimal collateralRate = LevelCalculator.calculateCollateralRate(baseCollateralRate, level);
        String description = LevelCalculator.getCollateralRateDescription(baseCollateralRate, level, collateralRate);

        return new UserProfileResponse(
                level,
                loansCompleted,
                loansDefaulted,
                totalBorrowed,
                totalLent,
                collateralRate,
                description);
    }

    /**
     * Get the base collateral rate from LendingService contract.
     * Falls back to default if not available.
     */
    private BigDecimal getBaseCollateralRate() {
        try {
            String lendingServiceCid = lendingAdminService.getLendingServiceContractId();
            if (lendingServiceCid == null) {
                log.warn("LendingService contract not found, using default base rate");
                return new BigDecimal(LendingConstants.DEFAULT_BASE_COLLATERAL_RATE);
            }

            // Query LendingService contract
            String operatorPartyId = adminSetupService.getAdminPartyId();
            List<ContractResult> results = damlService.queryContracts(
                    LendingConstants.TEMPLATE_LENDING_SERVICE,
                    Map.of("operator", operatorPartyId),
                    operatorPartyId);

            if (!results.isEmpty()) {
                Map<String, Object> payload = results.get(0).payload();
                return new BigDecimal(payload.get("baseCollateralRate").toString());
            }

        } catch (Exception e) {
            log.warn("Failed to get base collateral rate from LendingService: {}", e.getMessage());
        }

        // Fallback to default
        return new BigDecimal(LendingConstants.DEFAULT_BASE_COLLATERAL_RATE);
    }

    private String extractContractId(ExerciseResponse response) {
        if (response != null && response.result() != null) {
            Object result = response.result().exerciseResult();
            if (result instanceof String) {
                return (String) result;
            }
            // Handle tuple response - CreateUserProfile might return (newFactoryId,
            // profileId)
            if (result instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultMap = (Map<String, Object>) result;
                if (resultMap.containsKey("_2")) {
                    return (String) resultMap.get("_2");
                }
            }
        }
        return null;
    }
}
