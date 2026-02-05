package com.kucingoyen.entity.model

data class Transaction(
    val type: TransactionType,
    val address: String,
    val tokenAmount: String,
    val tokenSymbol: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    SENT,
    RECEIVED
}
