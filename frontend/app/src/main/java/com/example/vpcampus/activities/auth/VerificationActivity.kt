package com.example.vpcampus.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityVerificationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding:ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showTimer(60)

        // handle button verify click
        binding.btnVerify.setOnClickListener {
            handleVerifyBtnClick()
        }

        // resend otp button click
        binding.btnResendOtp.setOnClickListener {
            handlerResendBtnClick()
        }
    }

    // handle resend otp handler
    private fun handlerResendBtnClick(){

        // TODO:Resend otp api call

        showTimer(60)

    }

    //handle verify button click
    private fun handleVerifyBtnClick(){
        // TODO:Verify account api call
        startActivity(Intent(this,ActivationActivity::class.java))
        finish()
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
    private fun showTimer(time:Long){

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