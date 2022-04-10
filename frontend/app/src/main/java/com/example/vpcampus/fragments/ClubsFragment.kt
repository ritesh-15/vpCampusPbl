package com.example.vpcampus.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vpcampus.R
import com.example.vpcampus.activities.clubs.CreateNewClub
import com.example.vpcampus.activities.clubs.SingleClubActivity
import com.example.vpcampus.adapters.ClubsAdapter
import com.example.vpcampus.api.clubs.GetAllClubsResponse
import com.example.vpcampus.databinding.FragmentClubsBinding
import com.example.vpcampus.models.Club
import com.example.vpcampus.models.ErrorMessage
import com.example.vpcampus.network.factory.ClubsViewModelFactory
import com.example.vpcampus.network.models.ClubsViewModel
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.google.gson.Gson

class ClubsFragment : Fragment() {

    private lateinit var binding: FragmentClubsBinding
    private lateinit var clubViewModel: ClubsViewModel
    private var clubsAdapter: ClubsAdapter? = null

    private val activityAction =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentClubsBinding.inflate(layoutInflater, container, false)

        binding.fbCreateNewClub.setOnClickListener {
            val intent = Intent(activity, CreateNewClub::class.java)
            activityAction.launch(intent)
        }

        // set up view mode
        setUpViewModel()

        // all clubs api call
        clubViewModel.getAllClubs()

        // observe api call response
        observeApiCallResponse()

        return binding.root
    }

    private fun observeApiCallResponse() {
        clubViewModel.allClubs.observe(viewLifecycleOwner) { response ->
            handleAllClubsResponse(response)
        }
    }

    private fun handleAllClubsResponse(state: ScreenState<GetAllClubsResponse>) {
        when (state) {

            is ScreenState.Loading -> {
                showProgressBar(true)
            }

            is ScreenState.Success -> {
                showProgressBar(false)
                if (state.data != null) {
                    clubsAdapter = ClubsAdapter(requireActivity(), state.data.clubs)

                    clubsAdapter?.setOnItemClickListener(object : ClubsAdapter.OnClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(activity,SingleClubActivity::class.java)
                            intent.putExtra(Constants.CLUB, state.data.clubs)
                            startActivity(intent)
                        }

                    })

                    binding.rvClubs.adapter = clubsAdapter
                    binding.rvClubs.layoutManager = LinearLayoutManager(context)
                }
            }

            is ScreenState.Error -> {
                showProgressBar(false)
                if (state.errorBody != null) {
                    val error =
                        Gson().fromJson(state.errorBody.toString(), ErrorMessage::class.java)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun showProgressBar(loading: Boolean) {
        if (loading) {
            binding.pbClubs.visibility = View.VISIBLE
            binding.rvClubs.visibility = View.GONE
        } else {
            binding.pbClubs.visibility = View.GONE
            binding.rvClubs.visibility = View.VISIBLE
        }
    }

    private fun setUpViewModel() {
        val repository = ClubRepository()
        val factory = ClubsViewModelFactory(repository)
        clubViewModel = ViewModelProvider(this, factory)[ClubsViewModel::class.java]
    }

    companion object {
        const val NEW_CLUB_CREATED = 201
    }


}