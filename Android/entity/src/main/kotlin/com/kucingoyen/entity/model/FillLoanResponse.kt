package com.kucingoyen.entity.model

data class FillLoanResponse(
    val success : Boolean =  false,
    val activeLoanContractId : String =  "",
    val remainderHoldingCid : String =  "",
    val message : String =  "",
)