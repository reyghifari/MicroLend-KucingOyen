package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for deposit operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse {

    private String contractId;
    private BigDecimal amount;
    private String currency;
    private String status;
}
