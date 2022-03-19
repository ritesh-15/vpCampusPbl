package com.example.vpcampus.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.activities.user.EditProfileAcitivty
import com.example.vpcampus.databinding.FragmentProfileBinding
import com.example.vpcampus.store.UserState
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var binding:FragmentProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)

        // edit icon listener
        binding?.fbEditProfile?.setOnClickListener {
            val intent = Intent(activity,EditProfileAcitivty::class.java)
            startActivityForResult(intent, UPDATE_PROFILE_RESULT_CODE)
        }

        setUpUiData()


        return binding?.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UPDATE_PROFILE_RESULT_CODE && resultCode == Activity.RESULT_OK){
            setUpUiData()
        }
    }

    private fun setUpUiData(){
        if(UserState.user != null){
            binding?.etProfileName?.setText(UserState.user!!.name)
            binding?.etProfileDepartment?.setText(UserState.user!!.department)
            binding?.etProfileYearofstudy?.setText(UserState.user!!.yearOfStudy)
            binding?.etProfileEmail?.setText(UserState.user!!.email)
            binding?.tvProfileName?.text = UserState.user!!.name
            binding?.tvProfileBio?.text = UserState.user!!.bio

            Glide
                .with(this)
                .load(UserState.user!!.avatar.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(binding?.ivProfileUserImage!!)

        }

    }

    companion object {

        const val UPDATE_PROFILE_RESULT_CODE:Int = 200

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}