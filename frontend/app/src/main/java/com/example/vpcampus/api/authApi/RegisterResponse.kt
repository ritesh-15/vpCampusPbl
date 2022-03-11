package com.example.vpcampus.api.authApi

import com.example.vpcampus.models.Tokens
import com.example.vpcampus.models.User
import java.io.Serializable

data class OtpData(
    val hash:String,
    val email:String
)

data class RegisterResponse (
    val ok:Boolean,
    val user: User,
    val tokens: Tokens,
    val otp:OtpData
): Serializable