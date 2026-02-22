package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for merging multiple holdings into one.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeHoldingsRequest {

    /**
     * List of holding contract IDs to merge.
     * All holdings must be the same asset and owned by the same user.
     */
    private List<String> holdingContractIds;

    /**
     * Asset symbol (CC or USDx).
     * Used for validation - all holdings must match this symbol.
     */
    private String assetSymbol;
}
