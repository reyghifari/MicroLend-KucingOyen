package com.kucingoyen.microlend.dto.lending.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for updating lending configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConfigRequest {

    private BigDecimal newBaseRate;
    private BigDecimal newInterestRate;
    private Integer newDurationDays;
}
