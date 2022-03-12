package com.example.vpcampus.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.AuthResponse
import com.example.vpcampus.databinding.ActivityLoginBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler


class LoginActivity : BaseActivity() {

    private lateinit var binding:ActivityLoginBinding

    private lateinit var viewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // view model
        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(AuthViewModel::class.java)

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

        viewModel.loginResponse.observe(this){
            response ->
            parseLoginResponse(response)

        }

    }

    // parse the login response and to the specific task
    private fun parseLoginResponse(state:ScreenState<AuthResponse.LoginResponse>){
        when(state){

            is ScreenState.Success -> {
                hideProgressDialog()
                if(state.data != null){
                    Toast.makeText(this,"Login successfull",Toast.LENGTH_SHORT).show()

                    // save tokens
                    TokenHandler.saveTokenInSharedPreference(this,state.data.tokens)

                    // save user globally
                    UserState.user = state.data.user
                }
            }

            is ScreenState.Loading -> {
                showProgressDialog()
            }

            is ScreenState.Error -> {
                hideProgressDialog()
            }
        }
    }

}