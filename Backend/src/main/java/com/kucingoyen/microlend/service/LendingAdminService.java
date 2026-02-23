package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.dto.lending.admin.LendingSetupRequest;
import com.kucingoyen.microlend.dto.lending.admin.LendingSetupResponse;
import com.kucingoyen.microlend.dto.lending.admin.UpdateConfigRequest;
import com.kucingoyen.microlend.dto.lending.admin.UpdateConfigResponse;
import com.kucingoyen.microlend.model.SystemConfig;
import com.kucingoyen.microlend.repository.SystemConfigRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.CreateResponse;
import com.kucingoyen.microlend.service.DamlLedgerService.ExerciseResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Service for lending system admin operations.
 * Handles initialization and configuration of the lending system.
 */
@Service
@RequiredArgsConstructor
public class LendingAdminService {

    private static final Logger log = LoggerFactory.getLogger(LendingAdminService.class);

    private final DamlLedgerService damlService;
    private final SystemConfigRepository configRepository;
    private final AdminSetupService adminSetupService;

    /**
     * Auto-initialize lending system on application startup.
     * Checks if lending contracts already exist, if not, creates them with default
     * config.
     */
    @PostConstruct
    public void initializeLendingSystem() {
        try {
            log.info("Checking lending system initialization...");

            // Check if lending system is already initialized
            String lendingServiceCid = getLendingServiceContractId();

            if (lendingServiceCid != null) {
                log.info("Lending system already initialized with LendingService: {}", lendingServiceCid);
                return;
            }

            // Auto-initialize with default configuration
            log.info("Lending system not initialized. Setting up with default configuration...");

            LendingSetupRequest defaultConfig = new LendingSetupRequest(
                    new BigDecimal(LendingConstants.DEFAULT_BASE_COLLATERAL_RATE), // 1 CC = 0.16 USDx
                    new BigDecimal("0.05"), // 5% interest rate
                    30); // 30 days loan duration

            LendingSetupResponse response = setupLendingSystem(defaultConfig);

            log.info("Lending system auto-initialized successfully!");
            log.info("  - LendingService: {}", response.getLendingServiceContractId());
            log.info("  - TimeOracle: {}", response.getTimeOracleContractId());
            log.info("  - UserProfileFactory: {}", response.getUserProfileFactoryContractId());

        } catch (Exception e) {
            log.error("Failed to auto-initialize lending system: {}", e.getMessage(), e);
            log.warn("Lending system will need to be initialized manually via /api/admin/lending/setup");
        }
    }

    /**
     * Initialize the lending system by creating LendingService, TimeOracle, and
     * UserProfileFactory contracts.
     *
     * @param request The setup request with initial configuration
     * @return Response with contract IDs
     */
    public LendingSetupResponse setupLendingSystem(LendingSetupRequest request) {
        log.info("Setting up lending system with configuration: {}", request);

        String operatorPartyId = adminSetupService.getAdminPartyId();
        if (operatorPartyId == null) {
            throw new IllegalStateException("Admin party not initialized");
        }

        try {
            // 1. Create LendingService contract
            String lendingServiceCid = createLendingServiceContract(
                    operatorPartyId,
                    request.getBaseCollateralRate().toString(),
                    request.getInterestRate().toString(),
                    request.getLoanDurationDays());

            // 2. Create TimeOracle contract
            String timeOracleCid = createTimeOracleContract(operatorPartyId);

            // 3. Create UserProfileFactory contract
            String userProfileFactoryCid = createUserProfileFactoryContract(operatorPartyId);

            // 4. Save contract IDs to database
            saveContractIds(lendingServiceCid, timeOracleCid, userProfileFactoryCid);

            log.info("Lending system setup completed successfully");

            return new LendingSetupResponse(
                    true,
                    lendingServiceCid,
                    timeOracleCid,
                    userProfileFactoryCid);

        } catch (Exception e) {
            log.error("Failed to setup lending system: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to setup lending system: " + e.getMessage(), e);
        }
    }

