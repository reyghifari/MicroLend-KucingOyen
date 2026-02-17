package com.kucingoyen.entity.model

data class CreateLoanRequest(
    val collateralHoldingContractId: String = "",
    val loanAmount: Double = 0.0,
)