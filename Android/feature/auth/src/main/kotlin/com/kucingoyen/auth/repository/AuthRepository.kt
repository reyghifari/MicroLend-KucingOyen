package com.kucingoyen.auth.repository

import com.kucingoyen.entity.model.RegisterResponse
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun signInGoogle(token: String): Flow<RegisterResponse>


}
