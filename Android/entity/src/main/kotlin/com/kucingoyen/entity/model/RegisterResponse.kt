package com.kucingoyen.entity.model

data class RegisterResponse(
    val email: String = "",
    val partyId: String = "",
    val level: Int = 0,
    val jwt: String = "",
)

data class RegisterRequest(
    val token: String = ""
)

