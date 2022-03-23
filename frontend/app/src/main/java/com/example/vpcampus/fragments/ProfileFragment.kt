package com.example.vpcampus.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.activities.user.EditProfileAcitivty
import com.example.vpcampus.api.userApi.UpdateProfileResponse
import com.example.vpcampus.databinding.FragmentProfileBinding
import com.example.vpcampus.models.User
import com.example.vpcampus.network.factory.UserViewModelFactory
import com.example.vpcampus.network.models.UserViewModel
import com.example.vpcampus.repository.UserRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class ProfileFragment : Fragment() {
    private var binding:FragmentProfileBinding? = null

    private var user:User? = null

    private lateinit var userViewModel:UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository())
        )[UserViewModel::class.java]

        // edit icon listener
        binding?.fbEditProfile?.setOnClickListener {
            val intent = Intent(activity,EditProfileAcitivty::class.java)
            intent.putExtra(Constants.USER,user!!)
            startActivityForResult(intent, UPDATE_PROFILE_RESULT_CODE)
        }

        if(user == null){
            userViewModel.getUserDetails(TokenHandler.getTokens(requireActivity()))
        }

        userViewModel.userDetailsResponse.observe(viewLifecycleOwner){
                response -> parseUserDetailsResponse(response)
        }


        return binding?.root
    }


    // handle loading
    private fun handleSkeletonLoading(show:Boolean){
        if(show){
            binding?.mainProfileContainer?.visibility = View.GONE
            binding?.profileSkeleton?.visibility = View.VISIBLE
            binding?.profileSkeleton?.startShimmerAnimation()
        }else{
            binding?.mainProfileContainer?.visibility = View.VISIBLE
            binding?.profileSkeleton?.visibility = View.GONE
            binding?.profileSkeleton?.stopShimmerAnimation()
        }
    }

    private fun parseUserDetailsResponse(state: ScreenState<UpdateProfileResponse>) {
        when(state){

            is ScreenState.Loading -> {
                handleSkeletonLoading(true)
            }

            is ScreenState.Success -> {
                handleSkeletonLoading(false)
                if(state.data != null){
                    user = state.data.user
                    setUpUiData(user!!)
                }
            }

            is ScreenState.Error -> {
                handleSkeletonLoading(false)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_PROFILE_RESULT_CODE && resultCode == Activity.RESULT_OK){
            val  updatedUser = data?.getSerializableExtra(Constants.USER) as User
            setUpUiData(updatedUser)
        }
    }

    private fun setUpUiData(user:User){
            binding?.etProfileName?.setText(user.name)
            binding?.etProfileDepartment?.setText(user.department)
            binding?.etProfileYearofstudy?.setText(user.yearOfStudy)
            binding?.etProfileEmail?.setText(user.email)
            binding?.tvProfileName?.text = user.name
            binding?.tvProfileBio?.text = user.bio

            Glide
                .with(this)
                .load(user.avatar.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(binding?.ivProfileUserImage!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val UPDATE_PROFILE_RESULT_CODE:Int = 200
    }
}