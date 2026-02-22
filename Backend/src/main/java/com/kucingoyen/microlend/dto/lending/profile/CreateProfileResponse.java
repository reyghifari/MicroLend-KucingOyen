package com.kucingoyen.microlend.dto.lending.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for creating a user profile.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileResponse {

    private boolean success;
    private String contractId;
    private UserProfileResponse profile;
}
