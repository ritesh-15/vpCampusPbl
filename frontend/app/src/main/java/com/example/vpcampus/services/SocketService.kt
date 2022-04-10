package com.example.vpcampus.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.vpcampus.models.Notification
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.PushNotificationManager
import com.example.vpcampus.utils.SocketInstance
import com.google.gson.Gson

class SocketService : Service() {

    private var isRunning: Boolean = false
    private var thread: Thread? = null

    private val runnable: Runnable = Runnable {
        val mSocket = SocketInstance().getSocket()
        SocketInstance().connect()
        mSocket?.emit(Constants.JOIN_NOTIFICATION_ROOM)

        mSocket?.on(Constants.NEW_NOTIFICATION) { args ->
            if (args[0] != null) {
                val received = args[0]
                val notification =
                    Gson().fromJson(received.toString(), Notification::class.java)
                PushNotificationManager().generateNotifications(
                    applicationContext, notification.title, notification.description
                )
                Log.d("NEW_NOTIFICATION_SOCKET_BACKGROUND", notification.toString())
            }
        }

    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        isRunning = false
        thread = Thread(runnable)
    }

    override fun onDestroy() {
        isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            thread?.start()
        }

        return START_STICKY
    }
}