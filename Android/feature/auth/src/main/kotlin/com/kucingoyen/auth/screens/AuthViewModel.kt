package com.kucingoyen.auth.screens

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.kucingoyen.auth.GoogleAuthClient
import com.kucingoyen.auth.repository.AuthRepository
import com.kucingoyen.core.base.BaseViewModel
import com.kucingoyen.core.components.error.ErrorGeneralAction
import com.kucingoyen.core.utils.ViewModelUtils
import com.kucingoyen.core.utils.loading.LoadingAction
import com.kucingoyen.data.cache.AppSessionCache
import com.kucingoyen.data.cache.UserInfoCache
import com.kucingoyen.data.di.TagInjection
import com.kucingoyen.entity.model.ErrorModelData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    viewModelUtils: ViewModelUtils,
    @Named(TagInjection.WEB_CLIENT_ID) private val webClientId: String,
    private val repository: AuthRepository,
    val userInfoCache: UserInfoCache,
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
                signInGoogle(idToken){
                    onSuccess()
                }
            } else {
                ErrorGeneralAction.show(error = ErrorModelData(
                    title = "Failed Log in Google"
                ))
            }
        }
    }

    fun signInGoogle(token : String, onSuccess: () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            repository.signInGoogle(token)
                .onStart {
                    LoadingAction.show(true)
                }
                .onCompletion {
                    LoadingAction.show(false)
                }
                .collect { response ->
                    userInfoCache.apply {
                        level = response.level
                        email = response.email
                        partyId = response.partyId
                    }
                    onSuccess()
                }
        }
    }
}
