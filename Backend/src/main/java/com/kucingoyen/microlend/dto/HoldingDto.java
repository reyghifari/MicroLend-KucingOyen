package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for individual holding information.
 * Used in balance response to show holdings with contract IDs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingDto {

    private String contractId;
    private BigDecimal amount;
    private String assetSymbol;
    private String assetDescription;
}
