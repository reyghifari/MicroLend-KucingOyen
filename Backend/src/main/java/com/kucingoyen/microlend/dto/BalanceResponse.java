package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for balance query.
 * Contains both aggregate balances and individual holdings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {

    private Map<String, BigDecimal> balances;
    private Map<String, List<HoldingDto>> holdings;
}
