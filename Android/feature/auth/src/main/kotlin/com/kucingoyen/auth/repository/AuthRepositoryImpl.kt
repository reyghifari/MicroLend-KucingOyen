package com.kucingoyen.auth.repository

import com.kucingoyen.core.utils.DispatcherProvider
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
) : AuthRepository {


}
