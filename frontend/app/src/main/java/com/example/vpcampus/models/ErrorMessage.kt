package com.example.vpcampus.models

import java.io.Serializable

data class ErrorMessage(
    val ok:Boolean,
    val message:String,
    val status:Int
):Serializable
