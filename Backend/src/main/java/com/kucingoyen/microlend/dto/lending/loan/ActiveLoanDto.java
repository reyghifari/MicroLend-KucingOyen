package com.kucingoyen.microlend.dto.lending.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for active loan display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveLoanDto {

    private String contractId;
    private String lender;
    private String borrower;
    private String lenderDisplayName;
    private String borrowerDisplayName;
    private BigDecimal loanAmount;
    private BigDecimal collateralAmount;
    private BigDecimal interestRate;
    private BigDecimal requiredRepayment;
    private BigDecimal expectedReturn;
    private String startTime;
    private String endTime;
    private String status;
    private Integer daysRemaining;
    private Boolean isOverdue;
    private Boolean canLiquidate;
}
