package com.example.vpcampus.api.userApi

import com.example.vpcampus.models.Avatar

data class UpdateProfileBody(
    val name:String?,
    val bio:String?,
    val department:String?,
    val yearOfStudy:String?,
    val avatar: Avatar?
)
