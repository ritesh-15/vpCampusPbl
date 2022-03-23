package com.example.vpcampus.api.userApi

import com.example.vpcampus.models.User

data class UpdateProfileResponse(
    val ok:Boolean,
    val user:User
)
