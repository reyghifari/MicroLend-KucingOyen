package com.kucingoyen.microlend.dto.lending.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for configuration update.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConfigResponse {

    private boolean success;
    private String message;
    private String newLendingServiceContractId;
}
