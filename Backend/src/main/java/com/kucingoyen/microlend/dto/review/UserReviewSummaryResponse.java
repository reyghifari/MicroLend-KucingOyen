package com.kucingoyen.microlend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Summary of all reviews received by a user, including average rating.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewSummaryResponse {

    private String revieweePartyId;
    private double averageRating;
    private long totalReviews;
    private List<ReviewResponse> reviews;
}
