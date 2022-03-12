package com.example.vpcampus.api.authApi

import com.example.vpcampus.models.Tokens
import com.example.vpcampus.models.User

class AuthResponse {

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

}