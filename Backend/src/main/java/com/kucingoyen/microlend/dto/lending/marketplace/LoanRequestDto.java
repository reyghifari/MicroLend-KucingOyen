package com.kucingoyen.microlend.dto.lending.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for a loan request in the marketplace.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDto {

    private String contractId;
    private String borrower;
    private String borrowerDisplayName;
    private BigDecimal loanAmount;
    private BigDecimal collateralAmount;
    private String loanAsset;
    private String collateralAsset;
    private BigDecimal interestRate;
    private Integer durationDays;
    private String borrowerLevel;
    private String requestedAt;
    private BigDecimal expectedRepayment;
    private BigDecimal collateralRatio;
}
