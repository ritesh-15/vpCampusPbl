package com.example.vpcampus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.activities.user.ProfileActivity
import com.example.vpcampus.databinding.ActivityMainBinding
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.TokenHandler

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }
}