package com.example.vpcampus.api.authApi

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.*
import com.example.vpcampus.activities.SplashScreenActivity
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.activities.auth.RegisterActivity
import com.example.vpcampus.api.ApiInstance
import com.example.vpcampus.api.GsonRequest
import com.example.vpcampus.models.Tokens
import com.example.vpcampus.models.User
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.TokenHandler
import com.google.gson.JsonObject
import org.json.JSONException


class AuthApi {

    companion object{

        fun register(context: RegisterActivity,email: String,password: String,name:String,
                    onSuccessListener: (user: User) -> Unit,
                     onFailureListener: () -> Unit
                     ){
            val body = JsonObject()

            body.addProperty("email",email)
            body.addProperty("password",password)
            body.addProperty("name",name)

            val url = "${Constants.API_BASE_URL}auth/refresh"

            val headers: HashMap<String, String> = HashMap()
            headers["Content-Type"] = "application/json; charset=utf-8"
            headers["User-Agent"] = "Mozilla/5.0"

            val request = GsonRequest(Request.Method.POST,url,LoginResponse::class.java,body,headers,
                { response ->
                    try {
                        onSuccessListener(response.user)
                        // save tokens to shared preference
                        TokenHandler.saveTokenInSharedPreference(context,response.tokens)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                },
                { error ->
                    onFailureListener()
                }
            )

            ApiInstance.getInstance(context).addToRequestQueue(request)
        }

        fun refresh(context:SplashScreenActivity,tokens:Tokens,
                          onSuccessListener:(user:User) -> Unit,onFailureListener:() -> Unit
                          ){

            val headers: HashMap<String, String> = HashMap()
            headers["Content-Type"] = "application/json; charset=utf-8"
            headers["User-Agent"] = "Mozilla/5.0"
            headers["authorisation"] = "Bearer ${tokens.accessToken}"
            headers["refreshToken"] = "Bearer ${tokens.refreshToken}"

            val url = "${Constants.API_BASE_URL}auth/refresh"

            val request = GsonRequest(Request.Method.GET,url,LoginResponse::class.java,null,headers,
                { response ->
                    try {
                        onSuccessListener(response.user)
                        TokenHandler.saveTokenInSharedPreference(context,response.tokens)
                    }catch (e:java.lang.Exception){
                        onFailureListener()
                    }
                },
                { error ->
                    onFailureListener()
                }
            )

            ApiInstance.getInstance(context).addToRequestQueue(request)

        }

        fun login(context:LoginActivity,email:String,password:String,
                  onSuccessListener:(user:User) -> Unit,onFailureListener:(error:String) -> Unit) {
            val body = JsonObject()

            body.addProperty("email",email)
            body.addProperty("password",password)

            val url = "${Constants.API_BASE_URL}auth/login"

            val headers: HashMap<String, String> = HashMap()
            headers["Content-Type"] = "application/json; charset=utf-8"
            headers["User-Agent"] = "Mozilla/5.0"

            val request = GsonRequest(Request.Method.POST,url,LoginResponse::class.java,body,headers,
                {
                    response ->

                    try {
                        onSuccessListener(response.user)
                        // save tokens to shared preference
                        TokenHandler.saveTokenInSharedPreference(context,response.tokens)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                },
                {
                    error ->
                    onFailureListener("Something went wrong")
                }
                )

            ApiInstance.getInstance(context).addToRequestQueue(request)
        }
    }

}