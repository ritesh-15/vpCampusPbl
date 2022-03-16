package com.example.vpcampus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.vpcampus.store.UserState
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

        if(UserState.user != null){

            val etName:TextInputEditText? = view?.findViewById(R.id.et_profile_name)
            val etDepartment:TextInputEditText? = view?.findViewById(R.id.et_profile_department)
            val etEmail:TextInputEditText? = view?.findViewById(R.id.et_profile_email)
            val etYearOfStudy:TextInputEditText? = view?.findViewById(R.id.et_profile_yearofstudy)
            val ivUserImage:ImageView? = view?.findViewById(R.id.iv_profile_user_image)
            val tvName:TextView? = view.findViewById(R.id.tv_profile_name)
            val tvBio:TextView? = view.findViewById(R.id.tv_profile_bio)

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

        return view
    }

    companion object {
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