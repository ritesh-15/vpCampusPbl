package com.example.vpcampus.models

import android.text.Html
import java.io.Serializable

/*

  userId: UserInterface | ObjectId;
  title: string;
  description: string;
  createdAt: Date;
  updatedAt: Date;
  access: string;
  attachments: Attachment[];

*/

data class Notification(
    val userId:User,
    val title:String,
    val description:String,
    val createdAt:String,
    val updatedAt:String,
    val access:String,
    val attachments:Any,
    val _id:String,
    val html:String
):Serializable
