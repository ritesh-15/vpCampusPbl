package com.example.vpcampus.api

import android.util.JsonReader
import com.example.vpcampus.VpCampusApplication
import com.example.vpcampus.api.authApi.AuthInterface
import com.example.vpcampus.api.clubs.ClubInterface
import com.example.vpcampus.api.notification.NotificationInterface
import com.example.vpcampus.api.uploads.UploadInterface
import com.example.vpcampus.api.userApi.UserInterface
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.TokenHandler
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiInstance{

    private val interceptor = Interceptor {
            chain ->
        val tokens = TokenHandler.getTokens(VpCampusApplication.getInstance())
        val accessToken = tokens[Constants.AUTHORIZATION]
        val refreshToken = tokens[Constants.REFRESH_TOKEN]


        chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader(Constants.AUTHORIZATION, accessToken!!)
                    .addHeader(Constants.REFRESH_TOKEN, refreshToken!!)
                    .build()
            )
        }
    }


    private val retrofit:Retrofit by lazy {
        val json = GsonBuilder()
            .setLenient()
            .create()

        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(json))
            .build()
    }

    val authApi:AuthInterface by lazy {
        retrofit.create(AuthInterface::class.java)
    }

    val uploadApi:UploadInterface by lazy {
        retrofit.create(UploadInterface::class.java)
    }

    val notificationApi:NotificationInterface by lazy{
        retrofit.create(NotificationInterface::class.java)
    }

    val userApi:UserInterface by lazy {
        retrofit.create(UserInterface::class.java)
    }

    val clubsApi:ClubInterface by lazy {
        retrofit.create(ClubInterface::class.java)
    }

}