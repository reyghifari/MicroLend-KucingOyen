package com.kucingoyen.entity.model

data class DepositResponse(
    val isSuccess: Boolean = false,
    val message: String = "",
)

data class DepositRequest(
    val damlPartyId: String = ""
)

