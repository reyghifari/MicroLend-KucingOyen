package com.kucingoyen.dashboard.repository

import com.kucingoyen.entity.model.DepositResponse
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.Transaction
import com.kucingoyen.entity.model.TransferRequest
import com.kucingoyen.entity.model.TransferResponse
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun depositToken(currency : String, amount : Int): Flow<DepositResponse>
    fun getBalance(): Flow<GetBalanceResponse>
    fun transferToken(transferRequest: TransferRequest): Flow<TransferResponse>

    fun setTransactionActivity(transaction: Transaction): Flow<Unit>

}