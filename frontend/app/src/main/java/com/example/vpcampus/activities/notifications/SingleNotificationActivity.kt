package com.example.vpcampus.activities.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.api.notification.DeleteNotificationResponse
import com.example.vpcampus.databinding.ActivitySingleNotificationBinding
import com.example.vpcampus.models.Notification
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.FontService
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SingleNotificationActivity : BaseActivity() {

    companion object{

        const val UPDATE_NOTIFICATION_CODE = 200

    }

    private lateinit var binding:ActivitySingleNotificationBinding

    private lateinit var notificationViewModel:NotificationViewModel

    private var mNotification:Notification? = null

   private val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == UPDATE_NOTIFICATION_CODE){
            mNotification = result.data?.getSerializableExtra(Constants.NOTIFICATION) as Notification
            setUpUi(mNotification!!)
            Toast.makeText(this,"Notification updated!",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpFont()

        notificationViewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(NotificationRepository())
        )[NotificationViewModel::class.java]

        
        // observe response
        notificationViewModel.deleteNotificationResponse.observe(this){
            response -> parseDeleteResponse(response)
        }
        
        binding.singleNotificationToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if(intent.hasExtra(Constants.NOTIFICATION)){
            val notification = intent.getSerializableExtra(Constants.NOTIFICATION) as Notification
            mNotification = notification
            setUpUi(notification)
        }

        binding.singleNotificationToolbar.setOnMenuItemClickListener{
            item ->

            when(item.itemId){

                R.id.menu_delete_notification -> {
                    handleDeleteNotification()
                }

                R.id.menu_edit_notification -> {
                    handleUpdateNotification()
                }
            }

            true
        }

    }

    private fun setUpFont(){
        binding.tvNotificationDescription.typeface = FontService.regular(this)
        binding.tvUserName.typeface = FontService.semiBold(this)
        binding.tvUserEmail.typeface = FontService.regular(this)
        binding.tvTitle.typeface = FontService.regular(this)
    }

    private fun parseDeleteResponse(state: ScreenState<DeleteNotificationResponse>) {
        when(state){

            is ScreenState.Loading -> {
                showProgressDialog("Deleting...")
            }

            is ScreenState.Success -> {
                hideProgressDialog()
                finish()
            }

            is ScreenState.Error -> {
                hideProgressDialog()
                showErrorMessage(binding.root, state.message!!)
            }
        }
    }

    private fun handleUpdateNotification(){
        val intent = Intent(this,UpdateNotificationActivity::class.java)
        intent.putExtra(Constants.NOTIFICATION,mNotification)
        getAction.launch(intent)
    }

    private fun handleDeleteNotification(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Notification")
            .setMessage("Do you want to delete notification permanently")
            .setNegativeButton(resources.getString(R.string.account_activation_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.account_activation_dialog_proceed)) { _, _ ->
                notificationViewModel.deleteNotification(TokenHandler.getTokens(this),mNotification?._id!!)
            }
            .show()
    }

    private fun setUpUi(notification:Notification){
        binding.tvTitle.text = notification.title
        binding.tvUserEmail.text = notification.userId.email
        binding.tvUserName.text = notification.userId.name

        binding.tvNotificationDescription.text = HtmlCompat.fromHtml(notification.html,0)

        Glide
            .with(this)
            .load(notification.userId.avatar.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(binding.ivUserAvatar)

    }

}
