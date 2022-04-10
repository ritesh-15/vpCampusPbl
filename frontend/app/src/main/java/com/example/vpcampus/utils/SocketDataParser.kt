package com.example.vpcampus.utils

import android.util.Log
import com.example.vpcampus.models.Avatar
import com.example.vpcampus.models.Notification
import com.example.vpcampus.models.User
import org.json.JSONObject

class SocketDataParser {

    companion object{

        private fun parseAvatarData(avatar:JSONObject):Avatar{
            val url = avatar.getString("url")
            val publicId = avatar.getString("publicId")

            return Avatar(url, publicId)
        }

        private fun parseUserData(user:JSONObject):User{
            val userId = user.getString("_id")
            val name = user.getString("name")
            val email = user.getString("email")
            val department = user.getString("department")
            val yearOfStudy = user.getString("yearOfStudy")
            val bio = user.getString("bio")
            val isActivated = user.getBoolean("isActivated")
            val isVerified = user.getBoolean("isVerified")
            val role = user.getString("role")
            val avatar = parseAvatarData(user.getJSONObject("avatar"))

            return User(userId,name,email,department,yearOfStudy,avatar,bio,role,isActivated,isVerified)

        }

        fun parseNotificationData(notification:JSONObject):Notification{
            val title = notification.getString("title")
            val description = notification.getString("description")
            val createdAt = notification.getString("createdAt")
            val updatedAt = notification.getString("createdAt")
            val id = notification.getString("_id")
            val html = notification.getString("html")
            val access = notification.getString("string")
            val attachments = notification.getString("attachments")
            val user = parseUserData(notification.getJSONObject("userId"))

            return Notification(
                user,
                title,
                description,
                createdAt,
                updatedAt,
                access,
                attachments,
                id,
                html
            )
        }

    }

}