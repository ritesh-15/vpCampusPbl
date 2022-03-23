package com.example.vpcampus.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.SocketTimeoutException
import java.net.URISyntaxException


class SocketInstance {

    companion object{
        private var mSocket:Socket? = null
    }


    @Synchronized
    fun getSocket():Socket?{
        if(mSocket == null){
            mSocket = IO.socket(Constants.SOCKET_BASE_URL)
            return mSocket
        }

        return mSocket
    }

    @Synchronized
    fun connect(){
        mSocket?.connect()
    }

    @Synchronized
    fun disconnect(){
        mSocket?.disconnect()
    }
}