package com.example.vpcampus.api.authApi

import com.example.vpcampus.models.Tokens
import com.example.vpcampus.models.User
import java.io.Serializable

data class LoginResponse(
        val ok:Boolean,
        val user: User,
        val tokens: Tokens
    )

data class RefreshResponse(
        val ok:Boolean,
        val user:User,
        val tokens: Tokens
    )

data class Otp(
    val email:String,
    val hash:String
)

data class SendOtpResponse(
    val ok:Boolean,
    val otp : Otp
)

data class VerifyOtpResponse(
    val ok:Boolean,
    val user:User
)

data class RegisterResponse(
    val ok:Boolean,
    val user: User,
    val tokens: Tokens,
    val otp : Otp
)

data class ActivateResponse(
    val ok:Boolean,
    val user: User
)