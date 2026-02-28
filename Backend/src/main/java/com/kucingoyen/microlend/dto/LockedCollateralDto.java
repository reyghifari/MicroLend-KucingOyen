package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a single holding that is locked as collateral in an open
 * LoanRequest.
 * These holdings are excluded from the spendable balance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockedCollateralDto {

    /** Contract ID of the locked Holding */
    private String holdingContractId;

    /** Contract ID of the LoanRequest that references this collateral */
    private String loanRequestContractId;

    /** Amount of locked collateral */
    private BigDecimal amount;

    /** Asset symbol of the locked collateral (e.g. "USDx") */
    private String symbol;
}
