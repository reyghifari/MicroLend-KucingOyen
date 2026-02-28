package com.kucingoyen.dashboard

import androidx.lifecycle.viewModelScope
import com.kucingoyen.core.base.BaseViewModel
import com.kucingoyen.core.utils.ViewModelUtils
import com.kucingoyen.core.utils.loading.LoadingAction
import com.kucingoyen.dashboard.repository.DashboardRepository
import com.kucingoyen.data.cache.UserInfoCache
import com.kucingoyen.data.cache.database.room.TransactionDao
import com.kucingoyen.data.cache.database.room.TransactionEntity
import com.kucingoyen.entity.model.BalanceItem
import com.kucingoyen.entity.model.CreateLoanRequest
import com.kucingoyen.entity.model.CreateReviewRequest
import com.kucingoyen.entity.model.CreateReviewResponse
import com.kucingoyen.entity.model.FillLoanRequest
import com.kucingoyen.entity.model.GetBalanceResponse
import com.kucingoyen.entity.model.HoldingItem
import com.kucingoyen.entity.model.LoanRequestItem
import com.kucingoyen.entity.model.MyFundedResponse
import com.kucingoyen.entity.model.MyLoanResponse
import com.kucingoyen.entity.model.ReviewSummaryResponse
import com.kucingoyen.entity.model.Transaction
import com.kucingoyen.entity.model.TransactionType
import com.kucingoyen.entity.model.TransferRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    viewModelUtils: ViewModelUtils,
    val userInfoSession: UserInfoCache,
    val dashboardRepository: DashboardRepository,
    val transactionDao: TransactionDao
) : BaseViewModel(viewModelUtils) {

    private val _bottomBarSelected = MutableStateFlow(0)
    val bottomBarSelected: StateFlow<Int> = _bottomBarSelected.asStateFlow()

    private val _loanAmount = MutableStateFlow("")
    val loanAmount: StateFlow<String> = _loanAmount.asStateFlow()

    private val _totalCollateral = MutableStateFlow("")
    val totalCollateral: StateFlow<String> = _totalCollateral.asStateFlow()

    private val _showSuccessTransferSheet = MutableStateFlow(false)
    val showSuccessTransferSheet: StateFlow<Boolean> = _showSuccessTransferSheet.asStateFlow()

    private val _showSuccessRequestSheet = MutableStateFlow(false)
    val showSuccessRequestSheet: StateFlow<Boolean> = _showSuccessRequestSheet.asStateFlow()

    private val _balance = MutableStateFlow<GetBalanceResponse>(GetBalanceResponse())
    val balance: StateFlow<GetBalanceResponse> = _balance.asStateFlow()

    private val _getTotalBalance = MutableStateFlow<Double>(0.0)
    val getTotalBalance: StateFlow<Double> = _getTotalBalance.asStateFlow()

    private val _listTransactionActivity = MutableStateFlow<List<Transaction>>(emptyList())
    val listTransactionActivity: StateFlow<List<Transaction>> = _listTransactionActivity.asStateFlow()

    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction: StateFlow<Transaction?> = _selectedTransaction.asStateFlow()

    private val _bottomSheetLevelInfo = MutableStateFlow(false)
    val bottomSheetLevelInfo: StateFlow<Boolean> = _bottomSheetLevelInfo.asStateFlow()

    private val _bottomSheetNotEnoughCollateral = MutableStateFlow(false)
    val bottomSheetNotEnoughCollateral: StateFlow<Boolean> = _bottomSheetNotEnoughCollateral.asStateFlow()

    private val _bottomSheetSuccessRequestLoan = MutableStateFlow(false)
    val bottomSheetSuccessRequestLoan: StateFlow<Boolean> = _bottomSheetSuccessRequestLoan.asStateFlow()

    private val _bottomSheetNotEnoughFund = MutableStateFlow(false)
    val bottomSheetNotEnoughFund: StateFlow<Boolean> = _bottomSheetNotEnoughFund.asStateFlow()

    private val _bottomSheetSuccessFundLoan = MutableStateFlow(false)
    val bottomSheetSuccessFundLoan: StateFlow<Boolean> = _bottomSheetSuccessFundLoan.asStateFlow()

    private val _bottomSheetSuccessRepayLoan = MutableStateFlow(false)
    val bottomSheetSuccessRepayLoan: StateFlow<Boolean> = _bottomSheetSuccessRepayLoan.asStateFlow()

    private val _bottomSheetNotEnoughRepaymentFund = MutableStateFlow(false)
    val bottomSheetNotEnoughRepaymentFund: StateFlow<Boolean> = _bottomSheetNotEnoughRepaymentFund.asStateFlow()

    private val _listLoanRequest = MutableStateFlow<List<LoanRequestItem>>(emptyList())
    val listLoanRequest: StateFlow<List<LoanRequestItem>> = _listLoanRequest.asStateFlow()

    private val _selectedLoanRequest = MutableStateFlow<LoanRequestItem>(LoanRequestItem())
    val selectedLoanRequest: StateFlow<LoanRequestItem> = _selectedLoanRequest.asStateFlow()

    private val _listMyFunded = MutableStateFlow<List<MyFundedResponse>>(emptyList())
    val listMyFunded: StateFlow<List<MyFundedResponse>> = _listMyFunded.asStateFlow()

    private val _listMyLoan = MutableStateFlow<List<MyLoanResponse>>(emptyList())
    val listMyLoan: StateFlow<List<MyLoanResponse>> = _listMyLoan.asStateFlow()

    private val _selectedFundedItem = MutableStateFlow(MyFundedResponse())
    val selectedFundedItem: StateFlow<MyFundedResponse> = _selectedFundedItem.asStateFlow()

    private val _selectedLoanItem = MutableStateFlow(MyLoanResponse())
    val selectedLoanItem: StateFlow<MyLoanResponse> = _selectedLoanItem.asStateFlow()

    private val _reviewSummary = MutableStateFlow(ReviewSummaryResponse())
    val reviewSummary: StateFlow<ReviewSummaryResponse> = _reviewSummary.asStateFlow()

    init {
        getBalance()
        getProfile()
    }

    fun updateBottomBarLevelInfo(value: Boolean) {
        _bottomSheetLevelInfo.value = value
    }
    fun updateBottomBarSelected(value: Int) {
        _bottomBarSelected.value = value
    }
    fun updateBottomBarNotEnoughCollateral(value: Boolean) {
        _bottomSheetNotEnoughCollateral.value = value
    }

    fun updateBottomBarNotEnoughFund(value: Boolean) {
        _bottomSheetNotEnoughFund.value = value
    }

    fun updateBottomSuccessRequestLoan(value: Boolean) {
        _bottomSheetSuccessRequestLoan.value = value
    }

    fun updateBottomSuccessFundLoan(value: Boolean) {
        _bottomSheetSuccessFundLoan.value = value
    }

    fun updateBottomSuccessRepayLoan(value: Boolean) {
        _bottomSheetSuccessRepayLoan.value = value
    }

    fun updateBottomBarNotEnoughRepaymentFund(value: Boolean) {
        _bottomSheetNotEnoughRepaymentFund.value = value
    }



    fun getLevelUser(): Int {
        return userInfoSession.level
    }

    fun getEmailUser(): String {
        return userInfoSession.email
    }

    fun getPartyId(): String {
        return userInfoSession.partyId
    }

    fun updateLoanAmount(amount: String) {
        _loanAmount.value = amount

        if (amount.isNotEmpty()) {
            val interestRate = when (getLevelUser()) {
                1 -> 0.15
                2 -> 0.14
                3 -> 0.13
                4 -> 0.12
                5 -> 0.11
                else -> 0.15
            }

            val principal = amount
                .replace(",", ".")
                .toDoubleOrNull() ?: 0.0

            val totalCC = principal + (principal * interestRate)
            val totalUSDxRate = totalCC * 0.16
            updateTotalCollateral("%.2f".format(totalUSDxRate))
        } else {
            updateTotalCollateral("")
        }
    }

    fun updateTotalCollateral(amount: String) {
        _totalCollateral.value = amount
    }

    fun requestBalance(asset : String, amount : Int) {
        viewModelScope.launch(exceptionHandler) {
            dashboardRepository.depositToken(
                currency = asset,
                amount = amount
            )
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    updateShowSuccessRequestSheet(true)
                    setTransaction(
                        Transaction(
                            type = TransactionType.RECEIVED,
                            tokenSymbol = asset,
                            tokenAmount = amount.toString(),
                            address = ""
                        )
                    )
                    getBalance()
                    getProfile()
                }
        }
    }

    fun getBalance() {
        viewModelScope.launch(exceptionHandler) {
            dashboardRepository.getBalance()
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    _balance.emit(response)
                    _getTotalBalance.emit((response.balances.CC * 0.17) + response.balances.USDx)
                    getTransactionActivity()
                }
        }
    }

    fun getProfile() {
        viewModelScope.launch(exceptionHandler) {
            dashboardRepository.getProfileUser()
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->

                }
        }
    }

    fun updateShowSuccessTransferSheet(boolean: Boolean) {
        _showSuccessTransferSheet.tryEmit(boolean)
    }

    fun updateShowSuccessRequestSheet(boolean: Boolean) {
        _showSuccessRequestSheet.tryEmit(boolean)
    }

    fun selectTransaction(transaction: Transaction) {
        _selectedTransaction.value = transaction
    }

    fun transfer(amount: String, recipientAddress: String) {
        viewModelScope.launch(exceptionHandler) {
            dashboardRepository.transferToken(
                TransferRequest(
                    recipientPartyId = recipientAddress,
                    amount = amount.toDouble(),
                    currency = "CC",
                    note = ""
                )
            )
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    updateShowSuccessTransferSheet(true)
                    setTransaction(
                        Transaction(
                            type = TransactionType.SENT,
                            tokenSymbol = "CC",
                            tokenAmount = amount,
                            address = recipientAddress
                        )
                    )
                    getBalance()
                }
        }
    }

    private fun setTransaction(transaction: Transaction) {
        viewModelScope.launch(exceptionHandler) {
            dashboardRepository.setTransactionActivity(transaction)
                .onStart {}
                .onCompletion {}
                .collect {}
        }
    }

    private fun getTransactionActivity(){
        viewModelScope.launch {
            val data = transactionDao.getAll().map {
                TransactionEntity.parseEntity(it)
            }
            _listTransactionActivity.emit(data)
        }
    }


    fun createLoanRequestAsBorrower(amount : String){
        val doubleAmount = amount
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0
        val holdingItem = validateLoanRequest(doubleAmount)
        if (holdingItem.contractId.isNotEmpty()){
            viewModelScope.launch {
                dashboardRepository.createLoanRequestAsBorrower(
                    CreateLoanRequest(loanAmount = doubleAmount, collateralHoldingContractId = holdingItem.contractId)
                )
                    .onStart {
                        LoadingAction.show(true)
                    }
                    .onCompletion {
                        LoadingAction.show(false)
                    }
                    .collect { response ->
                        if (response.success){
                            updateBottomSuccessRequestLoan(true)
                            setTransaction(
                                Transaction(
                                    type = TransactionType.BORROWED,
                                    tokenSymbol = "CC",
                                    tokenAmount = amount,
                                    address = "Loan Request"
                                )
                            )
                            getBalance()
                            getProfile()
                        }else{
                            updateBottomBarNotEnoughCollateral(true)
                        }
                    }
            }
        }else{
            updateBottomBarNotEnoughCollateral(true)
        }
    }

    private fun validateLoanRequest(amount: Double) : HoldingItem {
        val interestRate = when (getLevelUser()) {
            1 -> 0.15
            2 -> 0.14
            3 -> 0.13
            4 -> 0.12
            5 -> 0.11
            else -> 0.15
        }

        val total = (amount + (amount * interestRate)) * 0.16
        val USDx = _balance.value.holdings.USDx.filter { it.amount > total }
        return USDx.first()
    }

    fun listLoanRequestAsLender(){
        viewModelScope.launch {
            dashboardRepository.listLoanRequestAsLender()
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    _listLoanRequest.emit(response.loanRequests)
                }
        }
    }

    fun fillLoanRequestAsLender(loanContractId : String, loanAmount : Double){
        val getLoanContractId = getHoldingLoanContractId(loanAmount)

        if (getLoanContractId.isNotEmpty()){
            viewModelScope.launch {
                dashboardRepository.fillLoanRequestAsLender(
                    FillLoanRequest(
                        contractId = loanContractId,
                        loanHoldingContractId = getLoanContractId
                    ))
                    .onStart {
                        LoadingAction.show(true)
                    }
                    .onCompletion {
                        LoadingAction.show(false)
                    }
                    .collect { response ->
                        if (response.success){
                            updateBottomSuccessFundLoan(true)
                            setTransaction(
                                Transaction(
                                    type = TransactionType.FUNDED,
                                    tokenSymbol = "CC",
                                    tokenAmount = loanAmount.toString(),
                                    address = loanContractId
                                )
                            )
                            getBalance()
                            getProfile()
                        }else{
                            updateBottomBarNotEnoughFund(true)
                        }
                    }
            }
        }else{
            updateBottomBarNotEnoughFund(true)
        }

    }

    fun myListFunded(){
        viewModelScope.launch {
            dashboardRepository.listMyFunded()
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    _listMyFunded.emit(response)
                }
        }
    }

    fun myListLoan(){
        viewModelScope.launch {
            dashboardRepository.listMyLoan()
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    _listMyLoan.emit(response)
                }
        }
    }

    private fun getHoldingLoanContractId(loanAmount: Double) : String{
        val getContractId = _balance.value.holdings.CC.filter { it.amount >= loanAmount }
        return getContractId.firstOrNull()?.contractId ?: ""
    }

    fun repayLoan(loanContractId: String, requiredRepayment: Double) {
        val repaymentHoldingId = getHoldingLoanContractId(requiredRepayment)

        if (repaymentHoldingId.isNotEmpty()) {
            viewModelScope.launch {
                dashboardRepository.repayLoan(
                    request = com.kucingoyen.entity.model.RepayLoanRequest(
                        contractId = loanContractId,
                        repaymentHoldingContractId = repaymentHoldingId
                    )
                )
                    .onStart {
                        LoadingAction.show(true)
                    }
                    .onCompletion {
                        LoadingAction.show(false)
                    }
                    .collect { response ->
                        if (response.success) {
                            updateBottomSuccessRepayLoan(true)
                            setTransaction(
                                Transaction(
                                    type = TransactionType.REPAID,
                                    tokenSymbol = "CC",
                                    tokenAmount = requiredRepayment.toString(),
                                    address = loanContractId
                                )
                            )
                            getBalance()
                            getProfile()
                            myListLoan()
                        } else {
                            updateBottomBarNotEnoughRepaymentFund(true)
                        }
                    }
            }
        } else {
            updateBottomBarNotEnoughRepaymentFund(true)
        }
    }

    fun setDetailLoanRequest(loanRequestItem: LoanRequestItem){
        _selectedLoanRequest.value = loanRequestItem
    }

    fun selectFundedItem(item: MyFundedResponse) {
        _selectedFundedItem.value = item
    }

    fun selectLoanItem(item: MyLoanResponse) {
        _selectedLoanItem.value = item
    }

    fun createReview(request: CreateReviewRequest) {
        viewModelScope.launch(exceptionHandler) {
            dashboardRepository.createReview(request)
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->

                }
        }
    }

    fun getReviewSummary(partyId: String) {
        // TODO: Replace dummy data with actual API call
        viewModelScope.launch {
            val dummyReviews = listOf(
                CreateReviewResponse(
                    id = 1,
                    reviewerEmail = "user_abc@gmail.com",
                    revieweePartyId = partyId,
                    reviewerRole = "LENDER",
                    loanContractId = "contract123",
                    rating = 5,
                    comment = "Very smooth repayment",
                    createdDate = "2026-02-20T10:30:00Z"
                ),
            )
            val avgRating = dummyReviews.map { it.rating }.average()
            _reviewSummary.emit(
                ReviewSummaryResponse(
                    revieweePartyId = partyId,
                    averageRating = avgRating,
                    totalReviews = dummyReviews.size,
                    reviews = dummyReviews
                )
            )
        }
    }
}