package com.kucingoyen.microlend.dto.lending.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for loan liquidation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiquidateResponse {

    private boolean success;
    private String claimedCollateralContractId;
    private BigDecimal collateralAmount;
    private String message;
}
