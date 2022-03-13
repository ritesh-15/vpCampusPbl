package com.example.vpcampus.activities.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.RegisterBody
import com.example.vpcampus.api.authApi.RegisterResponse
import com.example.vpcampus.databinding.ActivityRegisterBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class RegisterActivity : BaseActivity() {

    private lateinit var binding:ActivityRegisterBinding

    private lateinit var viewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(AuthViewModel::class.java)

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

        // observe register api call
        viewModel.registerResponse.observe(this){
            reponse -> parseRegisterResponse(reponse)
        }
    }

    // parse register response data
    private fun parseRegisterResponse(state: ScreenState<RegisterResponse>) {
        when(state){

            is ScreenState.Loading -> {
                showProgressDialog()
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                if(state.data != null){
                    TokenHandler.saveTokenInSharedPreference(this,state.data.tokens)
                    UserState.user = state.data.user
                    val intent = Intent(this,VerificationActivity::class.java)
                    intent.putExtra(Constants.EMAIL,state.data.otp.email)
                    intent.putExtra(Constants.HASH,state.data.otp.hash)
                    startActivity(intent)
                    finish()
                }
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                when(state.statusCode){
                    400 -> {
                        showErrorMessage(binding.root,"All fields are mandatory!")
                    }
                    403 -> {
                        showErrorMessage(binding.root,"User with this email address is already exits!")
                    }
                    500 -> {
                        showErrorMessage(binding.root,"Something went wrong!")
                    }
                }
            }

        }
    }

    private fun handleRegisterBtnClick(){
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etName.text.toString()

        if(!validateData(name,email, password))
        {
            showErrorMessage(binding.root,"Name, Email address and Password is required!")
            return
        }

        viewModel.register(RegisterBody(email, name, password))
    }

    private fun validateData(name:String,email:String,password:String):Boolean{
        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
            return false

        return true
    }
}