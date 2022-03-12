package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.authApi.AuthBodyState
import com.example.vpcampus.api.authApi.AuthResponse
import retrofit2.Response

class AuthRepository {

   suspend fun login(body:AuthBodyState.LoginRequestBody):Response<AuthResponse.LoginResponse>{
        return ApiInstance.authApi.login(body)
    }

    suspend fun refresh(headers:Map<String,String>):Response<AuthResponse.RefreshResponse>{
        return ApiInstance.authApi.refresh(headers)
    }

}