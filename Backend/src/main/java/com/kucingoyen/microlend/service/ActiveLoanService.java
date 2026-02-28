package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.dto.lending.loan.*;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for active loan management operations.
 */
@Service
@RequiredArgsConstructor
public class ActiveLoanService {

    private static final Logger log = LoggerFactory.getLogger(ActiveLoanService.class);

    private final DamlLedgerService damlService;
    private final UserRepository userRepository;
    private final AdminSetupService adminSetupService;
    private final LendingProfileService lendingProfileService;
    private final TimeOracleService timeOracleService;

    /**
     * Get active loans for a user (as borrower and lender).
     */
    public MyLoansResponse getMyLoans(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String partyId = user.getDamlPartyId();

        // Query all active loans
        List<ContractResult> allLoans = damlService.queryContracts(
                LendingConstants.TEMPLATE_ACTIVE_LOAN,
                Map.of(),
                partyId);

        List<ActiveLoanDto> asBorrower = allLoans.stream()
                .filter(loan -> partyId.equals(loan.payload().get("borrower")))
                .map(this::convertToActiveLoanDto)
                .collect(Collectors.toList());

        List<ActiveLoanDto> asLender = allLoans.stream()
                .filter(loan -> partyId.equals(loan.payload().get("lender")))
                .map(this::convertToActiveLoanDto)
                .collect(Collectors.toList());

        return new MyLoansResponse(asBorrower, asLender);
    }

    /**
     * Get loans funded by user (as lender only).
     * Shows all active loans where user provided the funding.
     */
    public List<ActiveLoanDto> getFundedLoans(String lenderEmail) {
        Users lender = userRepository.findByEmail(lenderEmail)
                .orElseThrow(() -> new NotFoundException("Lender not found"));

        String lenderPartyId = lender.getDamlPartyId();

        // Query all active loans where user is lender
        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_ACTIVE_LOAN,
                Map.of("lender", lenderPartyId),
                lenderPartyId);

