package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.review.ReviewResponse;
import com.kucingoyen.microlend.dto.review.SubmitReviewRequest;
import com.kucingoyen.microlend.dto.review.UserReviewSummaryResponse;
import com.kucingoyen.microlend.service.UserReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user review operations.
 * Lenders and borrowers can review each other after a loan.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class UserReviewController {

    private final UserReviewService reviewService;

    /**
     * Submit a review for the counterparty of a loan.
     * The reviewer must be either the lender or borrower of the specified loan.
     *
     * POST /api/reviews
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> submitReview(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody SubmitReviewRequest request) {

        ReviewResponse response = reviewService.submitReview(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all reviews received by a user (identified by DAML party ID).
     *
     * GET /api/reviews/user/{partyId}
     */
    @GetMapping("/user/{partyId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsForUser(
            @PathVariable String partyId) {

        return ResponseEntity.ok(reviewService.getReviewsForUser(partyId));
    }

    /**
     * Get all reviews the authenticated user has submitted.
     *
     * GET /api/reviews/my-reviews
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(reviewService.getMyReviews(user.getUsername()));
    }

    /**
     * Get the rating summary (average, total, all reviews) for a user.
     *
     * GET /api/reviews/summary/{partyId}
     */
    @GetMapping("/summary/{partyId}")
    public ResponseEntity<UserReviewSummaryResponse> getUserRatingSummary(
            @PathVariable String partyId) {

        return ResponseEntity.ok(reviewService.getUserRatingSummary(partyId));
    }
}
