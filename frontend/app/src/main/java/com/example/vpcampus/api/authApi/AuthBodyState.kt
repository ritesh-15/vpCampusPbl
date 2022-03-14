package com.example.vpcampus.api.authApi

import com.example.vpcampus.models.Avatar


data class LoginRequestBody(
        val email:String,
        val password:String
    )


data class SendOtpBody(
    val email:String
)

data class VerifyOtpBody(
    val otp:String,
    val email:String,
    val hash:String
)

data class RegisterBody(
    val email: String,
    val name:String,
    val password: String
)

data class ActivateBody(
    val avatar:Avatar,
    val department:String,
    val yearOfStudy:String,
    val bio:String
)