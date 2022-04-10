package com.example.vpcampus.models

import java.io.Serializable

data class Club(
    val _id: String,
    val name: String,
    val description: String,
    val admin: User,
    val createdAt: String,
    val updatedAt: String,
    val avatar:Avatar
):Serializable