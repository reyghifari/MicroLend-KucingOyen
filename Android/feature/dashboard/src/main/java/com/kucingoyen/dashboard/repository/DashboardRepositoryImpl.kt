package com.kucingoyen.dashboard.repository

import com.kucingoyen.core.utils.DispatcherProvider
import com.kucingoyen.data.cache.database.room.TransactionDao
import com.kucingoyen.data.cache.database.room.TransactionEntity
import com.kucingoyen.data.service.DashboardService
import com.kucingoyen.entity.model.CreateLoanRequest
import com.kucingoyen.entity.model.CreateLoanResponse
import com.kucingoyen.entity.model.CreateReviewRequest
import com.kucingoyen.entity.model.CreateReviewResponse
import com.kucingoyen.entity.model.DepositRequest
import com.kucingoyen.entity.model.DepositResponse
import com.kucingoyen.entity.model.FillLoanRequest
import com.kucingoyen.entity.model.FillLoanResponse
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.GetDataProfileResponse
import com.kucingoyen.entity.model.ListLendingResponse
import com.kucingoyen.entity.model.MyFundedResponse
import com.kucingoyen.entity.model.MyLoanResponse
import com.kucingoyen.entity.model.ReviewSummaryResponse
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

    override fun createLoanRequestAsBorrower(createLoanRequest: CreateLoanRequest): Flow<CreateLoanResponse>  =
        flow {
            emit(
                dashboardService.createLoanRequestAsBorrower(
                    createLoanRequest
                )
            )
        }.flowOn(dispatcher.io)

    override fun fillLoanRequestAsLender(fillLoanRequest: FillLoanRequest): Flow<FillLoanResponse>  =
        flow {
            emit(
                dashboardService.fillLoanRequestAsLender(
                    fillLoanRequest
                )
            )
        }.flowOn(dispatcher.io)

    override fun listLoanRequestAsLender(): Flow<ListLendingResponse> =
        flow {
            emit(
                dashboardService.listLoanRequestAsLender()
            )
        }.flowOn(dispatcher.io)

    override fun getProfileUser(): Flow<GetDataProfileResponse> =
        flow {
            emit(
                dashboardService.getProfile()
            )
        }.flowOn(dispatcher.io)

    override fun listMyFunded(): Flow<List<MyFundedResponse>> =
        flow {
            emit(
                dashboardService.listMyFunded()
            )
        }.flowOn(dispatcher.io)

    override fun listMyLoan(): Flow<List<MyLoanResponse>> =
        flow {
            emit(
                dashboardService.listMyLoan()
            )
        }.flowOn(dispatcher.io)

    override fun createReview(request: CreateReviewRequest): Flow<CreateReviewResponse> =
        flow {
            emit(
                dashboardService.createReview(request)
            )
        }.flowOn(dispatcher.io)

    override fun getReviewSummary(partyId: String): Flow<ReviewSummaryResponse> =
        flow {
            emit(
                dashboardService.getReviewSummary(partyId)
            )
        }.flowOn(dispatcher.io)

    override fun repayLoan(request: com.kucingoyen.entity.model.RepayLoanRequest): Flow<com.kucingoyen.entity.model.RepayLoanResponse> =
        flow {
            emit(
                dashboardService.repayLoan(request)
            )
        }.flowOn(dispatcher.io)

}