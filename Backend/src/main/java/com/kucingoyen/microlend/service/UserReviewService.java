package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.dto.review.ReviewResponse;
import com.kucingoyen.microlend.dto.review.SubmitReviewRequest;
import com.kucingoyen.microlend.dto.review.UserReviewSummaryResponse;
import com.kucingoyen.microlend.exception.NotFoundException;
import com.kucingoyen.microlend.model.UserReview;
import com.kucingoyen.microlend.model.UserReview.ReviewerRole;
import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import com.kucingoyen.microlend.repository.UserReviewRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for submitting and retrieving user reviews between lenders and
 * borrowers.
 */
@Service
@RequiredArgsConstructor
public class UserReviewService {

    private static final Logger log = LoggerFactory.getLogger(UserReviewService.class);

    private final UserReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DamlLedgerService damlService;
    private final AdminSetupService adminSetupService;

    /**
     * Submit a review. The reviewer must have participated in the loan
     * (as lender or borrower). Only one review allowed per loan per reviewer.
     */
    public ReviewResponse submitReview(String reviewerEmail, SubmitReviewRequest request) {
        log.info("User {} submitting review for loan {}", reviewerEmail, request.getLoanContractId());

        // Prevent duplicate reviews
        if (reviewRepository.existsByReviewerEmailAndLoanContractId(
                reviewerEmail, request.getLoanContractId())) {
            throw new IllegalStateException(
                    "You have already submitted a review for this loan.");
        }

        Users reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new NotFoundException("Reviewer not found"));

        // Determine reviewer's role by inspecting the ActiveLoan DAML contract
        ReviewerRole role = resolveReviewerRole(reviewer.getDamlPartyId(), request.getLoanContractId());

        // Validate that reviewee is the other party in the loan
        validateRevieweeIsCounterparty(
                reviewer.getDamlPartyId(), request.getRevieweePartyId(),
                request.getLoanContractId(), role);

        UserReview review = UserReview.builder()
                .reviewerEmail(reviewerEmail)
                .revieweePartyId(request.getRevieweePartyId())
                .reviewerRole(role)
                .loanContractId(request.getLoanContractId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        UserReview saved = reviewRepository.save(review);
        log.info("Review {} saved successfully", saved.getId());

        return toResponse(saved);
    }

    /**
     * Get all reviews received by a specific user (identified by DAML party ID).
     */
    public List<ReviewResponse> getReviewsForUser(String revieweePartyId) {
        return reviewRepository
                .findByRevieweePartyIdOrderByCreatedDateDesc(revieweePartyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all reviews the current user has written.
     */
    public List<ReviewResponse> getMyReviews(String reviewerEmail) {
        return reviewRepository
                .findByReviewerEmailOrderByCreatedDateDesc(reviewerEmail)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a rating summary (average + total + list) for a user.
     */
    public UserReviewSummaryResponse getUserRatingSummary(String revieweePartyId) {
        List<ReviewResponse> reviews = getReviewsForUser(revieweePartyId);
        double average = reviewRepository
                .findAverageRatingByRevieweePartyId(revieweePartyId)
                .orElse(0.0);
        long total = reviewRepository.countByRevieweePartyId(revieweePartyId);

        // Round to 1 decimal place
        double rounded = Math.round(average * 10.0) / 10.0;

        return new UserReviewSummaryResponse(revieweePartyId, rounded, total, reviews);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    /**
     * Query the ActiveLoan contract and determine whether the reviewer is
     * the lender or the borrower.
     */
    private ReviewerRole resolveReviewerRole(String reviewerPartyId, String loanContractId) {
        String operatorPartyId = adminSetupService.getAdminPartyId();

        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_ACTIVE_LOAN,
                Map.of(),
                operatorPartyId);

        ContractResult loan = results.stream()
                .filter(c -> c.contractId().equals(loanContractId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Loan contract not found: " + loanContractId));

        Map<String, Object> payload = loan.payload();
        String lender = (String) payload.get("lender");
        String borrower = (String) payload.get("borrower");

        if (reviewerPartyId.equals(lender)) {
            return ReviewerRole.LENDER;
        } else if (reviewerPartyId.equals(borrower)) {
            return ReviewerRole.BORROWER;
        } else {
            throw new IllegalStateException(
                    "You did not participate in loan " + loanContractId);
        }
    }

    /**
     * Ensure the reviewee is the counterparty of the reviewer in the loan.
     */
    private void validateRevieweeIsCounterparty(
            String reviewerPartyId, String revieweePartyId,
            String loanContractId, ReviewerRole reviewerRole) {

        if (reviewerPartyId.equals(revieweePartyId)) {
            throw new IllegalArgumentException("You cannot review yourself.");
        }

        String operatorPartyId = adminSetupService.getAdminPartyId();

        List<ContractResult> results = damlService.queryContracts(
                LendingConstants.TEMPLATE_ACTIVE_LOAN,
                Map.of(),
                operatorPartyId);

        ContractResult loan = results.stream()
                .filter(c -> c.contractId().equals(loanContractId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Loan contract not found: " + loanContractId));

        Map<String, Object> payload = loan.payload();
        String expectedReviewee = reviewerRole == ReviewerRole.LENDER
                ? (String) payload.get("borrower")
                : (String) payload.get("lender");

        if (!expectedReviewee.equals(revieweePartyId)) {
            throw new IllegalArgumentException(
                    "revieweePartyId does not match the counterparty for this loan.");
        }
    }

    private ReviewResponse toResponse(UserReview review) {
        return new ReviewResponse(
                review.getId(),
                review.getReviewerEmail(),
                review.getRevieweePartyId(),
                review.getReviewerRole().name(),
                review.getLoanContractId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedDate());
    }
}
