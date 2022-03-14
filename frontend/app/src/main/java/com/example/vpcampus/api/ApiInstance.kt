package com.example.vpcampus.api

import com.example.vpcampus.api.authApi.AuthInterface
import com.example.vpcampus.api.uploads.UploadInterface
import com.example.vpcampus.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiInstance{

    val retrofit:Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi:AuthInterface by lazy {
        retrofit.create(AuthInterface::class.java)
    }

    val uploadApi:UploadInterface by lazy {
        retrofit.create(UploadInterface::class.java)
    }

}