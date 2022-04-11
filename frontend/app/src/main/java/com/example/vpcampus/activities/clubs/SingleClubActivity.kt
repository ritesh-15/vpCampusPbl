package com.example.vpcampus.activities.clubs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vpcampus.adapters.ChatAdapter
import com.example.vpcampus.api.clubs.AllChatsResponse
import com.example.vpcampus.api.clubs.CreateChatBody
import com.example.vpcampus.api.clubs.NewChatResponse
import com.example.vpcampus.databinding.ActivitySingleClubBinding
import com.example.vpcampus.models.Chat
import com.example.vpcampus.models.Club
import com.example.vpcampus.models.ErrorMessage
import com.example.vpcampus.models.User
import com.example.vpcampus.network.factory.ClubsViewModelFactory
import com.example.vpcampus.network.models.ClubsViewModel
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.google.gson.Gson

class SingleClubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleClubBinding
    private lateinit var clubViewModel: ClubsViewModel
    private var club: Club? = null
    private var user: User? = null
    private var adapter: ChatAdapter? = null
    private lateinit var chats: ArrayList<Chat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleClubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up repository and view model
        setUpViewModel()

        // extract club from intent
        extractInformationFromIntent()

        // make get all chats request
        if (club != null) {
            clubViewModel.allChats(clubId = club?._id!!)
        }

        // send button click
        binding.btnSend.setOnClickListener {
            handleSendButtonClick()
        }

        // observe api call response
        observeApiCallResponse()

    }

    private fun handleSendButtonClick() {
        val message: String = binding.tvChatMessage.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(this, "Message not provided!", Toast.LENGTH_SHORT).show()
            return
        }

        clubViewModel.createChat(CreateChatBody(message = message), clubId = club?._id!!)

    }

    private fun observeApiCallResponse() {
        clubViewModel.allChats.observe(this) { response ->
            handleAllChatsResponse(response)
        }

        clubViewModel.createChat.observe(this) { response ->
            handleCreateChatResponse(response)
        }
    }

    private fun handleCreateChatResponse(state: ScreenState<NewChatResponse>) {
        when (state) {

            is ScreenState.Loading -> {
                // TODO show progress bar
            }

            is ScreenState.Success -> {
                // TODO hide progress bar

                if (state.data != null) {
                    chats.add(state.data.chat)
                    adapter?.notifyDataSetChanged()
                }

            }

            is ScreenState.Error -> {
                // TODO hide progress bar
                Log.d("all-chats-error","error")
            }

        }
    }

    private fun handleAllChatsResponse(state: ScreenState<AllChatsResponse>) {
        when (state) {

            is ScreenState.Loading -> {
                // TODO show progress bar
            }

            is ScreenState.Success -> {
                // TODO hide progress bar

                if (state.data != null) {
                    chats = state.data.chats
                    adapter = ChatAdapter(
                        this,
                        chats,
                        user!!
                    )
                    binding.rvSingleClubChats.adapter = adapter
                    binding.rvSingleClubChats.layoutManager = LinearLayoutManager(this)
                }

            }

            is ScreenState.Error -> {
                // TODO hide progress bar
                Log.d("all-chats-error",state.errorBody.toString())
//                if (state.errorBody != null) {
//                    val error = Gson().fromJson(state.errorBody.string(), ErrorMessage::class.java)
//                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
//                }
            }

        }
    }

    private fun extractInformationFromIntent() {
        if (intent.hasExtra(Constants.CLUB)) {
            club = intent.getSerializableExtra(Constants.CLUB) as Club
        }

        if (intent.hasExtra(Constants.USER)) {
            Log.d("single-club-user", intent.getSerializableExtra(Constants.USER).toString())
            user = intent.getSerializableExtra(Constants.USER) as User
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