package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.api.notification.CreateNotificationResponse
import com.example.vpcampus.api.notification.DeleteNotificationResponse
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

    suspend fun deleteNotification(
        headers:Map<String,String>,
        id:String
    ):Response<DeleteNotificationResponse>{
        return ApiInstance.notificationApi.deleteNotification(headers,id)
    }

    suspend fun updateNotification(
        headers:Map<String,String>,
        id:String,
        body:NotificationBody
    ):Response<CreateNotificationResponse>{
        return ApiInstance.notificationApi.updateNotification(headers,id,body)
    }

}