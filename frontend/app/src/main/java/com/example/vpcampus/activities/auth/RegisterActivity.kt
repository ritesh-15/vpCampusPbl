package com.example.vpcampus.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.AuthApi
import com.example.vpcampus.api.authApi.OtpData
import com.example.vpcampus.databinding.ActivityRegisterBinding
import com.example.vpcampus.models.User
import com.example.vpcampus.utils.Constants

class RegisterActivity : BaseActivity() {

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
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etName.text.toString()

        if(!validateData(name,email, password))
        {
            showErrorMessage(binding.root,"Name, Email address and Password is required!")
            return
        }

        showProgressDialog()
        AuthApi.register(this,email,password,name,{
            user, otp ->  onSuccessListener(user,otp)
        },{ onFailureResponse() })

    }

    private fun onSuccessListener(user:User,otp:OtpData){
        val intent = Intent(this,VerificationActivity::class.java)
        intent.putExtra(Constants.EMAIL,otp.email)
        intent.putExtra(Constants.HASH,otp.hash)
        hideProgressDialog()
        startActivity(intent)
        finish()
    }

    private fun onFailureResponse(){
        hideProgressDialog()
        showErrorMessage(binding.root,"Email address is already taken by another user!")
    }

    private fun validateData(name:String,email:String,password:String):Boolean{
        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
            return false

        return true
    }
}