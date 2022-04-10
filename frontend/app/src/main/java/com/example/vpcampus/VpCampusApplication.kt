package com.example.vpcampus

import android.app.Application
import android.content.Intent
import com.example.vpcampus.services.SocketService

class VpCampusApplication:Application() {

    companion object{
        private var instance:VpCampusApplication? = null

        fun getInstance():VpCampusApplication{
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val intent = Intent(this, SocketService::class.java)
        startService(intent)
    }

}