package com.kucingoyen.entity.model

data class CreateLoanResponse(
    val success: Boolean = false,
    val loanRequestContractId: String = "",
    val remainderHoldingContractId: String? = null, // nullable karena optional
    val loanDetails: LoanDetails = LoanDetails()
)

data class LoanDetails(
    val loanAmount: Double = 0.0,
    val collateralAmount: Double = 0.0,
    val interestRate: Double = 0.0,
    val durationDays: Int = 0,
    val requiredRepayment: Double = 0.0
)
