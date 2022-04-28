package com.example.vpcampus.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.activities.clubs.SingleClubActivity
import com.example.vpcampus.api.clubs.CreateClubResponse
import com.example.vpcampus.api.clubs.DeleteClubResponse
import com.example.vpcampus.api.clubs.UpdateClubBody
import com.example.vpcampus.databinding.FragmentClubInformationBinding
import com.example.vpcampus.models.Club
import com.example.vpcampus.models.User
import com.example.vpcampus.network.factory.ClubsViewModelFactory
import com.example.vpcampus.network.models.ClubsViewModel
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ClubInformationFragment : Fragment() {
    private lateinit var clubViewModel: ClubsViewModel
    private lateinit var binding: FragmentClubInformationBinding
    private var club: Club? = null
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentClubInformationBinding.inflate(layoutInflater, container, false)
        setUpViewModel()

        user = arguments?.getSerializable(Constants.USER) as User
        club = arguments?.getSerializable(Constants.CLUB) as Club

        binding.btnDeleteClub.setOnClickListener {
            handleDeleteClub()
        }

        clubViewModel.deleteClub.observe(requireActivity()) {
            handleDeleteClubResponse(it)
        }

        binding.btnUpdate.setOnClickListener {
            handleClubUpdate()
        }

        clubViewModel.updateClub.observe(requireActivity()){
            handleUpdateClubResponse(it)
        }

        handleClubView()

        return binding.root
    }

    private fun handleClubUpdate() {
        val name = binding.etClubName.text.toString()
        val description = binding.etClubDescription.text.toString()

        if(name.isEmpty() || description.isEmpty())
        {
            Toast.makeText(context, "Name and description is required!", Toast.LENGTH_SHORT).show()
            return
        }

        val body = UpdateClubBody(name = name,
            description = description,
            avatar = null
        )

        clubViewModel.updateClub(club!!._id, body)

    }

    private fun handleDeleteClubResponse(state: ScreenState<DeleteClubResponse>) {
        val activity = activity as SingleClubActivity
        when (state) {

            is ScreenState.Loading -> {
                activity.showProgressDialog()
            }

            is ScreenState.Error -> {
                activity.hideProgressDialog()
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

            is ScreenState.Success -> {
                activity.hideProgressDialog()
                Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                activity.finish()
            }

        }
    }

    private fun handleUpdateClubResponse(state: ScreenState<CreateClubResponse>) {
        val activity = activity as SingleClubActivity
        when (state) {

            is ScreenState.Loading -> {
                activity.showProgressDialog()
            }

            is ScreenState.Error -> {
                activity.hideProgressDialog()
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

            is ScreenState.Success -> {
                activity.hideProgressDialog()
                if (state.data != null){
                    club = state.data.club
                    Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun handleDeleteClub() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Club")
            .setMessage("Do you want to delete club permanently!")
            .setNegativeButton(resources.getString(R.string.account_activation_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.account_activation_dialog_proceed)) { _, _ ->
                clubViewModel.deleteClub(club?._id!!)
            }
            .show()
    }

    private fun handleClubView() {
        if (user?._id == club?.admin?._id) {
            binding.llAdmin.visibility = View.VISIBLE
            binding.llClub.visibility = View.GONE
            binding.llDeleteBtn.visibility = View.VISIBLE
            setUpAdminUi()
            return
        }


        setUpClubUi()
    }

    private fun setUpClubUi() {
        if (club != null) {
            binding.tvClubName.text = club?.name
            binding.tvClubDescription.text = club?.description

            Glide
                .with(this)
                .load(club?.avatar?.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(binding.ivClubImageView)

        }
    }

    private fun setUpAdminUi() {
        if (club != null) {
            binding.etClubName.setText(club?.name)
            binding.etClubDescription.setText(club?.description)

            Glide
                .with(this)
                .load(club?.avatar?.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(binding.ivClubImage)

        }
    }

    private fun setUpViewModel() {
        val repository = ClubRepository()
        val factory = ClubsViewModelFactory(repository)
        clubViewModel = ViewModelProvider(
            this,
            factory
        )[ClubsViewModel::class.java]
    }

}