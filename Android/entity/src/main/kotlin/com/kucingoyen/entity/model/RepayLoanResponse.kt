package com.kucingoyen.entity.model

data class RepayLoanResponse(
    val success: Boolean,
    val repaidHoldingContractId: String?,
    val returnedCollateralContractId: String?,
    val repaymentAmount: Double?,
    val collateralReturned: Double?,
    val message: String?
)
