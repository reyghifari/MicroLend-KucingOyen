package com.kucingoyen.microlend.dto.lending.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for filling a loan request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FillLoanResponse {

    private boolean success;
    private String activeLoanContractId;
    private String remainderHoldingCid; // Remainder CC returned to lender
    private String message;
}
