package com.example.vpcampus.api.clubs

import com.example.vpcampus.models.Avatar

data class CreateClubBody(
    val name:String,
    val description:String,
    val avatar:Avatar
)

data class CreateChatBody(
    val message:String
)