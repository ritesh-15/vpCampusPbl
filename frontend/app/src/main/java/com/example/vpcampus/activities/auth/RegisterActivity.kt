package com.example.vpcampus.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vpcampus.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar back press listener
        binding.registerToolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        // Already have account listener
        binding.tvAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        // Register button click listener
        binding.btnRegister.setOnClickListener {
            handleRegisterBtnClick()
        }
    }

    private fun handleRegisterBtnClick(){
        // TODO: Implement register api

        // after successfully register send user to verification screen for otp validation
        startActivity(Intent(this,VerificationActivity::class.java))
        finish()
    }
}