package com.kucingoyen.entity.model

data class GetBalanceResponse(
    val balances: BalanceItem = BalanceItem()
)

data class BalanceItem(
    val CC: Double = 0.0
)