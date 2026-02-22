package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.dto.lending.loan.*;
import com.kucingoyen.microlend.exception.NotFoundException;
import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import com.kucingoyen.microlend.util.DamlPayloadParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    /**
     * Repay a loan.
     */
    public RepayLoanResponse repayLoan(String borrowerEmail, String contractId, RepayLoanRequest request) {
        Users borrower = userRepository.findByEmail(borrowerEmail)
                .orElseThrow(() -> new NotFoundException("Borrower not found"));

        try {
            damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_ACTIVE_LOAN,
                    contractId,
                    "Repay",
                    Map.of("repaymentHoldingCid", request.getRepaymentHoldingContractId()),
                    borrower.getDamlPartyId());

            return new RepayLoanResponse(
                    true, contractId, contractId,
                    BigDecimal.ZERO, BigDecimal.ZERO,
                    "Loan repaid successfully. Collateral returned.");

        } catch (Exception e) {
            log.error("Failed to repay loan: {)", e.getMessage(), e);
            throw new RuntimeException("Failed to repay loan: " + e.getMessage(), e);
        }
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
