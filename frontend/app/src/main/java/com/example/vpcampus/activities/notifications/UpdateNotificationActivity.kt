package com.example.vpcampus.activities.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.notification.CreateNotificationResponse
import com.example.vpcampus.api.notification.NotificationBody
import com.example.vpcampus.databinding.ActivityUpdateNotificationBinding
import com.example.vpcampus.models.Notification
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class UpdateNotificationActivity : BaseActivity() {

    private lateinit var binding:ActivityUpdateNotificationBinding

    private lateinit var notificationViewModel:NotificationViewModel

    private var mNotification:Notification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationViewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(NotificationRepository())
        )[NotificationViewModel::class.java]

        if(intent.hasExtra(Constants.NOTIFICATION)){
            mNotification = intent.getSerializableExtra(Constants.NOTIFICATION) as Notification
            setUpUiData()
        }

        binding.btnUpdate.setOnClickListener {
            handleUpdateBtnClick()
        }

        notificationViewModel.updateNotificationResponse.observe(this){
            response -> parseUpdateResponse(response)
        }
    }

    private fun parseUpdateResponse(state: ScreenState<CreateNotificationResponse>) {
        when(state){
            is ScreenState.Loading -> {
                showProgressDialog("Updating notification...")
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                if(state.data != null){
                    val intent = Intent()
                    intent.putExtra(Constants.NOTIFICATION,state.data.notification)
                    setResult(SingleNotificationActivity.UPDATE_NOTIFICATION_CODE,intent)
                    finish()
                }
            }

            is ScreenState.Error -> {
                hideProgressDialog()
            }
        }
    }

    private fun handleUpdateBtnClick() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        if(mNotification?.title == title && description == mNotification?.description){
            showErrorMessage(binding.root,"Changes not found!")
            return
        }

        notificationViewModel.updateNotification(TokenHandler.getTokens(this),
            mNotification?._id!!,
            NotificationBody(title,description)
            )

    }

    private fun setUpUiData(){
        if(mNotification != null){
            binding.etDescription.setText(mNotification?.description)
            binding.etTitle.setText(mNotification?.title)
        }
    }


}