package com.kucingoyen.data.di


import com.kucingoyen.data.cache.database.AppDataBase
import com.kucingoyen.data.cache.database.room.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DaoModule {

    @Singleton
    @Provides
    fun provideTransactionDao(
        database: AppDataBase
    ): TransactionDao = database.transactionCache

}