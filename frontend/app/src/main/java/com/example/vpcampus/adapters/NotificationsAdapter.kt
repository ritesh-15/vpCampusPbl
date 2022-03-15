package com.example.vpcampus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.databinding.RvItemNotificationBinding
import com.example.vpcampus.models.Notification

class NotificationsAdapter(
    private val context:Context,
    private var list:List<Notification>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(RvItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if(holder is MyViewHolder){

            holder.binding.tvNotificationDescription.text = model.description
            holder.binding.tvNotificationTitle.text = model.title

            Glide
                .with(holder.binding.root)
                .load(model.userId.avatar.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(holder.binding.imNotificationImage)

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(binding:RvItemNotificationBinding):RecyclerView.ViewHolder(binding.root){
        val binding = binding

    }
}