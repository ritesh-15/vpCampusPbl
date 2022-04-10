package com.example.vpcampus.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.vpcampus.R
import com.example.vpcampus.activities.MainActivity

class PushNotificationManager {

    fun generateNotifications(context: Context, title: String, description: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_filled)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentInfo(description)
            .setContentText(description).build()


        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(
            0,
            builder
        )
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, description: String): RemoteViews {
        val remoteView = RemoteViews("com.example.vpcampus", R.layout.push_notification_layout)

        remoteView.setTextViewText(R.id.tv_push_notification_title, title)
        remoteView.setTextViewText(R.id.tv_push_notification_description, description)
        remoteView.setImageViewResource(R.id.iv_push_notification_app_logo,
            R.drawable.ic_notification_filled)

        return remoteView
    }

}