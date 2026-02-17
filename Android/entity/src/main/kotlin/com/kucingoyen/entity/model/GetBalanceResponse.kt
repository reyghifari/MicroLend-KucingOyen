package com.kucingoyen.entity.model

data class GetBalanceResponse(
    val balances: BalanceItem = BalanceItem(),
    val holdings: Holdings = Holdings()
)

data class BalanceItem(
    val CC: Double = 0.0,
    val USDx: Double = 0.0,
)

data class Holdings(
    val CC: List<HoldingItem> = emptyList(),
    val USDx: List<HoldingItem> = emptyList()
)

data class HoldingItem(
    val contractId: String = "",
    val amount: Double = 0.0,
    val assetSymbol: String = "",
    val assetDescription: String = ""
)
