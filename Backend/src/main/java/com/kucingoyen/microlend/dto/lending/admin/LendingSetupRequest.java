package com.kucingoyen.microlend.dto.lending.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for setting up the lending system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingSetupRequest {

    /**
     * Base collateral rate (e.g., 1.10 for 110%)
     */
    private BigDecimal baseCollateralRate;

    /**
     * Interest rate (e.g., 0.05 for 5%)
     */
    private BigDecimal interestRate;

    /**
     * Loan duration in days
     */
    private Integer loanDurationDays;
}
