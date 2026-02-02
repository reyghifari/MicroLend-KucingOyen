package com.kucingoyen.entity.model

data class TransferResponse(
    val isSuccess: Boolean = false,
    val message: String = "",
)

data class TransferRequest(
    val recipientPartyId: String = "",
    val amount: Double = 0.0,
    val currency: String = "",
    val note: String = "",
)

