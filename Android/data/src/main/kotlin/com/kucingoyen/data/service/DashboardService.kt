package com.kucingoyen.data.service

import com.kucingoyen.data.utils.Endpoint
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
import com.kucingoyen.entity.model.TransferRequest
import com.kucingoyen.entity.model.TransferResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST(Endpoint.CREATE_LOAN_REQUEST)
    suspend fun createLoanRequestAsBorrower(
        @Body request: CreateLoanRequest
    ): CreateLoanResponse

    @GET(Endpoint.LIST_LENDING)
    suspend fun listLoanRequestAsLender(
    ): ListLendingResponse

    @POST(Endpoint.LENDER_FILL_LOAN)
    suspend fun fillLoanRequestAsLender(
        @Body request: FillLoanRequest
    ): FillLoanResponse

    @POST(Endpoint.CREATE_PROFILE)
    suspend fun createProfile(
    ): GetDataProfileResponse

    @GET(Endpoint.CREATE_PROFILE)
    suspend fun getProfile(
    ): GetDataProfileResponse

    @GET(Endpoint.MY_FUNDED)
    suspend fun listMyFunded(
    ): List<MyFundedResponse>

    @GET(Endpoint.MY_LOAN)
    suspend fun listMyLoan(
    ): List<MyLoanResponse>

    @POST(Endpoint.REVIEWS)
    suspend fun createReview(
        @Body request: CreateReviewRequest
    ): CreateReviewResponse

    @GET(Endpoint.REVIEW_SUMMARY)
    suspend fun getReviewSummary(
        @Path("partyId") partyId: String
    ): ReviewSummaryResponse

}
