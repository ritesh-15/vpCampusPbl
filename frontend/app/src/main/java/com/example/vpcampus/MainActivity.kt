package com.example.vpcampus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.activities.user.ProfileActivity
import com.example.vpcampus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}