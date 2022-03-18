package com.example.vpcampus.api.authApi

import retrofit2.Response
import retrofit2.http.*

interface AuthInterface {

    @POST("auth/login")
    suspend fun login(
        @Body body:LoginRequestBody
    ) : Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refresh(
        @HeaderMap headers : Map<String,String>
    ):Response<RefreshResponse>

    @POST("auth/send-otp")
    suspend fun sendOtp(
        @Body body:SendOtpBody,
        @HeaderMap headers : Map<String,String>
    ):Response<SendOtpResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(
        @Body body:VerifyOtpBody,
        @HeaderMap headers : Map<String,String>
    ):Response<VerifyOtpResponse>

    @POST("auth/register")
    suspend fun register(
        @Body body : RegisterBody
    ):Response<RegisterResponse>

    @PUT("auth/activate")
    suspend fun activate(
        @Body body : ActivateBody,
        @HeaderMap headers : Map<String,String>
    ):Response<ActivateResponse>

    @DELETE("auth/logout")
    suspend fun logout(
        @HeaderMap headers : Map<String,String>
    ):Response<LogOutResponse>
}