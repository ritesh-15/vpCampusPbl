package com.example.vpcampus.activities.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.MainActivity
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.SendOtpResponse
import com.example.vpcampus.api.authApi.VerifyOtpBody
import com.example.vpcampus.api.authApi.VerifyOtpResponse
import com.example.vpcampus.databinding.ActivityVerificationBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VerificationActivity : BaseActivity() {

    private lateinit var binding:ActivityVerificationBinding

    private var email:String? = null
    private var hash:String? = null

    private lateinit var viewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(AuthViewModel::class.java)

        showTimer()

        // observe verify otp call
        viewModel.verifyOtpResponse.observe(this){
            response -> parseVerifyOtpResponse(response)
        }
        
        // observe resend otp call
        viewModel.sendOtpResponse.observe(this){
            response -> parseSendOtpResponse(response)
        }

        // handle button verify click
        binding.btnVerify.setOnClickListener {
            handleVerifyBtnClick()
        }

        // resend otp button click
        binding.btnResendOtp.setOnClickListener {
            handlerResendBtnClick()
        }

        if(intent.hasExtra(Constants.EMAIL) && intent.hasExtra(Constants.HASH)){
            email = intent.getStringExtra(Constants.EMAIL)
            hash = intent.getStringExtra(Constants.HASH)
            binding.tvEmailAddress.text = email!!
        }
    }

    private fun parseSendOtpResponse(state: ScreenState<SendOtpResponse>) {
        when(state){
            is ScreenState.Loading -> {
                showProgressDialog("Resending...")
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
                    email = state.data.otp.email
                    showTimer()
                }
            }

        }
    }

    // parse verify otp response data
    private fun parseVerifyOtpResponse(state: ScreenState<VerifyOtpResponse>) {
           when(state){

               is ScreenState.Loading -> {
                   showProgressDialog("Verifying...")
               }

               is ScreenState.Success -> {
                   hideProgressDialog()
                   if(state.data != null){
                       UserState.user = state.data.user
                       // check for activation
                       if(!state.data.user.isActivated){
                           startActivity(Intent(this,ActivationActivity::class.java))
                           finish()
                       }else{
                           // start main activity
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
                       400 -> {
                           showErrorMessage(binding.root,"One time password has been expired!")
                       }
                       401 -> {
                           showErrorMessage(binding.root,"One time password does not match!")
                       }
                       500 -> {
                           showErrorMessage(binding.root,"Something went wrong!")
                       }
                   }
               }
           }
    }

    // handle resend otp handler
    private fun handlerResendBtnClick(){
        if(email == null || hash == null){
            showErrorMessage(binding.root,"Something went wrong!")
            return
        }

        viewModel.sendOtp(email!!,TokenHandler.getTokens(this))
    }

    //handle verify button click
    private fun handleVerifyBtnClick(){
        val otp = binding.etOneTimePassword.text.toString()

        if(otp.isEmpty()){
            showErrorMessage(binding.root,"One time password is required!")
            return
        }

        viewModel.verifyOtp(VerifyOtpBody(otp,email!!,hash!!),TokenHandler.getTokens(this))

    }

    // handle back press here
    override fun onBackPressed() {
        // show custom dialog

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.verification_dialog_title))
            .setMessage(resources.getString(R.string.verification_dialog_description))
            .setNegativeButton(resources.getString(R.string.verification_dialog_cancel)) { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.verification_dialog_Proceed)) { _, _ ->
                // Respond to positive button press
                super.onBackPressed()
            }
            .show()
    }

    // handle timer
    private fun showTimer(time:Long = 60){

        binding.llResendOtp.visibility = View.GONE
        binding.llOtpTimer.visibility = View.VISIBLE

        object : CountDownTimer(time * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = millisUntilFinished/1000

                if(timeRemaining < 10){
                    binding.tvOtpTimer.text = "00:0$timeRemaining"
                }else{
                    binding.tvOtpTimer.text = "00:$timeRemaining"
                }
            }

            override fun onFinish() {
                binding.llResendOtp.visibility = View.VISIBLE
                binding.llOtpTimer.visibility = View.GONE
            }
        }.start()

    }
}