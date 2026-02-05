package com.kucingoyen.dashboard.repository

import com.kucingoyen.core.utils.DispatcherProvider
import com.kucingoyen.data.cache.database.room.TransactionDao
import com.kucingoyen.data.cache.database.room.TransactionEntity
import com.kucingoyen.data.service.DashboardService
import com.kucingoyen.entity.model.DepositRequest
import com.kucingoyen.entity.model.DepositResponse
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.Transaction
import com.kucingoyen.entity.model.TransferRequest
import com.kucingoyen.entity.model.TransferResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class DashboardRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val dashboardService: DashboardService,
    private val transactionCache: TransactionDao,
) : DashboardRepository {


    override fun depositToken(currency : String, amount : Int): Flow<DepositResponse>  =
        flow {
            emit(
                dashboardService.depositToken(
                    DepositRequest(currency, amount)
                )
            )
        }.flowOn(dispatcher.io)

    override fun getBalance(): Flow<GetBalanceResponse> =
        flow {
            emit(
                dashboardService.getBalance()
            )
        }.flowOn(dispatcher.io)

    override fun transferToken(transferRequest: TransferRequest): Flow<TransferResponse> =
        flow {
            emit(
                dashboardService.transferToken(
                    transferRequest
                )
            )
        }.flowOn(dispatcher.io)

    override fun setTransactionActivity(transaction: Transaction): Flow<Unit> =
        flow {
            transactionCache.insert(
                TransactionEntity.parseEntity(transaction)
            )
            emit(Unit)
        }.flowOn(dispatcher.io)


}