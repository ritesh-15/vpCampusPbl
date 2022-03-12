package com.example.vpcampus.models

import java.io.Serializable

data class Avatar(
    val url:String = "",
    val publicId:String = ""
):Serializable

data class User(
    val _id:String,
    val name:String,
    val email:String,
    val department:String,
    val yearOfStudy:String,
    val avatar:Avatar,
    val bio : String,
    val role: String,
    val isActivated:Boolean,
    val isVerified:Boolean
):Serializable