    /**
     * Update lending configuration by exercising UpdateServiceConfig choice.
     *
     * @param request The update request with new configuration
     * @return Response with success status
     */
    public UpdateConfigResponse updateConfiguration(UpdateConfigRequest request) {
        log.info("Updating lending configuration: {}", request);

        String operatorPartyId = adminSetupService.getAdminPartyId();
        String lendingServiceCid = getLendingServiceContractId();

        if (lendingServiceCid == null) {
            throw new IllegalStateException("Lending system not initialized. Please run setup first.");
        }

        try {
            // Exercise UpdateServiceConfig choice
            ExerciseResponse response = damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_LENDING_SERVICE,
                    lendingServiceCid,
                    "UpdateServiceConfig",
                    Map.of(
                            "newBaseCollateralRate", request.getNewBaseRate().toString(),
                            "newInterestRate", request.getNewInterestRate().toString(),
                            "newLoanDurationDays", request.getNewDurationDays()),
                    operatorPartyId);

            // UpdateServiceConfig returns new contract ID
            String newLendingServiceCid = extractContractId(response);

            // Update stored contract ID
            if (newLendingServiceCid != null) {
                saveContractId(LendingConstants.CONFIG_LENDING_SERVICE_CID, newLendingServiceCid);
            }

            log.info("Configuration updated successfully. New contract ID: {}", newLendingServiceCid);

            return new UpdateConfigResponse(
                    true,
                    "Configuration updated successfully",
                    newLendingServiceCid);

        } catch (Exception e) {
            log.error("Failed to update configuration: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update configuration: " + e.getMessage(), e);
        }
    }

    /**
     * Get the stored LendingService contract ID.
     */
    public String getLendingServiceContractId() {
        return configRepository.findByKey(LendingConstants.CONFIG_LENDING_SERVICE_CID)
                .map(SystemConfig::getValue)
                .orElse(null);
    }

    /**
     * Get the stored TimeOracle contract ID.
     */
    public String getTimeOracleContractId() {
        return configRepository.findByKey(LendingConstants.CONFIG_TIME_ORACLE_CID)
                .map(SystemConfig::getValue)
                .orElse(null);
    }

    /**
     * Get the stored UserProfileFactory contract ID.
     */
    public String getUserProfileFactoryContractId() {
        return configRepository.findByKey(LendingConstants.CONFIG_USER_PROFILE_FACTORY_CID)
                .map(SystemConfig::getValue)
                .orElse(null);
    }

    // Private helper methods

    private String createLendingServiceContract(String operator, String baseRate, String interestRate,
            int durationDays) {
        CreateResponse response = damlService.createContract(
                LendingConstants.TEMPLATE_LENDING_SERVICE,
                Map.of(
                        "operator", operator,
                        "baseCollateralRate", baseRate,
                        "interestRate", interestRate,
                        "loanDurationDays", durationDays,
                        "observers", List.of()),
                operator);

        if (response != null && response.result() != null) {
            String contractId = response.result().contractId();
            log.info("Created LendingService contract: {}", contractId);
            return contractId;
        }

        throw new IllegalStateException("Failed to create LendingService contract");
    }

    private String createTimeOracleContract(String operator) {
        CreateResponse response = damlService.createContract(
                LendingConstants.TEMPLATE_TIME_ORACLE,
                Map.of(
                        "operator", operator,
                        "currentTime", Instant.now().toString(),
                        "observers", List.of()),
                operator);

        if (response != null && response.result() != null) {
            String contractId = response.result().contractId();
            log.info("Created TimeOracle contract: {}", contractId);
            return contractId;
        }

        throw new IllegalStateException("Failed to create TimeOracle contract");
    }

    private String createUserProfileFactoryContract(String operator) {
        CreateResponse response = damlService.createContract(
                LendingConstants.TEMPLATE_USER_PROFILE_FACTORY,
                Map.of("operator", operator),
                operator);

        if (response != null && response.result() != null) {
            String contractId = response.result().contractId();
            log.info("Created UserProfileFactory contract: {}", contractId);
            return contractId;
        }

        throw new IllegalStateException("Failed to create UserProfileFactory contract");
    }

    private void saveContractIds(String lendingServiceCid, String timeOracleCid, String userProfileFactoryCid) {
        configRepository.save(new SystemConfig(LendingConstants.CONFIG_LENDING_SERVICE_CID, lendingServiceCid));
        configRepository.save(new SystemConfig(LendingConstants.CONFIG_TIME_ORACLE_CID, timeOracleCid));
        configRepository
                .save(new SystemConfig(LendingConstants.CONFIG_USER_PROFILE_FACTORY_CID, userProfileFactoryCid));
        log.info("Saved lending contract IDs to database");
    }

    private void saveContractId(String key, String contractId) {
        configRepository.save(new SystemConfig(key, contractId));
    }

    private String extractContractId(ExerciseResponse response) {
        if (response != null && response.result() != null) {
            Object result = response.result().exerciseResult();
            if (result instanceof String) {
                return (String) result;
            }
        }
        return null;
    }
}
