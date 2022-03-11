package com.example.vpcampus.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vpcampus.MainActivity
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.api.authApi.AuthApi
import com.example.vpcampus.databinding.ActivitySplashScreenBinding
import com.example.vpcampus.models.User
import com.example.vpcampus.utils.TokenHandler

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tokens = TokenHandler.getTokens(this)

        if(tokens.accessToken.isEmpty() || tokens.refreshToken.isEmpty()){
            onFailureListener()
        }

        AuthApi.refresh(this,tokens,
            {
                user -> onSuccessListener(user)
            },
            {
                onFailureListener()
            }
            )

    }

    // on success
    private fun onSuccessListener(user:User){
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    // on failure
    private fun onFailureListener(){
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}