package com.example.vpcampus.activities.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.MainActivity
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.LoginResponse
import com.example.vpcampus.api.authApi.SendOtpResponse
import com.example.vpcampus.databinding.ActivityLoginBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler


class LoginActivity : BaseActivity() {

    private lateinit var binding:ActivityLoginBinding

    private lateinit var viewModel:AuthViewModel

    private var hash:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // view model
        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory)[AuthViewModel::class.java]

        // Toolbar back press handler
        binding.loginToolbarr.setNavigationOnClickListener{
            onBackPressed()
        }

        // Create new account activity handler
        binding.tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        // login button click listener
        binding.btnLogin.setOnClickListener {
            handleLoginBtnClick()
        }

        // observe login api call
        viewModel.loginResponse.observe(this) { response ->
            parseLoginResponse(response)
        }

        // observe send otp api call
        viewModel.sendOtpResponse.observe(this){
            response -> parseSendOtpResponse(response)
        }
    }

    // parse the send otp response data
    private fun parseSendOtpResponse(state:ScreenState<SendOtpResponse>){
        when(state){

            is ScreenState.Loading -> {
                showProgressDialog("Sending verification code")
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                when(state.statusCode){
                    400 -> {
                        showErrorMessage(binding.root,"Email address is not found please retry!")
                    }
                    404 -> {
                        showErrorMessage(binding.root,"User not found with this email address!")
                    }
                    500 -> {
                        showErrorMessage(binding.root,"Something went wrong!")
                    }
                }
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                if(state.data != null){
                    hash = state.data.otp.hash
                    val intent = Intent(this,VerificationActivity::class.java)
                    intent.putExtra(Constants.EMAIL,state.data.otp.email)
                    intent.putExtra(Constants.HASH,hash!!)
                    startActivity(intent)
                    finish()
                }
            }

        }
    }

    // handle login button
    private fun handleLoginBtnClick(){
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            showErrorMessage(binding.root,"Email address and password is required!")
            return
        }

        viewModel.login(email,password)
    }

    // parse login response
    private fun parseLoginResponse(state:ScreenState<LoginResponse>){
        when(state){

            is ScreenState.Loading -> {
                showProgressDialog()
            }

            is ScreenState.Success -> {
                hideProgressDialog()

                if(state.data != null){
                    TokenHandler.saveTokenInSharedPreference(this,state.data.tokens)
                    UserState.user = state.data.user

                    // check if user is activated or not
                    if(!state.data.user.isVerified){
                        viewModel.sendOtp(state.data.user.email,TokenHandler.getTokens(this))
                    }else if(!state.data.user.isActivated){
                        val intent = Intent(this,ActivationActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }

                }
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                when(state.statusCode){
                    401 -> {
                        showErrorMessage(binding.root,"Email address or password is incorrect!")
                    }
                    404 -> {
                        showErrorMessage(binding.root,"User not found with this email address!")
                    }
                    500 -> {
                        showErrorMessage(binding.root,"Something went wrong!")
                    }
                }
            }

        }
    }


}