package com.kucingoyen.entity.model

data class RegisterResponse(
    val email: String = "",
    val damlPartyId: String = "",
    val fullName: String = "",
    val level: Int = 1,
    val token: String = "",
)

data class RegisterRequest(
    val token: String = ""
)

