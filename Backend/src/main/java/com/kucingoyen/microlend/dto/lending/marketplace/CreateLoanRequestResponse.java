package com.kucingoyen.microlend.dto.lending.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for creating a loan request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanRequestResponse {

    private boolean success;
    private String loanRequestContractId;
    private String remainderHoldingContractId;
    private LoanDetails loanDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanDetails {
        private BigDecimal loanAmount;
        private BigDecimal collateralAmount;
        private BigDecimal interestRate;
        private Integer durationDays;
        private BigDecimal requiredRepayment;
    }
}
