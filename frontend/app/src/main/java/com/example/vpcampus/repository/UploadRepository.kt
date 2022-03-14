package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.uploads.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response

class UploadRepository {

    suspend fun uploadSingleFile(
        file : MultipartBody.Part,
        headers:Map<String,String>
    ):Response<UploadResponse>{
        return ApiInstance.uploadApi.uploadSingleFile(file,headers)
    }

}