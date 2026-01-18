package com.kucingoyen.auth.screens

import com.kucingoyen.core.base.BaseViewModel
import com.kucingoyen.core.utils.ViewModelUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    viewModelUtils: ViewModelUtils,
) : BaseViewModel(viewModelUtils) {

    private val _showSheetLogin = MutableStateFlow(false)
    val showSheetLogin: StateFlow<Boolean> = _showSheetLogin.asStateFlow()

    private val _showSheetRegister = MutableStateFlow(false)
    val showSheetRegister: StateFlow<Boolean> = _showSheetRegister.asStateFlow()

    fun updateShowSheetRegister(value: Boolean) {
        _showSheetRegister.value = value
    }

    fun updateShowSheetLogin(value: Boolean) {
        _showSheetLogin.value = value
    }
}
