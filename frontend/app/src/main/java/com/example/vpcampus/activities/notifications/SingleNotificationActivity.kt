package com.example.vpcampus.activities.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivitySingleNotificationBinding
import com.example.vpcampus.models.Notification
import com.example.vpcampus.utils.Constants

class SingleNotificationActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySingleNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singleNotificationToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if(intent.hasExtra(Constants.NOTIFICATION)){
            val notification = intent.getSerializableExtra(Constants.NOTIFICATION) as Notification
            setUpUi(notification)
        }

    }

    private fun setUpUi(notification:Notification){
        binding.tvTitle.text = notification.title
        binding.tvUserEmail.text = notification.userId.email
        binding.tvUserName.text = notification.userId.name

        binding.tvNotificationDescription.text = notification.description

        Glide
            .with(this)
            .load(notification.userId.avatar.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(binding.ivUserAvatar)

    }
}