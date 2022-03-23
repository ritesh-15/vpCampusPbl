package com.example.vpcampus.repository

import com.cloudinary.Api
import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.userApi.UpdateProfileBody
import com.example.vpcampus.api.userApi.UpdateProfileResponse
import retrofit2.Response

class UserRepository {

    suspend fun updateProfile(
        body:UpdateProfileBody,
        headers:Map<String,String>
    ):Response<UpdateProfileResponse>{
        return ApiInstance.userApi.updateProfile(body,headers)
    }

    suspend fun getUserDetails(
        headers: Map<String, String>
    ):Response<UpdateProfileResponse>{
        return ApiInstance.userApi.getUserDetails(headers)
    }

}