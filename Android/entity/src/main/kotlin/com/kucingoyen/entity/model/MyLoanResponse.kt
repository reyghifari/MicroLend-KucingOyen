package com.kucingoyen.entity.model

data class MyLoanResponse(
    val contractId: String = "",
    val lender: String = "",
    val borrower: String = "",
    val lenderDisplayName: String = "",
    val borrowerDisplayName: String = "",
    val loanAmount: Double = 0.0,
    val collateralAmount: Double = 0.0,
    val interestRate: Double = 0.0,
    val requiredRepayment: Double = 0.0,
    val expectedReturn: Double = 0.0,
    val startTime: String = "",
    val endTime: String = "",
    val status: String = "",
    val daysRemaining: Int = 0,
    val isOverdue: Boolean = false,
    val canLiquidate: Boolean = false
)
