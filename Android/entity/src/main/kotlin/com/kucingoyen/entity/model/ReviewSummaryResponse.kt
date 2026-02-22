package com.kucingoyen.entity.model

data class ReviewSummaryResponse(
    val revieweePartyId: String = "",
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val reviews: List<CreateReviewResponse> = emptyList()
)
