package com.example.vpcampus.api.userApi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PUT

interface UserInterface {

    @PUT("user/update-profile")
    suspend fun updateProfile(
        @Body body:UpdateProfileBody,
        @HeaderMap headers:Map<String,String>
    ):Response<UpdateProfileResponse>

    @GET("user/get")
    suspend fun getUserDetails(
        @HeaderMap headers:Map<String,String>
    ):Response<UpdateProfileResponse>

}