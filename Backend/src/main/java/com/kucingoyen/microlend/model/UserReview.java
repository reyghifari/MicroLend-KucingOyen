package com.kucingoyen.microlend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Stores a review written by one loan participant about the other.
 * One review is allowed per reviewer per loan contract.
 */
@Entity
@Table(name = "user_reviews", uniqueConstraints = @UniqueConstraint(name = "uk_review_per_loan", columnNames = {
        "reviewer_email", "loan_contract_id" }))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 8732948120938471209L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email of the user writing this review */
    @Column(name = "reviewer_email", nullable = false, length = 255)
    private String reviewerEmail;

    /** DAML party ID of the user being reviewed */
    @Column(name = "reviewee_party_id", nullable = false, length = 255)
    private String revieweePartyId;

    /** Role the reviewer played in this loan */
    @Enumerated(EnumType.STRING)
    @Column(name = "reviewer_role", nullable = false, length = 20)
    private ReviewerRole reviewerRole;

    /** The ActiveLoan contract ID this review is linked to */
    @Column(name = "loan_contract_id", nullable = false, length = 500)
    private String loanContractId;

    /** Star rating 1–5 */
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /** Optional text comment */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    public enum ReviewerRole {
        LENDER, BORROWER
    }
}
