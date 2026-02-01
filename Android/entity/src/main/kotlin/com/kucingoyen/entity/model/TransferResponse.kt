package com.kucingoyen.entity.model

data class TransferResponse(
    val isSuccess: Boolean = false,
    val message: String = "",
)

data class TransferRequest(
    val damlPartyId: String = ""
)

