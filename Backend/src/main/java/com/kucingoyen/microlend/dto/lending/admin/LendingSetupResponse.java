package com.kucingoyen.microlend.dto.lending.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for lending system setup.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingSetupResponse {

    private boolean success;

    /**
     * Contract ID of the created LendingService contract
     */
    private String lendingServiceContractId;

    /**
     * Contract ID of the created TimeOracle contract
     */
    private String timeOracleContractId;

    /**
     * Contract ID of the created UserProfileFactory contract
     */
    private String userProfileFactoryContractId;
}
