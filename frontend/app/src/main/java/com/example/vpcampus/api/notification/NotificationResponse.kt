package com.example.vpcampus.api.notification

import com.example.vpcampus.models.Notification
import java.io.Serializable

data class CreateNotificationResponse(
    val ok:Boolean,
    val notification:Notification
)


data class AllNotificationResponse(
    val ok: Boolean,
    val notifications:List<Notification>
):Serializable