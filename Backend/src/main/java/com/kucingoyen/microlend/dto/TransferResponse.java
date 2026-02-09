package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for transfer operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private String status;
    private BigDecimal amount;
    private String transactionId;
}
