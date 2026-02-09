package com.kucingoyen.microlend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for transfer operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private String recipientPartyId; // DAML party ID of recipient
    private BigDecimal amount;
    private String currency;
    private String note;
}
