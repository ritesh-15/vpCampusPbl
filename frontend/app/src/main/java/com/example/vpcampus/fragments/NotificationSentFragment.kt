package com.example.vpcampus.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vpcampus.activities.notifications.SingleNotificationActivity
import com.example.vpcampus.adapters.NotificationsAdapter
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.databinding.FragmentNotificationSentBinding
import com.example.vpcampus.models.Notification
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler


class NotificationSentFragment : Fragment() {


    private lateinit var viewModel:NotificationViewModel

    private lateinit var binding:FragmentNotificationSentBinding

    private var adapter:NotificationsAdapter? = null

    private lateinit var sentNotifications:ArrayList<Notification>

    @SuppressLint("NotifyDataSetChanged")
    private var singleNotificationAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == DELETE_NOTIFICATION_CODE){
            val deletedNotification = result.data?.getSerializableExtra(Constants.NOTIFICATION) as Notification
            Log.d("Delete_notification",deletedNotification.toString())
            sentNotifications.remove(deletedNotification)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        viewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(NotificationRepository())
        )[NotificationViewModel::class.java]

        binding = FragmentNotificationSentBinding.inflate(layoutInflater,container,false)

        viewModel.getAllNotifications(TokenHandler.getTokens(requireActivity()),"true")

        viewModel.allNotificationsResponse.observe(requireActivity()){
            response -> parseGetAllSentResponse(response)
        }

        return binding.root
    }

    private fun parseGetAllSentResponse(state: ScreenState<AllNotificationResponse>) {
        when(state){

            is ScreenState.Loading -> {
                binding.sflNotifications.visibility = View.VISIBLE
                binding.rvNotificationSent.visibility = View.GONE
                binding.sflNotifications.startShimmerAnimation()
            }

            is ScreenState.Success -> {
                binding.sflNotifications.visibility = View.GONE
                binding.rvNotificationSent.visibility = View.VISIBLE
                binding.sflNotifications.stopShimmerAnimation()

                if(state.data != null){
                    sentNotifications = state.data.notifications

                    binding.rvNotificationSent.layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)


                    adapter =  NotificationsAdapter(requireActivity(),
                        sentNotifications)

                    adapter?.setOnClickListener(object : NotificationsAdapter.OnClickListener{
                        override fun onClick(position: Int) {
                            val currentNotification = state.data.notifications[position]
                            val intent = Intent(this@NotificationSentFragment.context,
                                SingleNotificationActivity::class.java)
                            intent.putExtra(Constants.NOTIFICATION,currentNotification)
                            singleNotificationAction.launch(intent)
                        }

                    })

                    binding.rvNotificationSent.adapter = adapter
                }

            }

            is ScreenState.Error -> {
                binding.sflNotifications.visibility = View.GONE
                binding.rvNotificationSent.visibility = View.VISIBLE
                binding.sflNotifications.stopShimmerAnimation()
            }

        }
    }

    companion object {
        const val DELETE_NOTIFICATION_CODE = 200
        const val UPDATE_NOTIFICATION_CODE = 201
    }
}