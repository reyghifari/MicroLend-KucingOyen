package com.kucingoyen.dashboard.repository

import com.kucingoyen.entity.model.CreateLoanRequest
import com.kucingoyen.entity.model.CreateLoanResponse
import com.kucingoyen.entity.model.CreateReviewRequest
import com.kucingoyen.entity.model.CreateReviewResponse
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

interface DashboardRepository {

    fun depositToken(currency : String, amount : Int): Flow<DepositResponse>
    fun getBalance(): Flow<GetBalanceResponse>
    fun transferToken(transferRequest: TransferRequest): Flow<TransferResponse>

    fun setTransactionActivity(transaction: Transaction): Flow<Unit>

    fun createLoanRequestAsBorrower(createLoanRequest: CreateLoanRequest): Flow<CreateLoanResponse>
    fun fillLoanRequestAsLender(fillLoanRequest: FillLoanRequest): Flow<FillLoanResponse>
    fun listLoanRequestAsLender(): Flow<ListLendingResponse>

    fun getProfileUser(): Flow<GetDataProfileResponse>

    fun listMyFunded(): Flow<List<MyFundedResponse>>

    fun listMyLoan(): Flow<List<MyLoanResponse>>

    fun createReview(request: CreateReviewRequest): Flow<CreateReviewResponse>
    fun getReviewSummary(partyId: String): Flow<ReviewSummaryResponse>

}