package com.example.vpcampus.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vpcampus.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar back press handler
        binding.loginToolbarr.setNavigationOnClickListener{
            onBackPressed()
        }

        // Create new account activity handler
        binding.tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
    }


}