package com.kucingoyen.entity.model

data class CreateReviewResponse(
    val id: Long = 0,
    val reviewerEmail: String = "",
    val revieweePartyId: String = "",
    val reviewerRole: String = "",
    val loanContractId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val createdDate: String = ""
)
