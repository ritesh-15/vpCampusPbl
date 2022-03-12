package com.example.vpcampus.utils

sealed class ScreenState<T>(
    val data: T? = null,
    val message:String? = null
) {

    class Success<T>(data: T?) : ScreenState<T>(data)

    class Loading<T>(data: T?) : ScreenState<T>(data)

    class Error<T>(message:String,data:T? = null) : ScreenState<T>(data,message)
}