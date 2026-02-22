package com.kucingoyen.microlend.dto.lending.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for loan repayment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepayLoanResponse {

    private boolean success;
    private String repaidHoldingContractId;
    private String returnedCollateralContractId;
    private BigDecimal repaymentAmount;
    private BigDecimal collateralReturned;
    private String message;
}
