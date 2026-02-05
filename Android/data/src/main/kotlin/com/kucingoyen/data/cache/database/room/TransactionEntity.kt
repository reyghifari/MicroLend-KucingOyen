package com.kucingoyen.data.cache.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kucingoyen.entity.model.Transaction
import com.kucingoyen.entity.model.TransactionType

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "tokenAmount") val tokenAmount: String,
    @ColumnInfo(name = "tokenSymbol") val tokenSymbol: String,
    @ColumnInfo(name = "address") val address: String,
) {
    companion object {
        fun parseEntity(it: Transaction) = TransactionEntity(
            id = 0,
            type = it.type.name,
            address = it.address,
            tokenAmount = it.tokenAmount,
            tokenSymbol = it.tokenSymbol
        )

        fun parseEntity(it: TransactionEntity) = Transaction(
            type = TransactionType.valueOf(it.type),
            address = it.address,
            tokenAmount = it.tokenAmount,
            tokenSymbol = it.tokenSymbol
        )
    }

}

