package com.kucingoyen.microlend.dto.lending.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for filling a loan request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FillLoanRequest {
    private String contractId;
    private String loanHoldingContractId;
}
