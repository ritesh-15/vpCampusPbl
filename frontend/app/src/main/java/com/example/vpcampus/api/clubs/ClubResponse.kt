package com.example.vpcampus.api.clubs

import com.example.vpcampus.models.Club
import com.example.vpcampus.models.User
import java.util.*
import kotlin.collections.ArrayList

data class CreateClubResponse(
    val ok:Boolean,
    val club:Club
)


data class GetAllClubsResponse(
    val ok:Boolean,
    val clubs:ArrayList<Club>
)