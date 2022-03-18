package com.example.vpcampus

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
import com.example.vpcampus.activities.user.EditProfileAcitivty
import com.example.vpcampus.store.UserState
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // edit icon listern
        view?.findViewById<FloatingActionButton>(R.id.fb_edit_profile)?.setOnClickListener {
            val intent = Intent(activity,EditProfileAcitivty::class.java)
            startActivityForResult(intent, UPDATE_PROFILE_RESULT_CODE)
        }

        setUpUiData(view)


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UPDATE_PROFILE_RESULT_CODE && resultCode == Activity.RESULT_OK){
            setUpUiData(requireView())
            Log.d("update_result",UserState.user!!.toString())
        }
    }

    private fun setUpUiData(view:View){
        if(UserState.user != null){

            val etName:TextInputEditText? = view.findViewById(R.id.et_profile_name)
            val etDepartment:TextInputEditText? = view?.findViewById(R.id.et_profile_department)
            val etEmail:TextInputEditText? = view?.findViewById(R.id.et_profile_email)
            val etYearOfStudy:TextInputEditText? = view?.findViewById(R.id.et_profile_yearofstudy)
            val ivUserImage:ImageView? = view?.findViewById(R.id.iv_profile_user_image)
            val tvName:TextView? = view?.findViewById(R.id.tv_profile_name)
            val tvBio:TextView? = view?.findViewById(R.id.tv_profile_bio)

            etName?.setText(UserState.user!!.name)
            etDepartment?.setText(UserState.user!!.department)
            etYearOfStudy?.setText(UserState.user!!.yearOfStudy)
            etEmail?.setText(UserState.user!!.email)
            tvName?.text = UserState.user!!.name
            tvBio?.text = UserState.user!!.bio

            Glide
                .with(this)
                .load(UserState.user!!.avatar.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(ivUserImage!!)

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