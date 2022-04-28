package com.example.vpcampus.api.clubs

import retrofit2.Response
import retrofit2.http.*

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

    @DELETE("club/{clubId}")
    suspend fun deleteClub(
        @Path("clubId") clubId:String
    ):Response<DeleteClubResponse>

    @PUT("club/{clubId}")
    suspend fun updateClub(
        @Path("clubId") clubId:String,
        @Body body:UpdateClubBody
    ):Response<CreateClubResponse>
}