package com.kucingoyen.entity.model

data class DepositResponse(
    val amount: Int = 0,
    val currency: String = "",
    val status: String = "",
)

data class DepositRequest(
    val currency: String = "",
    val amount: Int = 0
)

