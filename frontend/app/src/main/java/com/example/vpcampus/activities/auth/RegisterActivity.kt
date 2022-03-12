package com.example.vpcampus.activities.auth

import android.content.Intent
import android.os.Bundle
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.databinding.ActivityRegisterBinding

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

    }


    private fun validateData(name:String,email:String,password:String):Boolean{
        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
            return false

        return true
    }
}