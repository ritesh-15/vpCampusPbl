package com.example.vpcampus.api.uploads

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadInterface {

    @Multipart
    @POST("upload/upload-single")
    suspend fun uploadSingleFile(
        @Part part:MultipartBody.Part,
        @HeaderMap headers:Map<String,String>
    ):Response<UploadResponse>

}