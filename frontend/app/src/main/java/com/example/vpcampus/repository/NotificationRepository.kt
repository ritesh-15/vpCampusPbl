package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.api.notification.CreateNotificationResponse
import com.example.vpcampus.api.notification.NotificationBody
import retrofit2.Response

class NotificationRepository {

    suspend fun createNewNotification(
        body:NotificationBody,
        headers:Map<String,String>
    ):Response<CreateNotificationResponse>{
        return ApiInstance.notificationApi.createNewNotification(body,headers)
    }

    suspend fun getAllNotifications(
        headers:Map<String,String>,
        sent:String? = null
    ):Response<AllNotificationResponse>{
        return ApiInstance.notificationApi.getAllNotifications(headers,sent)
    }

}