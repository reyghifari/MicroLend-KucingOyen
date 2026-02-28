package com.kucingoyen.microlend.dto.lending.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for repaying a loan.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepayLoanRequest {

    private String contractId;
    private String repaymentHoldingContractId;
}
