package com.example.vpcampus.repository

import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.clubs.*
import retrofit2.Response

class ClubRepository {

    suspend fun getAllClubs(): Response<GetAllClubsResponse> = ApiInstance.clubsApi.getAllClubs()

    suspend fun createNewClub(
        body:CreateClubBody
    ): Response<CreateClubResponse> = ApiInstance.clubsApi.createNewClub(body)


    suspend fun createChat(
        body:CreateChatBody,
        clubId:String
    ): Response<NewChatResponse> = ApiInstance.clubsApi.createChat(body, clubId)


    suspend fun getAllChats(
        clubId:String
    ): Response<AllChatsResponse> = ApiInstance.clubsApi.getAllChats(clubId)

}