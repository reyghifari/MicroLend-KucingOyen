package com.kucingoyen.data.service

import com.kucingoyen.data.utils.Endpoint
import com.kucingoyen.entity.model.DepositRequest
import com.kucingoyen.entity.model.DepositResponse
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.TransferRequest
import com.kucingoyen.entity.model.TransferResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardService {

    @POST(Endpoint.DEPOSIT)
    suspend fun depositToken(
        @Body request: DepositRequest
    ): DepositResponse

    @GET(Endpoint.GET_BALANCE)
    suspend fun getBalance(): GetBalanceResponse

    @POST(Endpoint.TRANSFER)
    suspend fun transferToken(
        @Body request: TransferRequest
    ): TransferResponse

}
