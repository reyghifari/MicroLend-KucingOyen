package com.kucingoyen.dashboard.repository

import com.kucingoyen.entity.model.DepositResponse
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.TransferResponse
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun depositToken(partyId: String): Flow<DepositResponse>
    fun getBalance(partyId: String): Flow<GetBalanceResponse>
    fun transferToken(partyId: String): Flow<TransferResponse>


}