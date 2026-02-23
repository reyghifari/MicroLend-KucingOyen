package com.kucingoyen.microlend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for a single review.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private String reviewerEmail;
    private String revieweePartyId;
    private String reviewerRole; // LENDER or BORROWER
    private String loanContractId;
    private Integer rating;
    private String comment;
    private Instant createdDate;
}
