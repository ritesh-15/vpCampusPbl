package com.example.vpcampus.models

import java.io.Serializable

data class Tokens(
    val accessToken:String = "",
    val refreshToken:String = ""
):Serializable
