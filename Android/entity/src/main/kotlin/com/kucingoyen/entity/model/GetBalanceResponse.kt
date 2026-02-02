package com.kucingoyen.entity.model

data class GetBalanceResponse(
    val balances: BalanceItem
)

data class BalanceItem(
    val CC: Double
)