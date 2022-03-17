package com.example.vpcampus.activities.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.userApi.UpdateProfileBody
import com.example.vpcampus.api.userApi.UpdateProfileResponse
import com.example.vpcampus.databinding.ActivityEditProfileAcitivtyBinding
import com.example.vpcampus.network.factory.UserViewModelFactory
import com.example.vpcampus.network.models.UserViewModel
import com.example.vpcampus.repository.UserRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class EditProfileAcitivty : BaseActivity() {

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

        userViewModel.updateProfileResponse.observe(this){
            response -> parseUpdateProfileResponse(response)
        }
    }

    private fun parseUpdateProfileResponse(state: ScreenState<UpdateProfileResponse>) {
        when(state){
            is ScreenState.Loading -> {
                showProgressDialog("Updating...")
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                Toast.makeText(this,"Update successfully!",Toast.LENGTH_SHORT).show()
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                Toast.makeText(this,state.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleUpdateBtnClick() {
        val name = binding.etProfileName.text.toString()
        val email = binding.etProfileEmail.text.toString()
        val bio = binding.etEdProfileBio.text.toString()
        val department = binding.actvEditDepartment.text.toString()
        val yearOfStudy = binding.actvEditYearOfStudy.text.toString()

        val body = UpdateProfileBody(
            name,
            bio,
            department,
            yearOfStudy,
            null
        )

        userViewModel.updateProfile(body,TokenHandler.getTokens(this))

    }

    private fun setUpData(){
        Glide
            .with(this)
            .load(UserState.user!!.avatar.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(binding.ivEdProfileUserImage)


        binding.etProfileName.setText(UserState.user!!.name)
        binding.etProfileEmail.setText(UserState.user!!.email)
        binding.etEdProfileBio.setText(UserState.user!!.bio)


        val departmentsAdapter = ArrayAdapter(this, R.layout.list_item, Constants.getDepartmentsList(this))
        binding.actvEditDepartment.setAdapter(departmentsAdapter)


        val yearOfStudyAdapter = ArrayAdapter(this, R.layout.list_item, Constants.getYearOfStudies(this))
        binding.actvEditYearOfStudy.setAdapter(yearOfStudyAdapter)

    }
}