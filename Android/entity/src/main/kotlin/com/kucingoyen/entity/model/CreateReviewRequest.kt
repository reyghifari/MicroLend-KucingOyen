package com.kucingoyen.entity.model

data class CreateReviewRequest(
    val revieweePartyId: String = "",
    val loanContractId: String = "",
    val rating: Int = 0,
    val comment: String = ""
)
