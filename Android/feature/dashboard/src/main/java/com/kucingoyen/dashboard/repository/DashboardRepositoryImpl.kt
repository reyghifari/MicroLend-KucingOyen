package com.kucingoyen.dashboard.repository

import com.kucingoyen.core.utils.DispatcherProvider
import com.kucingoyen.data.service.DashboardService
import com.kucingoyen.entity.model.DepositRequest
import com.kucingoyen.entity.model.DepositResponse
import com.kucingoyen.entity.model.GetBalanceRequest
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.TransferRequest
import com.kucingoyen.entity.model.TransferResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class DashboardRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val dashboardService: DashboardService,
) : DashboardRepository {


    override fun depositToken(partyId: String): Flow<DepositResponse>  =
        flow {
            emit(
                dashboardService.depositToken(
                    DepositRequest(partyId)
                )
            )
        }.flowOn(dispatcher.io)

    override fun getBalance(partyId: String): Flow<GetBalanceResponse> =
        flow {
            emit(
                dashboardService.getBalance(
                    GetBalanceRequest(partyId)
                )
            )
        }.flowOn(dispatcher.io)

    override fun transferToken(partyId: String): Flow<TransferResponse> =
        flow {
            emit(
                dashboardService.transferToken(
                    TransferRequest(partyId)
                )
            )
        }.flowOn(dispatcher.io)

}