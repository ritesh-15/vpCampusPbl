package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.authApi.LoginRequestBody
import com.example.vpcampus.api.authApi.LoginResponse
import com.example.vpcampus.api.authApi.RefreshResponse
import retrofit2.Response

class AuthRepository {

   suspend fun login(body:LoginRequestBody):Response<LoginResponse>{
        return ApiInstance.authApi.login(body)
    }

   suspend fun refresh(headers:Map<String,String>):Response<RefreshResponse>{
        return ApiInstance.authApi.refresh(headers)
    }

}