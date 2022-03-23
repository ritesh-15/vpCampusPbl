package com.example.vpcampus.utils

import android.content.Context
import com.example.vpcampus.R

object Constants {

    const val USER: String = "user"

    const val API_BASE_URL = "https://vp-campus.herokuapp.com/api/v1/"

    const val SOCKET_BASE_URL = "https://vp-campus.herokuapp.com"

    const val EMAIL = "email"
    const val HASH = "hash"

    const val ACCESS_TOKEN = "accessToken"
    const val REFRESH_TOKEN = "refreshToken"
    const val AUTHORIZATION = "authorization"

    const val NOTIFICATION = "notification"

    const val JOIN_NOTIFICATION_ROOM = "join-notification-room"

    const val SOCKET = "socket"

    const val NEW_NOTIFICATION = "new-notification"

    fun getDepartmentsList(context:Context):List<String>{
        val departments = ArrayList<String>()
        departments.add(context.getString(R.string.comp))
        departments.add(context.getString(R.string.it))
        departments.add(context.getString(R.string.elect))
        departments.add(context.getString(R.string.entc))
        departments.add(context.getString(R.string.mech))
        departments.add(context.getString(R.string.civil))
        departments.add(context.getString(R.string.aids))

        return departments
    }

    fun getYearOfStudies(context:Context):List<String>{
        val years = ArrayList<String>()

        years.add(context.getString(R.string.fe))
        years.add(context.getString(R.string.se))
        years.add(context.getString(R.string.te))
        years.add(context.getString(R.string.be))

        return years
    }

}