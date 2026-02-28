package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for balance query.
 * <p>
 * {@code balances} and {@code holdings} reflect only <em>spendable</em> tokens.
 * USDx locked as collateral in open loan requests is shown separately in
 * {@code lockedCollateral} and is excluded from the spendable totals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {

    /**
     * Spendable aggregate balances by asset symbol, e.g. {"CC": 100, "USDx": 50}
     */
    private Map<String, BigDecimal> balances;

    /** Spendable individual holdings grouped by asset symbol */
    private Map<String, List<HoldingDto>> holdings;

    /** USDx locked as collateral in open loan requests (not spendable) */
    private Map<String, BigDecimal> lockedCollateral;

    /** Individual locked holdings (collateralHoldingCid → amount) */
    private List<LockedCollateralDto> lockedHoldings;

    /**
     * Convenience constructor for backward-compatibility (no locked collateral).
     */
    public BalanceResponse(Map<String, BigDecimal> balances,
            Map<String, List<HoldingDto>> holdings) {
        this(balances, holdings, Map.of(), List.of());
    }
}
