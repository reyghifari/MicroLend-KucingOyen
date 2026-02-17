package com.kucingoyen.entity.model

data class ListLendingResponse(
    val loanRequests: List<LoanRequestItem> = emptyList(),
    val totalCount: Int = 0
)

data class LoanRequestItem(
    val contractId: String = "",
    val borrower: String = "",
    val borrowerDisplayName: String = "",
    val loanAmount: Double = 0.0,
    val collateralAmount: Double = 0.0,
    val loanAsset: String = "",
    val collateralAsset: String = "",
    val interestRate: Double = 0.0,
    val durationDays: Int = 0,
    val borrowerLevel: String = "",
    val requestedAt: String = "",
    val expectedRepayment: Double = 0.0,
    val collateralRatio: Double = 0.0
)
