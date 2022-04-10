package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.clubs.CreateClubBody
import com.example.vpcampus.api.clubs.CreateClubResponse
import com.example.vpcampus.api.clubs.GetAllClubsResponse
import retrofit2.Response

class ClubRepository {

    suspend fun getAllClubs(): Response<GetAllClubsResponse> = ApiInstance.clubsApi.getAllClubs()

    suspend fun createNewClub(
        body:CreateClubBody
    ): Response<CreateClubResponse> = ApiInstance.clubsApi.createNewClub(body)
}