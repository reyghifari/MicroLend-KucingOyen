package com.kucingoyen.auth.repository

import com.kucingoyen.core.utils.DispatcherProvider
import com.kucingoyen.data.service.AuthService
import com.kucingoyen.entity.model.RegisterRequest
import com.kucingoyen.entity.model.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val authService: AuthService,
) : AuthRepository {

    override fun signInGoogle(token: String): Flow<RegisterResponse> =
        flow {
            emit(
                authService.signInGoogle(
                    RegisterRequest(token)
                )
            )
        }.flowOn(dispatcher.io)

}
