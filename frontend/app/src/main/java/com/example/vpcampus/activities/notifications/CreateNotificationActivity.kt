package com.example.vpcampus.activities.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.notification.CreateNotificationResponse
import com.example.vpcampus.databinding.ActivityCreateNotificationBinding
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler

class CreateNotificationActivity : BaseActivity() {

    private lateinit var binding:ActivityCreateNotificationBinding

    private lateinit var notificationViewModel:NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationViewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(NotificationRepository())
        ).get(NotificationViewModel::class.java)

        binding.newNotificationToolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        binding.btnCreate.setOnClickListener {
            handleCreateButtonClick()
        }

        notificationViewModel.createNotificationResponse.observe(this){
            response -> parseCreateNotificationResponse(response)
        }

    }

    private fun parseCreateNotificationResponse(state: ScreenState<CreateNotificationResponse>) {

        when(state){

            is ScreenState.Loading -> {
                showProgressDialog()
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                if(state.data != null){
                    Toast.makeText(this,"Notification created successfully!",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                showErrorMessage(binding.root,state.message!!)
            }

        }

    }

    private fun handleCreateButtonClick() {
        val title:String = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        if(!validate(title, description)){
            showErrorMessage(binding.root,"Title and description is required!")
            return
        }

        notificationViewModel.createNewNotification(title,description,TokenHandler.getTokens(this))

    }

    private fun validate(title:String,description:String):Boolean{
        if(title.isEmpty() && description.isEmpty())
            return false
        return true
    }
}