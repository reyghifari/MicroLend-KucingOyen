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

    private val _balance = MutableStateFlow<BalanceItem>(BalanceItem())
    val balance: StateFlow<BalanceItem> = _balance.asStateFlow()

    private val _getTotalBalance = MutableStateFlow<Double>(0.0)
    val getTotalBalance: StateFlow<Double> = _getTotalBalance.asStateFlow()

    private val _listTransactionActivity = MutableStateFlow<List<Transaction>>(emptyList())
    val listTransactionActivity: StateFlow<List<Transaction>> = _listTransactionActivity.asStateFlow()

    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction: StateFlow<Transaction?> = _selectedTransaction.asStateFlow()


    init {
        getBalance()
    }

    fun updateBottomBarSelected(value: Int) {
        _bottomBarSelected.value = value
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
                1 -> 0.10
                2 -> 0.09
                3 -> 0.08
                4 -> 0.07
                5 -> 0.05
                else -> 0.10
            }

            val principal = amount.toDoubleOrNull() ?: 0.0

            val total = principal + (principal * interestRate)
            updateTotalCollateral(total.toString())
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
                    _balance.emit(response.balances)

                    _getTotalBalance.emit((response.balances.CC * 0.17) + 100.0)
                    getTransactionActivity()
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


}