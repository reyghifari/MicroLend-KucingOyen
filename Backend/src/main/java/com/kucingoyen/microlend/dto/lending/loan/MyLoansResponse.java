package com.kucingoyen.microlend.dto.lending.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for my loans listing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyLoansResponse {

    private List<ActiveLoanDto> asBorrower;
    private List<ActiveLoanDto> asLender;
}
