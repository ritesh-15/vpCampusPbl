package com.example.vpcampus.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.MainActivity
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.api.authApi.RefreshResponse
import com.example.vpcampus.databinding.ActivitySplashScreenBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashScreenBinding

    private  var viewModel: AuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tokens = TokenHandler.getTokens(this)

        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(AuthViewModel::class.java)


        viewModel?.refresh(tokens)
        viewModel?.refreshResponse?.observe(this){
           response -> parseData(response)

        }
    }

    private fun parseData(state:ScreenState<RefreshResponse>){
        when(state){

            is ScreenState.Loading -> {
                // TODO
            }

            is ScreenState.Success -> {
                if(state.data != null){
                    // if is not activate the sent to activate screen
                    // if is not verified then sent to verification screen with otp and details
                    // if active and verified then sent to main activity
                    UserState.user = state.data.user
                    TokenHandler.saveTokenInSharedPreference(this,state.data.tokens)
                    val intent = Intent(this,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

            is ScreenState.Error -> {
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
    }

}