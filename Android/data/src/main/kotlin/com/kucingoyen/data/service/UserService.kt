package com.kucingoyen.data.service


import com.kucingoyen.data.utils.Endpoint
import retrofit2.http.GET

interface UserService {

    @GET(Endpoint.USERS)
    suspend fun getUsers(): String


}
