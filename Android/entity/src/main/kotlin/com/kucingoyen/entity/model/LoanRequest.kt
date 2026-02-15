package com.kucingoyen.entity.model

data class LoanRequest(
    val userName: String = "",
    val level: Int = 1,
    val loanAmount: String = "",
    val loanCurrency: String = "CC",
    val collateralAmount: String = "",
    val collateralCurrency: String = "USDCx",
    val requestedDate: String = "",
    val status: String = "REQUESTED"
){
    companion object {

        fun get() : LoanRequest =
            LoanRequest(
                userName = "John Doe",
                level = 1,
                loanAmount = "1000",
                loanCurrency = "CC",
                collateralAmount = "500",
                collateralCurrency = "USDCx",
                requestedDate = "2023-09-15"
            )
    }
}