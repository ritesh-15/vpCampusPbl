package com.example.vpcampus.api.clubs

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ClubInterface {

    @POST("club/create")
    suspend fun createNewClub(
        @Body body:CreateClubBody
    ):Response<CreateClubResponse>


    @GET("club/clubs")
    suspend fun getAllClubs(
    ):Response<GetAllClubsResponse>
}