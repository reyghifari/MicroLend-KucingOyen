package com.kucingoyen.microlend.dto.lending.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a loan request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanRequestRequest {

    private BigDecimal loanAmount;
    private String collateralHoldingContractId;
}
