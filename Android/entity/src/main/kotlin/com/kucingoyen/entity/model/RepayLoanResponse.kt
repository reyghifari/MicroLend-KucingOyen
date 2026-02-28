package com.kucingoyen.entity.model

data class RepayLoanResponse(
    val success: Boolean,
    val message: String?,
    val repaymentAmount: Double?
)
