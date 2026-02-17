package com.kucingoyen.entity.model

data class GetDataProfileResponse(
    val success: Boolean = false,
    val contractId: String = "",
    val profile: Profile = Profile()
)

data class Profile(
    val level: String = "",
    val loansCompleted: Int = 0,
    val loansDefaulted: Int = 0,
    val totalBorrowed: Int = 0,
    val totalLent: Int = 0,
    val collateralRate: Double = 0.0,
    val collateralRateDescription: String = "",
)