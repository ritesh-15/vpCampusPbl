package com.example.vpcampus.api.authApi

import com.example.vpcampus.models.User

data class VerificationResponse(
    val ok:Boolean,
    val user:User
)