        // Convert to DTOs
        return results.stream()
                .map(this::convertToActiveLoanDto)
                .collect(Collectors.toList());
    }

    public List<ActiveLoanDto> getFundedRequest(String borrowerEmail) {
        Users lender = userRepository.findByEmail(borrowerEmail)
                .orElseThrow(() -> new NotFoundException("Borrower not found"));

        String lenderPartyId = lender.getDamlPartyId();

        // Query all active loans where user is lender
        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_ACTIVE_LOAN,
                Map.of("borrower", lenderPartyId),
                lenderPartyId);

        // Convert to DTOs
        return results.stream()
                .map(this::convertToActiveLoanDto)
                .collect(Collectors.toList());
    }

    /**
     * Repay a loan.
     * <ol>
     * <li>Fetches the ActiveLoan to determine exact repayment amount.</li>
     * <li>Splits the borrower's CC holding if it is larger than required
     * so only the exact repayment amount is sent to the lender.</li>
     * <li>Exercises the DAML {@code Repay} choice and extracts the returned
     * collateral contract ID from the result tuple.</li>
     * <li>Auto-merges the returned USDx collateral with any other USDx
     * holdings the borrower already holds.</li>
     * </ol>
     */
    public RepayLoanResponse repayLoan(String borrowerEmail, RepayLoanRequest request) {
        Users borrower = userRepository.findByEmail(borrowerEmail)
                .orElseThrow(() -> new NotFoundException("Borrower not found"));

        String borrowerPartyId = borrower.getDamlPartyId();

        try {
            // --- Step 1: Fetch the ActiveLoan to get repayment details ---
            ContractResult loanContract = damlService.queryContracts(
                    LendingConstants.TEMPLATE_ACTIVE_LOAN,
                    Map.of(),
                    borrowerPartyId)
                    .stream()
                    .filter(c -> c.contractId().equals(request.getContractId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("ActiveLoan not found: " + request.getContractId()));

            BigDecimal loanAmount = DamlPayloadParser.parseBigDecimal(loanContract.payload().get("loanAmount"));
            BigDecimal interestRate = DamlPayloadParser.parseBigDecimal(loanContract.payload().get("interestRate"));
            // requiredRepayment = loanAmount * (1 + interestRate), rounded up to 10 dp
            BigDecimal requiredRepayment = loanAmount
                    .multiply(BigDecimal.ONE.add(interestRate))
                    .setScale(10, RoundingMode.CEILING);

            log.info("Repaying loan {}. Required: {} CC", request.getContractId(), requiredRepayment);

            // --- Step 2: Split CC holding if larger than required ---
            // originalCid is effectively final — safe to capture in lambdas below.
            // actualRepaymentCid may be replaced with the split result.
            final String originalCid = request.getRepaymentHoldingContractId();
            String actualRepaymentCid = originalCid;

            ContractResult ccHolding = damlService.queryContracts(
                    "MicroLend.Finance.Holding:Holding",
                    Map.of("owner", borrowerPartyId),
                    borrowerPartyId)
                    .stream()
                    .filter(c -> c.contractId().equals(originalCid))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("CC holding not found: " + originalCid));

            BigDecimal ccAmount = DamlPayloadParser.parseBigDecimal(ccHolding.payload().get("amount"));

            if (ccAmount.compareTo(requiredRepayment) > 0) {
                // Split: exact repayment goes to lender, remainder stays with borrower
                log.info("Splitting CC holding ({}) → exact repayment ({})", ccAmount, requiredRepayment);
                ExerciseResponse splitResp = damlService.exerciseChoice(
                        "MicroLend.Finance.Holding:Holding",
                        originalCid,
                        "Split",
                        Map.of("splitAmount", requiredRepayment.toPlainString()),
                        borrowerPartyId);
                actualRepaymentCid = extractFirstFromTuple(splitResp);
                if (actualRepaymentCid == null) {
                    throw new IllegalStateException("Failed to split CC holding before repayment");
                }
            }

            // --- Step 3: Exercise Repay ---
            ExerciseResponse repayResp = damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_ACTIVE_LOAN,
                    request.getContractId(),
                    "Repay",
                    Map.of("repaymentHoldingCid", actualRepaymentCid),
                    borrowerPartyId);

            // Repay returns (repaidHoldingCid, collateralReturnCid)
            String collateralReturnCid = extractSecondFromTuple(repayResp);
            log.info("Loan repaid. Collateral returned: {}", collateralReturnCid);

            // --- Step 3.5: Auto-merge lender's CC holdings ---
            // The repayment CC just arrived in the lender's wallet. Merge it with any
            // other CC holdings the lender already owns, acting as the lender party.
            String lenderPartyId = (String) loanContract.payload().get("lender");
            try {
                List<ContractResult> lenderCcHoldings = damlService.queryContracts(
                        "MicroLend.Finance.Holding:Holding",
                        Map.of("owner", lenderPartyId),
                        lenderPartyId)
                        .stream()
                        .filter(h -> "CC".equals(h.payload().get("assetId") instanceof Map<?, ?> assetMap
                                ? assetMap.get("symbol")
                                : null))
                        .collect(Collectors.toList());

                if (lenderCcHoldings.size() > 1) {
                    log.info("Auto-merging {} CC holdings for lender after repayment", lenderCcHoldings.size());
                    String baseCcCid = lenderCcHoldings.get(0).contractId();
                    for (int i = 1; i < lenderCcHoldings.size(); i++) {
                        ExerciseResponse mergeResp = damlService.exerciseChoice(
                                "MicroLend.Finance.Holding:Holding",
                                baseCcCid,
                                "Merge",
                                Map.of("otherHoldingCid", lenderCcHoldings.get(i).contractId()),
                                lenderPartyId);
                        String mergedCid = extractSingleContractId(mergeResp);
                        if (mergedCid != null)
                            baseCcCid = mergedCid;
                    }
                    log.info("Lender CC auto-merge complete. Final CID: {}", baseCcCid);
                }
            } catch (Exception mergeEx) {
                log.warn("Auto-merge of lender CC after repayment failed (non-fatal): {}", mergeEx.getMessage());
            }

            // --- Step 4: Auto-merge returned USDx collateral with existing USDx holdings
            // ---
            try {
                List<ContractResult> usdxHoldings = damlService.queryContracts(
                        "MicroLend.Finance.Holding:Holding",
                        Map.of("owner", borrowerPartyId),
                        borrowerPartyId)
                        .stream()
                        .filter(h -> "USDx".equals(h.payload().get("assetId") instanceof Map<?, ?> assetMap
                                ? assetMap.get("symbol")
                                : null))
                        .collect(Collectors.toList());

                List<String> usdxCids = new ArrayList<>();
                for (ContractResult h : usdxHoldings) {
                    usdxCids.add(h.contractId());
                }

                // Make sure the returned collateral is in the list; the DAML ledger
                // may take a moment to reflect it, but typically it's queryable immediately.
                if (usdxCids.size() > 1) {
                    log.info("Auto-merging {} USDx holdings after repayment", usdxCids.size());
                    String baseHoldingCid = usdxCids.get(0);
                    for (int i = 1; i < usdxCids.size(); i++) {
                        ExerciseResponse mergeResp = damlService.exerciseChoice(
                                "MicroLend.Finance.Holding:Holding",
                                baseHoldingCid,
                                "Merge",
                                Map.of("otherHoldingCid", usdxCids.get(i)),
                                borrowerPartyId);
                        String mergedCid = extractSingleContractId(mergeResp);
                        if (mergedCid != null)
                            baseHoldingCid = mergedCid;
                    }
                    log.info("USDx auto-merge complete. Final CID: {}", baseHoldingCid);
                    collateralReturnCid = baseHoldingCid; // Return the merged CID
                }
            } catch (Exception mergeEx) {
                log.warn("Auto-merge of USDx after repayment failed (non-fatal): {}", mergeEx.getMessage());
            }

            // --- Step 5: Update borrower profile (record completion + auto-upgrade level)
            // ---
            lendingProfileService.recordBorrowCompletion(borrowerPartyId, loanAmount);

            return new RepayLoanResponse(
                    true, actualRepaymentCid, collateralReturnCid,
                    requiredRepayment, loanContract.payload().get("collateralAmount") instanceof String s
                            ? new BigDecimal(s)
                            : BigDecimal.ZERO,
                    "Loan repaid successfully. Collateral returned and merged.");

        } catch (Exception e) {
            log.error("Failed to repay loan: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to repay loan: " + e.getMessage(), e);
        }
    }

    // ---- Tuple extraction helpers ----

    /**
     * Extracts the first element from a DAML tuple result ({"_1": ..., "_2": ...}
     * or [a, b]).
     */
    private String extractFirstFromTuple(ExerciseResponse response) {
        if (response == null || response.result() == null)
            return null;
        Object result = response.result().exerciseResult();
        if (result instanceof Map<?, ?> map) {
            Object v = map.get("_1");
            return v instanceof String s ? s : null;
        }
        if (result instanceof List<?> list && !list.isEmpty()) {
            return list.get(0) instanceof String s ? s : null;
        }
        return result instanceof String s ? s : null;
    }

    /** Extracts the second element from a DAML tuple result. */
    private String extractSecondFromTuple(ExerciseResponse response) {
        if (response == null || response.result() == null)
            return null;
        Object result = response.result().exerciseResult();
        if (result instanceof Map<?, ?> map) {
            Object v = map.get("_2");
            return v instanceof String s ? s : null;
        }
        if (result instanceof List<?> list && list.size() >= 2) {
            return list.get(1) instanceof String s ? s : null;
        }
        return null;
    }

    /** Extracts a direct single contract ID (for Merge choice). */
    private String extractSingleContractId(ExerciseResponse response) {
        if (response == null || response.result() == null)
            return null;
        Object result = response.result().exerciseResult();
        return result instanceof String s ? s : null;
    }

    /**
     * Liquidate an expired loan.
     * Uses TimeOracle to validate loan expiry in a trustless manner.
     * 
     * @param lenderEmail Email of lender claiming collateral
     * @param contractId  ActiveLoan contract ID
     * @return LiquidateResponse with collateral details
     */
    public LiquidateResponse liquidateLoan(String lenderEmail, String contractId) {
        Users lender = userRepository.findByEmail(lenderEmail)
                .orElseThrow(() -> new NotFoundException("Lender not found"));

        try {
            // Get current time from TimeOracle contract
            String currentTime = timeOracleService.getCurrentTime();

            log.info("Liquidating loan {} with oracle time: {}", contractId, currentTime);

            // Exercise Liquidate choice with oracle time
            damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_ACTIVE_LOAN,
                    contractId,
                    "Liquidate",
                    Map.of("currentTime", currentTime),
                    lender.getDamlPartyId());

            return new LiquidateResponse(
                    true, contractId, BigDecimal.ZERO,
                    "Loan liquidated. Collateral claimed.");

        } catch (Exception e) {
            log.error("Failed to liquidate loan: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to liquidate loan: " + e.getMessage(), e);
        }
    }

    private ActiveLoanDto convertToActiveLoanDto(ContractResult contract) {
        Map<String, Object> payload = contract.payload();

        return new ActiveLoanDto(
                contract.contractId(),
                (String) payload.get("lender"),
                (String) payload.get("borrower"),
                "Lender", "Borrower",
                DamlPayloadParser.parseBigDecimal(payload.get("loanAmount")),
                DamlPayloadParser.parseBigDecimal(payload.get("collateralAmount")),
                DamlPayloadParser.parseBigDecimal(payload.get("interestRate")),
                BigDecimal.ZERO, BigDecimal.ZERO,
                (String) payload.get("startTime"),
                (String) payload.get("endTime"),
                "Active", 0, false, false);
    }
}
