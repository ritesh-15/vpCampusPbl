package com.example.vpcampus.activities.auth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.vpcampus.MainActivity
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.authApi.ActivateBody
import com.example.vpcampus.api.authApi.ActivateResponse
import com.example.vpcampus.databinding.ActivityActivationBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class ActivationActivity : BaseActivity() {

    private lateinit var binding:ActivityActivationBinding

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

        val departmentsAdapter = ArrayAdapter(this, R.layout.list_item, getDepartmentsList())
        binding.actvDepartment.setAdapter(departmentsAdapter)

        val yearOfStudyAdapter = ArrayAdapter(this, R.layout.list_item, getYearOfStudies())
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

    private fun handleActivateBtnClick(){
        val department:String = binding.actvDepartment.text.toString()
        val yearOfStudy:String = binding.actvYearOfStudy.text.toString()
        val bio:String = binding.etBio.text.toString()

        if(mSelectedImageFileUri == null){
            showErrorMessage(binding.root,
                "Please choose avatar!")
            return
        }

        val imageStream: InputStream? = contentResolver.openInputStream(mSelectedImageFileUri!!)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val avatar: String? = encodeImage(selectedImage)

        Log.d("AVATAR",avatar!!)

        if(!validateData(department, yearOfStudy, bio, avatar))
        {
            showErrorMessage(binding.root,
                "User avatar, department, year of study and bio is required!")
            return
        }

        viewModel.activate(ActivateBody(avatar, department, yearOfStudy, bio),
            TokenHandler.getTokens(this))
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private fun validateData(department:String,yearOfStudy:String,bio:String,avatar:String):Boolean{
        if(department.isEmpty() || yearOfStudy.isEmpty() || bio.isEmpty() || avatar.isEmpty()){
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
                        val intent = Intent(this,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        grantResults: IntArray
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

    private fun getDepartmentsList():List<String>{
        val departments = ArrayList<String>()
        departments.add(getString(R.string.comp))
        departments.add(getString(R.string.it))
        departments.add(getString(R.string.elect))
        departments.add(getString(R.string.entc))
        departments.add(getString(R.string.mech))
        departments.add(getString(R.string.civil))
        departments.add(getString(R.string.aids))

        return departments
    }

    private fun getYearOfStudies():List<String>{
        val years = ArrayList<String>()

        years.add(getString(R.string.fe))
        years.add(getString(R.string.se))
        years.add(getString(R.string.te))
        years.add(getString(R.string.be))

        return years
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