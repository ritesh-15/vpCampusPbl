package com.example.vpcampus.utils

import okhttp3.ResponseBody
import retrofit2.Response

sealed class ScreenState<T>(
    val data: T? = null,
    val response:Response<T>? = null,
    val message:String? = null,
    val statusCode:Int? = null,
    val errorBody:ResponseBody? = null
) {

    class Success<T>(data: T?, response: Response<T>? = null) : ScreenState<T>(data, response)

    class Loading<T>(data: T?) : ScreenState<T>(data)

    class Error<T>(message:String? = null, statusCode:Int? = null ,data:T? = null,errorBody:ResponseBody? = null) :
        ScreenState<T>(data,null,message,statusCode)
}