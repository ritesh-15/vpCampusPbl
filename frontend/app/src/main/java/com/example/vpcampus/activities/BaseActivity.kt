package com.example.vpcampus.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityBaseBinding
import com.example.vpcampus.databinding.ProgressDialogBinding
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var binding:ActivityBaseBinding

    private lateinit var dialogBinding:ProgressDialogBinding

    private var dialog:Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showProgressDialog(message: String="Please wait..."){
        dialogBinding = ProgressDialogBinding.inflate(layoutInflater)
        dialogBinding.pbText.text = message
        dialog = Dialog(this)
        dialog?.setContentView(R.layout.progress_dialog)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    fun hideProgressDialog(){
        if(dialog != null){
            dialog?.dismiss()
            dialog = null
        }
    }

    fun showErrorMessage(view:View,message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setTextColor(resources.getColor(R.color.errorMessageColor))
            .setBackgroundTint(resources.getColor(R.color.red))
            .show()
    }
}