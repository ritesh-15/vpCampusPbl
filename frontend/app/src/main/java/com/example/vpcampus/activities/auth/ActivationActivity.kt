package com.example.vpcampus.activities.auth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.vpcampus.activities.MainActivity
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.ActivateBody
import com.example.vpcampus.api.authApi.ActivateResponse
import com.example.vpcampus.databinding.ActivityActivationBinding
import com.example.vpcampus.models.Avatar
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.factory.UploadViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.network.models.UploadViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.repository.UploadRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.example.vpcampus.utils.UploadImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.IOException


class ActivationActivity : BaseActivity() {

    private lateinit var binding:ActivityActivationBinding

    private lateinit var uploadViewModel :UploadViewModel

    private lateinit var viewModel:AuthViewModel

    private var mSelectedImageFileUri:Uri? = null

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val viewModelFactory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(AuthViewModel::class.java)

        // setting up upload view model
        uploadViewModel = ViewModelProvider(
            this,
            UploadViewModelFactory(UploadRepository())
        ).get(UploadViewModel::class.java)

        val departmentsAdapter = ArrayAdapter(this, R.layout.list_item, Constants.getDepartmentsList(this))
        binding.actvDepartment.setAdapter(departmentsAdapter)

        val yearOfStudyAdapter = ArrayAdapter(this, R.layout.list_item, Constants.getYearOfStudies(this))
        binding.actvYearOfStudy.setAdapter(yearOfStudyAdapter)

        binding.cimUserAvatar.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }else{
                // permission not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }


        binding.btnActivate.setOnClickListener {
            handleActivateBtnClick()
        }

        // activate response observer
        viewModel.activateResponse.observe(this){
            response -> parseActivateResponse(response)
        }

    }


    // upload avatar to cloudinary
    private fun uploadAvatar(avatar: Uri){
        UploadImage.initConfig(this)
        UploadImage().upload(avatar,object:UploadCallback{
            override fun onStart(requestId: String?) {
                showProgressDialog("Uploading avatar...")
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                Log.d("UPLOADING",bytes.toString())
            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                hideProgressDialog()
                if(resultData != null){
                    val url = resultData["secure_url"].toString()
                    val publicId = resultData["public_id"].toString()

                    activateAccount(Avatar(url,publicId))
                }
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                hideProgressDialog()
                showErrorMessage(binding.root,"Something went wrong while uploading avatar")
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                hideProgressDialog()
                showErrorMessage(binding.root,"Something went wrong while uploading avatar")
            }

        })
    }

    private fun activateAccount(avatar:Avatar){
        val department:String = binding.actvDepartment.text.toString()
        val yearOfStudy:String = binding.actvYearOfStudy.text.toString()
        val bio:String = binding.etBio.text.toString()

        viewModel.activate(ActivateBody(
            avatar,
            department, yearOfStudy, bio
        ),TokenHandler.getTokens(this))

    }

    private fun handleActivateBtnClick(){
        val department:String = binding.actvDepartment.text.toString()
        val yearOfStudy:String = binding.actvYearOfStudy.text.toString()
        val bio:String = binding.etBio.text.toString()

        if(mSelectedImageFileUri == null ||  !validateData(department,yearOfStudy,bio)){
            showErrorMessage(binding.root,"Avatar, department, year of study and bio is required!")
            return
        }

        uploadAvatar(mSelectedImageFileUri!!)
    }

    private fun validateData(department:String,yearOfStudy:String,bio:String):Boolean{
        if(department.isEmpty() || yearOfStudy.isEmpty() || bio.isEmpty()){
            return false
        }

        return true
    }

    private fun parseActivateResponse(state: ScreenState<ActivateResponse>) {
        when(state){

            is ScreenState.Loading -> {
                showProgressDialog("Activating...")
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                if(state.data != null){
                    UserState.user = state.data.user
                    if(state.data.user.isActivated && state.data.user.isVerified){
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra(Constants.USER,state.data.user)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Something is remaining!",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                showErrorMessage(binding.root,state.message!!)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&
            requestCode == PICK_IMAGE_REQUEST_CODE &&
            data!!.data != null){
            mSelectedImageFileUri = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .into(binding.cimUserAvatar)
            }catch (e:IOException){
                e.printStackTrace()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
                // access the permission
                showImageChooser()
            }
        }else{
            // permission not granted
            Toast.makeText(this,
                "You have not granted the storage permission you can grant the storage" +
                        "permission from settings",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    // handle back press here
    override fun onBackPressed() {
        // show custom dialog

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.account_activation_dialog_title))
            .setMessage(resources.getString(R.string.account_activation_dialog_description))
            .setNegativeButton(resources.getString(R.string.account_activation_dialog_cancel)) { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.account_activation_dialog_proceed)) { _, _ ->
                // Respond to positive button press
                super.onBackPressed()
            }
            .show()
    }
}