package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for merge holdings operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeHoldingsResponse {

    /**
     * Contract ID of the resulting merged holding.
     */
    private String mergedHoldingContractId;

    /**
     * Total amount in the merged holding.
     */
    private BigDecimal totalAmount;

    /**
     * Asset symbol.
     */
    private String assetSymbol;

    /**
     * Number of holdings that were merged.
     */
    private int holdingsMerged;

    /**
     * Status message.
     */
    private String message;
}
