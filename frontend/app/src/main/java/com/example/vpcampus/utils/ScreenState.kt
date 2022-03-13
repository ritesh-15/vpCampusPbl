package com.example.vpcampus.utils

sealed class ScreenState<T>(
    val data: T? = null,
    val message:String? = null,
    val statusCode:Int? = null
) {

    class Success<T>(data: T?) : ScreenState<T>(data)

    class Loading<T>(data: T?) : ScreenState<T>(data)

    class Error<T>(message:String, statusCode:Int? ,data:T? = null) :
        ScreenState<T>(data,message,statusCode)
}