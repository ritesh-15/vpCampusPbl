package com.example.vpcampus.fragments

import android.media.MediaParser
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.activities.clubs.SingleClubActivity
import com.example.vpcampus.adapters.ChatAdapter
import com.example.vpcampus.api.clubs.AllChatsResponse
import com.example.vpcampus.api.clubs.CreateChatBody
import com.example.vpcampus.api.clubs.NewChatResponse
import com.example.vpcampus.databinding.FragmentClubChatsBinding
import com.example.vpcampus.models.Chat
import com.example.vpcampus.models.Club
import com.example.vpcampus.models.User
import com.example.vpcampus.network.factory.ClubsViewModelFactory
import com.example.vpcampus.network.models.ClubsViewModel
import com.example.vpcampus.repository.ClubRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.PushNotificationManager
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.SocketInstance
import com.google.gson.Gson
import io.socket.client.Socket


class ClubChatsFragment : Fragment() {
    private lateinit var clubViewModel: ClubsViewModel
    private var club: Club? = null
    private var user: User? = null
    private var adapter: ChatAdapter? = null
    private lateinit var chats: ArrayList<Chat>
    private lateinit var binding: FragmentClubChatsBinding
    private var mSocket: Socket? = null
    private var mPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentClubChatsBinding.inflate(layoutInflater, container, false)

        user = arguments?.getSerializable(Constants.USER) as User
        club = arguments?.getSerializable(Constants.CLUB) as Club

        mSocket = SocketInstance().getSocket()
        mSocket?.connect()

        mSocket?.on("chat-message") { args ->
            if (args[0] != null) {
                activity?.runOnUiThread {
                    val chat = Gson().fromJson(args[0].toString(), Chat::class.java)
                    chats.add(chat)
                    if (mPlayer == null) {
                        mPlayer = MediaPlayer.create(context, R.raw.notification).apply {
                            start()
                        }
                    } else {
                        mPlayer?.start()
                    }
                    adapter?.notifyDataSetChanged()

                }
            }
        }

        setupClubUi()

        setUpViewModel()

        clubViewModel.allChats(clubId = club?._id!!)

        observeApiCallResponse()

        binding.btnSend.setOnClickListener {
            handleSendButtonClick()
        }

        binding.ivClubImage.setOnClickListener {
            val activity = activity as SingleClubActivity
            val fragment = ClubInformationFragment()
            val bundle = Bundle()
            bundle.putSerializable(Constants.USER, user)
            bundle.putSerializable(Constants.CLUB, club)
            fragment.arguments = bundle
            activity.replaceFragment(fragment, true)
        }

        return binding.root
    }

    private fun setupClubUi() {
        binding.tvClubName.text = club?.name
        binding.tvClubDescription.text = club?.description

        Glide
            .with(this)
            .load(club?.avatar?.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(binding.ivClubImage)

    }

    private fun handleSendButtonClick() {
        val message: String = binding.tvChatMessage.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(context, "Message not provided!", Toast.LENGTH_SHORT).show()
            return
        }

        clubViewModel.createChat(CreateChatBody(message = message), clubId = club?._id!!)

    }

    private fun observeApiCallResponse() {
        clubViewModel.allChats.observe(requireActivity()) { response ->
            handleAllChatsResponse(response)
        }

        clubViewModel.createChat.observe(requireActivity()) { response ->
            handleCreateChatResponse(response)
        }
    }

    private fun handleCreateChatResponse(state: ScreenState<NewChatResponse>) {
        val activity = activity as SingleClubActivity

        when (state) {

            is ScreenState.Loading -> {
                activity.showProgressDialog("Sending...")
            }

            is ScreenState.Success -> {
                activity.hideProgressDialog()
                if (state.data != null) {
                    val mChat = Gson().toJson(state.data.chat)
                    mSocket?.emit("chat-message", mChat)
                    chats.add(state.data.chat)
                    adapter?.notifyDataSetChanged()
                    clearMessageInput()
                }
            }

            is ScreenState.Error -> {
                activity.hideProgressDialog()
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun handleChatLoadingProgressBar(loading: Boolean) {
        if (loading) {
            binding.rvSingleClubChats.visibility = View.GONE
            binding.chatLoadingProgressbar.visibility = View.VISIBLE
            return
        }

        binding.rvSingleClubChats.visibility = View.VISIBLE
        binding.chatLoadingProgressbar.visibility = View.GONE
    }

    private fun clearMessageInput() {
        binding.tvChatMessage.text.clear()
    }

    private fun handleAllChatsResponse(state: ScreenState<AllChatsResponse>) {
        when (state) {

            is ScreenState.Loading -> {
                handleChatLoadingProgressBar(true)
            }

            is ScreenState.Success -> {
                handleChatLoadingProgressBar(false)
                if (state.data != null) {
                    chats = state.data.chats
                    adapter = ChatAdapter(requireContext(), chats, user!!)
                    binding.rvSingleClubChats.adapter = adapter
                    binding.rvSingleClubChats.layoutManager = LinearLayoutManager(context)
                    binding.rvSingleClubChats.scrollToPosition(chats.size - 1)
                }
            }

            is ScreenState.Error -> {
                handleChatLoadingProgressBar(false)
                Log.d("all-chats-error", state.message!!)
            }

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

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }


}