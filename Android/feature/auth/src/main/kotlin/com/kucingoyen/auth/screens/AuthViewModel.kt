package com.kucingoyen.auth.screens

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.kucingoyen.auth.GoogleAuthClient
import com.kucingoyen.core.base.BaseViewModel
import com.kucingoyen.core.utils.ViewModelUtils
import com.kucingoyen.data.di.TagInjection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    viewModelUtils: ViewModelUtils,
    @Named(TagInjection.WEB_CLIENT_ID) private val webClientId: String,
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

    fun loginWithGoogle(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val idToken = GoogleAuthClient(context).signInAndGetToken(webClientId)

            if (idToken != null) {
                onSuccess()
            } else {

            }
        }
    }
}
