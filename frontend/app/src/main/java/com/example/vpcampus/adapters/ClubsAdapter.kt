package com.example.vpcampus.adapters

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.databinding.RvItemClubBinding
import com.example.vpcampus.models.Club
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ClubsAdapter(
    private val context: Context,
    private val list: ArrayList<Club>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: OnClickListener? = null

    fun setOnItemClickListener(listener: OnClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomViewHolder(
            binding = RvItemClubBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            mListener!!
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if(holder is CustomViewHolder){
            // set up ui
            holder.binding.tvClubName.text = model.name
            holder.binding.tvClubDescription.text = model.description
            try {
                val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val date = format.parse(model.createdAt)
                holder.binding.tvCreatedTime.text = date.toString()
            }catch (e:ParseException){
                holder.binding.tvCreatedTime.text = "EXE"
            }


            Glide
                .with(holder.binding.root)
                .load(model.avatar.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(holder.binding.ivClubImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CustomViewHolder(
        val binding: RvItemClubBinding,
        private val listener: OnClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

    }
}