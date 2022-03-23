package com.example.vpcampus.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.auth.ActivationActivity
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.activities.auth.VerificationActivity
import com.example.vpcampus.api.authApi.RefreshResponse
import com.example.vpcampus.api.authApi.SendOtpResponse
import com.example.vpcampus.databinding.ActivitySplashScreenBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashScreenBinding

    private  var viewModel: AuthViewModel? = null

    private var hash:String? = null

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
           response -> parseRefreshResponse(response)

        }

        // send otp obeserver
        viewModel?.sendOtpResponse?.observe(this){
            response -> parseSendOtpResponse(response)
        }
    }

    // parse send otp response data
    private fun parseSendOtpResponse(state: ScreenState<SendOtpResponse>){
        when(state){

            is ScreenState.Loading -> {
                // do nothing
            }

            is ScreenState.Error -> {
                when(state.statusCode){
                    400 -> {
                        Toast.makeText(this,"Email address is not found please retry!",
                            Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(this,"User not found with given email addresss!",
                            Toast.LENGTH_SHORT).show()
                    }
                    500 -> {
                        Toast.makeText(this,"Something went wrong!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            is ScreenState.Success -> {
                if(state.data != null){
                    hash = state.data.otp.hash
                    val intent = Intent(this, VerificationActivity::class.java)
                    intent.putExtra(Constants.EMAIL,state.data.otp.email)
                    intent.putExtra(Constants.HASH,hash!!)
                    startActivity(intent)
                    finish()
                }
            }

        }

    }

    // parse refresh response data
    private fun parseRefreshResponse(state:ScreenState<RefreshResponse>){
        when(state){

            is ScreenState.Loading -> {
                // TODO
            }

            is ScreenState.Success -> {
                if(state.data != null){
                    TokenHandler.saveTokenInSharedPreference(this,state.data.tokens)
                    UserState.user = state.data.user

                    // check if user is activated or not
                    if(!state.data.user.isVerified){
                        viewModel?.sendOtp(state.data.user.email,TokenHandler.getTokens(this))
                    }else if(!state.data.user.isActivated){
                        val intent = Intent(this, ActivationActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra(Constants.USER,state.data.user)
                        startActivity(intent)
                        finish()
                    }
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