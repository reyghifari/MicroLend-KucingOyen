package com.kucingoyen.microlend.dto.lending.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for marketplace listing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketplaceResponse {

    private List<LoanRequestDto> loanRequests;
    private Integer totalCount;
}
