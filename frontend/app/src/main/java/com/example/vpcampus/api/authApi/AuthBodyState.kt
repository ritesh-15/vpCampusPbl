package com.example.vpcampus.api.authApi

import java.io.Serializable

sealed class AuthBodyState {

    data class LoginRequestBody(
        val email:String,
        val password:String
    ):Serializable

}