package com.kucingoyen.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kucingoyen.data.cache.database.room.TransactionDao
import com.kucingoyen.data.cache.database.room.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDataBase : RoomDatabase() {

    abstract val transactionCache: TransactionDao

    companion object {
        const val NAME = "AppDataBase.db"
    }

}
