package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.dto.lending.marketplace.*;
import com.kucingoyen.microlend.dto.lending.marketplace.CreateLoanRequestResponse.LoanDetails;
import com.kucingoyen.microlend.exception.NotFoundException;
import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import com.kucingoyen.microlend.service.DamlLedgerService.ExerciseResponse;
import com.kucingoyen.microlend.util.DamlPayloadParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for loan marketplace operations.
 */
@Service
@RequiredArgsConstructor
public class LoanMarketplaceService {

    private static final Logger log = LoggerFactory.getLogger(LoanMarketplaceService.class);

    private final DamlLedgerService damlService;
    private final UserRepository userRepository;
    private final AdminSetupService adminSetupService;
    private final LendingAdminService lendingAdminService;
    private final LendingProfileService lendingProfileService;

    /**
     * Create a loan request.
     */
    public CreateLoanRequestResponse createLoanRequest(String borrowerEmail, CreateLoanRequestRequest request) {
        log.info("Creating loan request for borrower: {}, amount: {}", borrowerEmail, request.getLoanAmount());

        Users borrower = userRepository.findByEmail(borrowerEmail)
                .orElseThrow(() -> new NotFoundException("Borrower not found"));

        String borrowerPartyId = borrower.getDamlPartyId();
        String operatorPartyId = adminSetupService.getAdminPartyId();
        String lendingServiceCid = lendingAdminService.getLendingServiceContractId();

        // Get borrower profile for level
        var profile = lendingProfileService.getUserProfile(borrowerEmail);

        try {
            // Exercise RequestLoanWithSplit using submitMulti (operator + borrower)
            ExerciseResponse response = damlService.exerciseChoiceMulti(
                    LendingConstants.TEMPLATE_LENDING_SERVICE,
                    lendingServiceCid,
                    "RequestLoanWithSplit",
                    Map.of(
                            "borrower", borrowerPartyId,
                            "loanAmount", request.getLoanAmount().toString(),
                            "loanAssetId", getAssetId(operatorPartyId, "CC"),
                            "collateralAssetId", getAssetId(operatorPartyId, "USDx"),
                            "collateralHoldingCid", request.getCollateralHoldingContractId(),
                            "borrowerLevel", profile.getLevel(),
                            "requestTime", Instant.now().toString()),
                    List.of(operatorPartyId, borrowerPartyId));

            // Extract contract IDs from response
            Map<String, String> contractIds = extractTupleResult(response);
            String loanRequestCid = contractIds.get("loanRequest");
            String remainderCid = contractIds.get("remainder");

            // Calculate loan details
            BigDecimal requiredRepayment = request.getLoanAmount()
                    .multiply(BigDecimal.ONE.add(profile.getCollateralRate()));

            LoanDetails details = new LoanDetails(
                    request.getLoanAmount(),
                    request.getLoanAmount().multiply(profile.getCollateralRate()),
                    new BigDecimal("0.05"), // From config
                    30, // From config
                    requiredRepayment);

            return new CreateLoanRequestResponse(true, loanRequestCid, remainderCid, details);

        } catch (Exception e) {
            log.error("Failed to create loan request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create loan request: " + e.getMessage(), e);
        }
    }

    /**
     * Get marketplace loan requests.
     * Shows loan requests from OTHER users (not the current user's own requests).
     * 
     * Queries as OPERATOR to see all LoanRequest contracts (operator is signatory).
     * Individual users cannot see other users' requests due to DAML visibility
     * rules.
     */
    public MarketplaceResponse getMarketplace(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Get operator party ID - operator is signatory on ALL loan requests
        String operatorPartyId = adminSetupService.getAdminPartyId();

        // Query as OPERATOR to see all LoanRequest contracts
        // Users can't see other users' contracts due to DAML visibility
        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_LOAN_REQUEST,
                Map.of(),
                operatorPartyId); // ← Query as OPERATOR, not as user!

        String currentUserPartyId = user.getDamlPartyId();

        // Filter OUT current user's loan requests - marketplace shows OTHER users'
        // requests
        List<LoanRequestDto> loans = results.stream()
                .map(this::convertToLoanRequestDto)
                .filter(loan -> !loan.getBorrower().equals(currentUserPartyId)) // Exclude own requests
                .collect(Collectors.toList());

