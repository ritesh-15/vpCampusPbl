package com.example.vpcampus.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.vpcampus.MainActivity
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.AuthApi
import com.example.vpcampus.databinding.ActivityLoginBinding
import com.example.vpcampus.models.User

class LoginActivity : BaseActivity() {

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

        showProgressDialog()
        AuthApi.login(this,email,password,
            {
                user -> onLoginSuccessListener(user)
                hideProgressDialog()
            },
            {
                error -> // TODO
                hideProgressDialog()
            }
            )
    }

    // on login success
    private fun onLoginSuccessListener(user:User){
        Toast.makeText(this,"Login success ${user._id}",Toast.LENGTH_SHORT).show()
        // TODO: set user globally
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    // on login fail
    private fun onLoginFailureSuccess(error:String){

    }
}