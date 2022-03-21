package com.example.vpcampus.api.notification

import retrofit2.Response
import retrofit2.http.*

interface NotificationInterface {

    @POST("notification/create")
    suspend fun createNewNotification(
        @Body body:NotificationBody,
        @HeaderMap headers:Map<String,String>
    ):Response<CreateNotificationResponse>

    @GET("notification/all")
    suspend fun getAllNotifications(
        @HeaderMap headers:Map<String,String>,
        @Query("sent") sent:String? = null
    ):Response<AllNotificationResponse>

}