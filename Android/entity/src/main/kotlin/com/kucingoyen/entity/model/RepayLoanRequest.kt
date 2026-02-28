package com.kucingoyen.entity.model

data class RepayLoanRequest(
    val contractId: String,
    val repaymentHoldingContractId: String
)