        return new MarketplaceResponse(loans, loans.size());
    }

    /**
     * Get current user's loan requests.
     * Shows ONLY loan requests created by the current user.
     */
    public MarketplaceResponse getMyRequests(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String userPartyId = user.getDamlPartyId();
        String operatorPartyId = adminSetupService.getAdminPartyId();

        // Query as OPERATOR to see all LoanRequest contracts
        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_LOAN_REQUEST,
                Map.of(),
                operatorPartyId);

        // Filter to show ONLY current user's requests
        List<LoanRequestDto> myRequests = results.stream()
                .map(this::convertToLoanRequestDto)
                .filter(loan -> loan.getBorrower().equals(userPartyId)) // Include ONLY own requests
                .collect(Collectors.toList());

        return new MarketplaceResponse(myRequests, myRequests.size());
    }

    /**
     * Fill a loan request.
     * Uses multi-party exercise: operator provides visibility, lender executes.
     * Automatically splits CC holding if larger than needed and returns remainder.
     * After fill, auto-merges the borrower's CC holdings so the received CC is
     * consolidated with any CC the borrower already held.
     */
    public FillLoanResponse fillLoanRequest(String lenderEmail, FillLoanRequest request) {
        Users lender = userRepository.findByEmail(lenderEmail)
                .orElseThrow(() -> new NotFoundException("Lender not found"));

        String operatorPartyId = adminSetupService.getAdminPartyId();
        String lenderPartyId = lender.getDamlPartyId();

        try {
            // Pre-fetch the LoanRequest to get the borrower's party ID for the post-fill
            // merge
            ContractResult loanRequestContract = damlService.queryContracts(
                    LendingConstants.TEMPLATE_LOAN_REQUEST,
                    Map.of(),
                    operatorPartyId)
                    .stream()
                    .filter(c -> c.contractId().equals(request.getContractId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("LoanRequest not found: " + request.getContractId()));
            String borrowerPartyId = (String) loanRequestContract.payload().get("borrower");

            // Use multi-party exercise:
            // - Operator provides READ access (can see all LoanRequest contracts)
            // - Lender provides ACT authority (controller of FillLoanWithSplit choice)
            ExerciseResponse response = damlService.exerciseChoiceMulti(
                    LendingConstants.TEMPLATE_LOAN_REQUEST,
                    request.getContractId(),
                    "FillLoanWithSplit",
                    Map.of(
                            "lender", lenderPartyId,
                            "loanHoldingCid", request.getLoanHoldingContractId()),
                    List.of(operatorPartyId, lenderPartyId));

            // Extract result tuple: (activeLoanCid, remainderCid)
            // DAML tuples serialize as {"_1": ..., "_2": ...}
            Map<String, String> contractIds = extractTupleResult(response);
            String activeLoanCid = contractIds.get("_1");
            String remainderCid = contractIds.get("_2");

            // Auto-merge borrower's CC holdings — the newly received CC is now in their
            // wallet alongside any CC they already held.
            try {
                List<ContractResult> borrowerCcHoldings = damlService.queryContracts(
                        "MicroLend.Finance.Holding:Holding",
                        Map.of("owner", borrowerPartyId),
                        borrowerPartyId)
                        .stream()
                        .filter(h -> "CC".equals(h.payload().get("assetId") instanceof Map<?, ?> assetMap
                                ? assetMap.get("symbol")
                                : null))
                        .collect(Collectors.toList());

                if (borrowerCcHoldings.size() > 1) {
                    log.info("Auto-merging {} CC holdings for borrower after loan fill", borrowerCcHoldings.size());
                    String baseCid = borrowerCcHoldings.get(0).contractId();
                    for (int i = 1; i < borrowerCcHoldings.size(); i++) {
                        ExerciseResponse mergeResp = damlService.exerciseChoice(
                                "MicroLend.Finance.Holding:Holding",
                                baseCid,
                                "Merge",
                                Map.of("otherHoldingCid", borrowerCcHoldings.get(i).contractId()),
                                borrowerPartyId);
                        Object mergeResult = mergeResp != null && mergeResp.result() != null
                                ? mergeResp.result().exerciseResult()
                                : null;
                        if (mergeResult instanceof String merged)
                            baseCid = merged;
                    }
                    log.info("Borrower CC auto-merge complete after loan fill");
                }
            } catch (Exception mergeEx) {
                log.warn("Auto-merge of borrower CC after fill failed (non-fatal): {}", mergeEx.getMessage());
            }

            // Update lender profile: record the amount they just lent
            BigDecimal loanAmountFilled = DamlPayloadParser.parseBigDecimal(
                    loanRequestContract.payload().get("loanAmount"));
            lendingProfileService.recordLendingActivity(lenderPartyId, loanAmountFilled);

            return new FillLoanResponse(
                    true,
                    activeLoanCid,
                    remainderCid,
                    "Loan funded successfully. Remainder returned to lender.");

        } catch (Exception e) {
            log.error("Failed to fill loan: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fill loan: " + e.getMessage(), e);
        }
    }

    /**
     * Cancel a loan request.
     */
    public void cancelLoanRequest(String borrowerEmail, String contractId) {
        Users borrower = userRepository.findByEmail(borrowerEmail)
                .orElseThrow(() -> new NotFoundException("Borrower not found"));

        try {
            damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_LOAN_REQUEST,
                    contractId,
                    "CancelRequest",
                    Map.of(),
                    borrower.getDamlPartyId());

        } catch (Exception e) {
            log.error("Failed to cancel loan request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to cancel loan request: " + e.getMessage(), e);
        }
    }

    // Helper methods

    private Map<String, Object> getAssetId(String issuer, String symbol) {
        String description = symbol.equals("CC") ? "Collateral Coin Token" : "USD Stablecoin Token";
        return Map.of("issuer", issuer, "symbol", symbol, "description", description);
    }

    private LoanRequestDto convertToLoanRequestDto(ContractResult contract) {
        Map<String, Object> payload = contract.payload();

        return new LoanRequestDto(
                contract.contractId(),
                (String) payload.get("borrower"),
                "User", // Could fetch from Users table
                DamlPayloadParser.parseBigDecimal(payload.get("loanAmount")),
                DamlPayloadParser.parseBigDecimal(payload.get("collateralAmount")),
                "CC",
                "USDx",
                DamlPayloadParser.parseBigDecimal(payload.get("interestRate")),
                DamlPayloadParser.parseInteger(payload.get("durationDays")),
                (String) payload.get("borrowerLevel"),
                (String) payload.get("requestedAt"),
                DamlPayloadParser.parseBigDecimal(payload.get("requiredRepayment")),
                new BigDecimal("0.16"));
    }

    private Map<String, String> extractTupleResult(ExerciseResponse response) {
        if (response != null && response.result() != null) {
            Object result = response.result().exerciseResult();
            if (result instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultMap = (Map<String, Object>) result;
                // DAML JSON serialises tuples as {"_1": ..., "_2": ...}
                return Map.of(
                        "_1", String.valueOf(resultMap.getOrDefault("_1", "")),
                        "_2", String.valueOf(resultMap.getOrDefault("_2", "")));
            }
        }
        return Map.of();
    }
}
