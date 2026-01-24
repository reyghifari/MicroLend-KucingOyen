package com.kucingoyen.data.service


import com.kucingoyen.data.utils.Endpoint
import com.kucingoyen.entity.model.RegisterRequest
import com.kucingoyen.entity.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST(Endpoint.SIGN_IN)
    suspend fun signInGoogle(
        @Body request: RegisterRequest
    ): RegisterResponse


}
