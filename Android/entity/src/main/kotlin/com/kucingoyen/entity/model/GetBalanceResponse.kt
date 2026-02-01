package com.kucingoyen.entity.model

data class GetBalanceResponse(
    val isSuccess: Boolean = false,
    val message: String = "",
)

data class GetBalanceRequest(
    val damlPartyId: String = ""
)

