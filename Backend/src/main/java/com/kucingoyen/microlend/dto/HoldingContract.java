package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Internal DTO representing a DAML Holding contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingContract {

    private String contractId;
    private String owner;
    private String custodian;
    private AssetId assetId;
    private BigDecimal amount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetId {
        private String issuer;
        private String symbol;
        private String description;
    }
}
