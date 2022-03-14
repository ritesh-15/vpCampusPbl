package com.example.vpcampus.api.uploads

data class UploadResponse(
    val ok:Boolean,
    val publicId:String,
    val filename:String,
    val url:String
)