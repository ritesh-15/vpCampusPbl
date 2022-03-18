package com.example.vpcampus.activities.user

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.userApi.UpdateProfileBody
import com.example.vpcampus.api.userApi.UpdateProfileResponse
import com.example.vpcampus.databinding.ActivityEditProfileAcitivtyBinding
import com.example.vpcampus.models.Avatar
import com.example.vpcampus.network.factory.UserViewModelFactory
import com.example.vpcampus.network.models.UserViewModel
import com.example.vpcampus.repository.UserRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.example.vpcampus.utils.UploadImage
import java.io.ByteArrayOutputStream
import java.io.IOException


class EditProfileAcitivty : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mSelectedImageFileUri: Uri? = null

    private lateinit var binding:ActivityEditProfileAcitivtyBinding

    private lateinit var userViewModel:UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileAcitivtyBinding.inflate(layoutInflater)

        setContentView(binding.root)


        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository())
        )[UserViewModel::class.java]

        setUpData()

        binding.btnUpdate.setOnClickListener{
            handleUpdateBtnClick()
        }

        binding.editProfileToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        userViewModel.updateProfileResponse.observe(this){
            response -> parseUpdateProfileResponse(response)
        }

        binding.ivEdProfileUserImage.setOnClickListener {
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

    }

    // upload avatar
    private fun uploadAvatar(avatar: Uri){
        UploadImage.initConfig(this)
        UploadImage().upload(avatar,object: UploadCallback {
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
                    updateProfile(Avatar(url,publicId))
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

    // show gallary
    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    // permission activity result
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
                    .placeholder(com.example.vpcampus.R.drawable.ic_user_avatar)
                    .into(binding.ivEdProfileUserImage)
            }catch (e: IOException){
                e.printStackTrace()
            }

        }
    }

    // update response parser
    private fun parseUpdateProfileResponse(state: ScreenState<UpdateProfileResponse>) {
        when(state){
            is ScreenState.Loading -> {
                showProgressDialog("Updating...")
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                Toast.makeText(this,"Update successfully!",Toast.LENGTH_SHORT).show()
                UserState.user = state.data?.user
                setResult(Activity.RESULT_OK,Intent())
                finish()
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                Toast.makeText(this,state.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    // update profile
    private fun updateProfile(avatar: Avatar?){
        val name = binding.etProfileName.text.toString()
        val bio = binding.etEdProfileBio.text.toString()
        val department = binding.actvEditDepartment.text.toString()
        val yearOfStudy = binding.actvEditYearOfStudy.text.toString()

        val body = UpdateProfileBody(
            name,
            bio,
            department,
            yearOfStudy,
            avatar
        )

        userViewModel.updateProfile(body,TokenHandler.getTokens(this))

    }

    // update button click listener
    private fun handleUpdateBtnClick() {
        if(mSelectedImageFileUri != null){
            uploadAvatar(mSelectedImageFileUri!!)
        }else{
            updateProfile(null)
        }
    }


    // set up ui
    private fun setUpData(){
        Glide
            .with(this)
            .load(UserState.user!!.avatar.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(binding.ivEdProfileUserImage)


        binding.etProfileName.setText(UserState.user!!.name)
        binding.etEdProfileBio.setText(UserState.user!!.bio)


        val departmentsAdapter = ArrayAdapter(this, R.layout.list_item, Constants.getDepartmentsList(this))
        binding.actvEditDepartment.setText(UserState.user!!.department,true)
        binding.actvEditDepartment.setAdapter(departmentsAdapter)

        val yearOfStudyAdapter = ArrayAdapter(this, R.layout.list_item, Constants.getYearOfStudies(this))
        binding.actvEditYearOfStudy.setText(UserState.user!!.yearOfStudy,true)
        binding.actvEditYearOfStudy.setAdapter(yearOfStudyAdapter)

    }
}