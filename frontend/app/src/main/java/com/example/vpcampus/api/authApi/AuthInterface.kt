package com.example.vpcampus.api.authApi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AuthInterface {

    @POST("auth/login")
    suspend fun login(
        @Body body:LoginRequestBody
    ) : Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refresh(
        @HeaderMap headers : Map<String,String>
    ):Response<RefreshResponse>

}