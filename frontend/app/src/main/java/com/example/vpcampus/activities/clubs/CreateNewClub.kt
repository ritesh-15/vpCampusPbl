package com.example.vpcampus.activities.clubs

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.activities.user.EditProfileAcitivty
import com.example.vpcampus.adapters.ClubsAdapter
import com.example.vpcampus.api.clubs.CreateClubBody
import com.example.vpcampus.api.clubs.CreateClubResponse
import com.example.vpcampus.databinding.ActivityCreateNewClubBinding
import com.example.vpcampus.fragments.ClubsFragment
import com.example.vpcampus.models.Avatar
import com.example.vpcampus.models.ErrorMessage
import com.example.vpcampus.network.factory.ClubsViewModelFactory
import com.example.vpcampus.network.models.ClubsViewModel
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.UploadImage
import com.google.gson.Gson

class CreateNewClub : BaseActivity() {

    private lateinit var binding: ActivityCreateNewClubBinding
    private lateinit var clubViewModel: ClubsViewModel

    private var mSelectedFileUri: Uri? = null

    private val activityAction =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mSelectedFileUri = result.data?.data
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewClubBinding.inflate(layoutInflater)

        setUpViewModel()

        binding.btnCreateClub.setOnClickListener {
            handleCreateBtnClick()
        }

        binding.civClubProfilePicture.setOnClickListener {
            // check storage permission
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        observeApiCallResponse()

        setContentView(binding.root)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE && permissions[0] == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                Toast.makeText(this,
                    "You have not granted storage permission. You can grant storage permission from settings.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImageChooser() {
        val gallaryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityAction.launch(gallaryIntent)
    }

    private fun uploadAvatar(avatar: Uri, name: String, description: String) {
        UploadImage.initConfig(this)
        UploadImage().upload(avatar, object : UploadCallback {
            override fun onStart(requestId: String?) {
                showProgressDialog("Uploading Profile...")
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                Log.d("UPLOADING", bytes.toString())
            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                hideProgressDialog()
                if (resultData != null) {
                    val url = resultData["secure_url"].toString()
                    val publicId = resultData["public_id"].toString()
                    createClub(CreateClubBody(
                        name = name,
                        description = description,
                        avatar = Avatar(url, publicId)
                    ))
                }
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                hideProgressDialog()
                showErrorMessage(binding.root, "Something went wrong while uploading avatar")
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                hideProgressDialog()
                showErrorMessage(binding.root, "Something went wrong while uploading avatar")
            }

        })
    }

    private fun createClub(body: CreateClubBody) {
        clubViewModel.createClub(body)
    }

    private fun observeApiCallResponse() {
        clubViewModel.createClub.observe(this) { response ->
            handleCreateClubResponse(response)
        }
    }

    private fun handleCreateClubResponse(state: ScreenState<CreateClubResponse>) {
        when (state) {
            is ScreenState.Loading -> {
                showProgressDialog("Creating...")
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                finish()
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                if (state.errorBody != null) {
                    val error =
                        Gson().fromJson(state.errorBody.toString(), ErrorMessage::class.java)
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun handleCreateBtnClick() {
        val name: String = binding.etClubName.text.toString()
        val description: String = binding.etClubDescription.text.toString()

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this,
                "Name and description of club is not provided!",
                Toast.LENGTH_SHORT).show()

            return
        }

        if (mSelectedFileUri == null) {
            Toast.makeText(this,
                "Please choose profile picture!",
                Toast.LENGTH_SHORT).show()
        } else {
            uploadAvatar(mSelectedFileUri!!, name, description)
        }
    }

    private fun setUpViewModel() {
        val repository = ClubRepository()
        val factory = ClubsViewModelFactory(repository)
        clubViewModel = ViewModelProvider(this, factory)[ClubsViewModel::class.java]
    }

    companion object {
        const val READ_STORAGE_PERMISSION_CODE = 111
    }
}