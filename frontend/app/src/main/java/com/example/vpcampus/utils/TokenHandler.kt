package com.example.vpcampus.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.vpcampus.models.Tokens
import org.json.JSONException

class TokenHandler {

    companion object{

        private var prefs:SharedPreferences? = null
        private var edit:SharedPreferences.Editor?= null

        fun getTokens(context: Context): Tokens {
            prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val accessToken: String = prefs?.getString("accessToken", "")!!
            val refreshToken: String = prefs?.getString("refreshToken", "")!!

            return Tokens(accessToken, refreshToken)

        }


        fun saveTokenInSharedPreference(context: Context, tokens: Tokens){
            prefs= context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            edit= prefs?.edit()
            try {
                edit?.putString("accessToken", tokens.accessToken)
                edit?.putString("refreshToken", tokens.refreshToken)
                edit?.apply()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }

}