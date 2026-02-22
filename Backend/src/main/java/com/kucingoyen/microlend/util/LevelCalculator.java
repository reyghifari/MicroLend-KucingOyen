package com.kucingoyen.microlend.util;

import com.kucingoyen.microlend.constant.LendingConstants;

import java.math.BigDecimal;

/**
 * Utility class for calculating collateral rates and level bonuses.
 */
public class LevelCalculator {

    /**
     * Calculate the total collateral rate for a user level.
     * Total rate = base rate + level bonus
     *
     * @param baseCollateralRate The base collateral rate (e.g., 1.10)
     * @param userLevel          The user's level (Level1, Level2, etc.)
     * @return The total collateral rate
     */
    public static BigDecimal calculateCollateralRate(BigDecimal baseCollateralRate, String userLevel) {
        BigDecimal levelBonus = getLevelBonus(userLevel);
        return baseCollateralRate.add(levelBonus);
    }

    /**
     * Get the level bonus percentage for a user level.
     *
     * @param userLevel The user's level
     * @return The bonus percentage (e.g., 0.05 for Level1)
     */
    public static BigDecimal getLevelBonus(String userLevel) {
        return switch (userLevel) {
            case LendingConstants.LEVEL_1 -> new BigDecimal(LendingConstants.LEVEL1_BONUS);
            case LendingConstants.LEVEL_2 -> new BigDecimal(LendingConstants.LEVEL2_BONUS);
            case LendingConstants.LEVEL_3 -> new BigDecimal(LendingConstants.LEVEL3_BONUS);
            case LendingConstants.LEVEL_4 -> new BigDecimal(LendingConstants.LEVEL4_BONUS);
            case LendingConstants.LEVEL_5 -> new BigDecimal(LendingConstants.LEVEL5_BONUS);
            default -> new BigDecimal(LendingConstants.LEVEL1_BONUS); // Default to Level1
        };
    }

    /**
     * Get a human-readable description of the collateral rate calculation.
     *
     * @param baseRate  The base rate
     * @param userLevel The user level
     * @param totalRate The total calculated rate
     * @return Description string
     */
    public static String getCollateralRateDescription(BigDecimal baseRate, String userLevel, BigDecimal totalRate) {
        BigDecimal bonus = getLevelBonus(userLevel);
        int basePercent = baseRate.multiply(BigDecimal.valueOf(100)).intValue();
        int bonusPercent = bonus.multiply(BigDecimal.valueOf(100)).intValue();

        return String.format("%d%% base + %d%% (%s)", basePercent, bonusPercent, userLevel);
    }

    private LevelCalculator() {
        // Prevent instantiation
    }
}
