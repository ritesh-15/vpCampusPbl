package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.authApi.*
import retrofit2.Response

class AuthRepository {

   suspend fun login(body:LoginRequestBody):Response<LoginResponse>{
        return ApiInstance.authApi.login(body)
    }

   suspend fun refresh(headers:Map<String,String>):Response<RefreshResponse>{
        return ApiInstance.authApi.refresh(headers)
    }

    suspend fun sendOtp(body:SendOtpBody,headers:Map<String,String>):Response<SendOtpResponse>{
        return ApiInstance.authApi.sendOtp(body,headers)
    }

    suspend fun verifyOtp(body:VerifyOtpBody,headers:Map<String,String>):Response<VerifyOtpResponse>{
        return ApiInstance.authApi.verifyOtp(body,headers)
    }

    suspend fun register(body:RegisterBody):Response<RegisterResponse>{
        return ApiInstance.authApi.register(body)
    }

    suspend fun activate(body:ActivateBody,headers:Map<String,String>):Response<ActivateResponse>{
        return ApiInstance.authApi.activate(body,headers)
    }
}