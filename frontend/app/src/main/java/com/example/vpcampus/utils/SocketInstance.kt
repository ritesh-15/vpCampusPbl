package com.example.vpcampus.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.SocketTimeoutException
import java.net.URISyntaxException


class SocketInstance {

    private var mSocket:Socket? = null

    @Synchronized
    fun setSocket(){
        try {
            mSocket = IO.socket(Constants.SOCKET_BASE_URL)
        }catch (e:URISyntaxException){
            Log.e("SOCKET_ERROR",e.message!!)
        }catch (e:SocketTimeoutException){
            Log.e("SOCKET_ERROR",e.message!!)
        }
    }

    @Synchronized
    fun getSocket():Socket?{
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