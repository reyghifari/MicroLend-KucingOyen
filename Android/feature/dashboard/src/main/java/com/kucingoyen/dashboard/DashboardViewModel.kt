package com.kucingoyen.dashboard

import com.kucingoyen.core.base.BaseViewModel
import com.kucingoyen.core.utils.ViewModelUtils
import com.kucingoyen.data.cache.UserInfoCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    viewModelUtils: ViewModelUtils,
    val userInfoSession: UserInfoCache,
) : BaseViewModel(viewModelUtils) {

    private val _bottomBarSelected = MutableStateFlow(0)
    val bottomBarSelected: StateFlow<Int> = _bottomBarSelected.asStateFlow()

    private val _loanAmount = MutableStateFlow("")
    val loanAmount: StateFlow<String> = _loanAmount.asStateFlow()

    private val _totalCollateral = MutableStateFlow("")
    val totalCollateral: StateFlow<String> = _totalCollateral.asStateFlow()

    fun updateBottomBarSelected(value: Int) {
        _bottomBarSelected.value = value
    }

    fun getLevelUser() : Int{
        return userInfoSession.level
    }

    fun getEmailUser() : String{
        return userInfoSession.email
    }

    fun getPartyId() : String{
        return userInfoSession.partyId
    }

    fun updateLoanAmount(amount : String){
        _loanAmount.value = amount

        if (amount.isNotEmpty()){
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
        }else{
            updateTotalCollateral("")
        }
    }

    fun updateTotalCollateral(amount : String){
        _totalCollateral.value = amount
    }

}