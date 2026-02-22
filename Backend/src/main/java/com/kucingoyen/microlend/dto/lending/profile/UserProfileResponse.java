package com.kucingoyen.microlend.dto.lending.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for user lending profile.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private String level;
    private Integer loansCompleted;
    private Integer loansDefaulted;
    private BigDecimal totalBorrowed;
    private BigDecimal totalLent;
    private BigDecimal collateralRate;
    private String collateralRateDescription;
}
