package com.example.vpcampus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vpcampus.databinding.RvChatRecivedItemBinding
import com.example.vpcampus.databinding.RvChatSentItemBinding
import com.example.vpcampus.models.Chat
import com.example.vpcampus.models.User


class ChatAdapter(
    private val context: Context,
    private val list: ArrayList<Chat>,
    private val currentUser: User,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val CHAT_SENT_CODE = 1
        private const val CHAT_RECEVIED_CODE = 2

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHAT_SENT_CODE -> {
                ChatSentViewHolder(
                    RvChatSentItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            CHAT_RECEVIED_CODE -> {
                ChatReceivedViewHolder(
                    RvChatRecivedItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                ChatReceivedViewHolder(
                    RvChatRecivedItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val model = list[position]

        if (model.userId._id == currentUser._id) {
            return CHAT_SENT_CODE
        }

        return CHAT_RECEVIED_CODE

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ChatSentViewHolder) {
            holder.binding.tvChatMessage.text = model.message
            holder.binding.tvUserName.text = model.userId.name
        } else if (holder is ChatReceivedViewHolder) {
            holder.binding.tvChatMessage.text = model.message
            holder.binding.tvUserName.text = model.userId.name
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class ChatSentViewHolder(
        val binding: RvChatSentItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    private class ChatReceivedViewHolder(
        val binding: RvChatRecivedItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

    }

}