package com.example.vpcampus.api.clubs

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClubInterface {

    @POST("club/create")
    suspend fun createNewClub(
        @Body body: CreateClubBody,
    ): Response<CreateClubResponse>

    @GET("club/clubs")
    suspend fun getAllClubs(
    ): Response<GetAllClubsResponse>

    @POST("chat/new-chat/{clubId}")
    suspend fun createChat(
        @Body body: CreateChatBody,
        @Path("clubId") clubId: String,
    ): Response<NewChatResponse>

    @GET("chat/chats/{clubId}")
    suspend fun getAllChats(
        @Path("clubId") clubId: String,
    ): Response<AllChatsResponse>
}