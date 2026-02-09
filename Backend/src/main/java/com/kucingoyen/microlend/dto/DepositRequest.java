package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for deposit operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {

    private BigDecimal amount;
    private String currency;
}
