package com.example.vpcampus.models

import java.io.Serializable

/*
  clubId: ClubInterface | mongoose.ObjectId;
  message: string;
  createdAt: Date;
  _id: mongoose.ObjectId | string;
  userId: UserInterface | mongoose.ObjectId;
  updatedAt: Date;
  */

data class Chat(
    val clubId:Club,
    val message:String,
    val createdAt:String,
    val _id:String,
    val userId:User,
    val updatedAt:String
):Serializable
