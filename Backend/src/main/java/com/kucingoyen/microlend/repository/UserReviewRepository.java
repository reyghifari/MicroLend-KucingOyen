package com.kucingoyen.microlend.repository;

import com.kucingoyen.microlend.model.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    /** All reviews received by a specific user (by DAML party ID). */
    List<UserReview> findByRevieweePartyIdOrderByCreatedDateDesc(String revieweePartyId);

    /** All reviews submitted by a specific user. */
    List<UserReview> findByReviewerEmailOrderByCreatedDateDesc(String reviewerEmail);

    /** Prevent duplicate reviews for the same loan. */
    boolean existsByReviewerEmailAndLoanContractId(String reviewerEmail, String loanContractId);

    /** For computing average rating of a reviewee. */
    @Query("SELECT AVG(r.rating) FROM UserReview r WHERE r.revieweePartyId = :revieweePartyId")
    Optional<Double> findAverageRatingByRevieweePartyId(String revieweePartyId);

    /** Total review count for a reviewee. */
    long countByRevieweePartyId(String revieweePartyId);
}
