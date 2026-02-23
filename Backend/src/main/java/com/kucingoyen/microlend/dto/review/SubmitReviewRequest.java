package com.kucingoyen.microlend.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to submit a review for another loan participant.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitReviewRequest {

    /** DAML party ID of the user being reviewed */
    @NotBlank(message = "revieweePartyId is required")
    private String revieweePartyId;

    /** The ActiveLoan contract ID this review is linked to */
    @NotBlank(message = "loanContractId is required")
    private String loanContractId;

    /** Star rating from 1 (lowest) to 5 (highest) */
    @NotNull(message = "rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    /** Optional text comment */
    private String comment;
}
