package com.example.vpcampus.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.MainActivity
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.api.authApi.AuthResponse
import com.example.vpcampus.databinding.ActivitySplashScreenBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashScreenBinding

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tokens = TokenHandler.getTokens(this)

        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(AuthViewModel::class.java)

        viewModel.refresh(tokens)

        viewModel.refreshResponse.observe(this){
            response -> parseRefreshResponseData(response)
        }
    }

    // reseponse data parser
    private fun parseRefreshResponseData(state:ScreenState<AuthResponse.RefreshResponse>){
        when(state){
                is ScreenState.Loading -> {

                }

                is ScreenState.Error -> {
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }

                is ScreenState.Success -> {
                    if(state.data != null){
                        UserState.user = state.data.user
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                }
        }
    }

}