package com.example.vpcampus.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vpcampus.R
import com.example.vpcampus.activities.notifications.CreateNotificationActivity
import com.example.vpcampus.activities.notifications.SingleNotificationActivity
import com.example.vpcampus.adapters.NotificationsAdapter
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.databinding.FragmentNotificationsBinding
import com.example.vpcampus.models.Notification
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.SocketInstance
import com.example.vpcampus.utils.TokenHandler
import io.socket.client.Socket


class Notifications : Fragment() {

    private lateinit var notificationViewModel:NotificationViewModel

    private lateinit var notificationFragmentBinding:FragmentNotificationsBinding

    private lateinit var inboxNotifications:List<Notification>

    private var mSocket:Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSocket = SocketInstance().getSocket()
        mSocket?.connect()

        mSocket?.emit(Constants.JOIN_NOTIFICATION_ROOM)

        mSocket?.on(Constants.NEW_NOTIFICATION){
            args ->
            if(args[0] != null){
                activity?.runOnUiThread {
                    Toast.makeText(activity,"REcived response",Toast.LENGTH_SHORT).show()
                }
            }
        }

        notificationViewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(NotificationRepository())
        )[NotificationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        notificationFragmentBinding = FragmentNotificationsBinding.inflate(layoutInflater,container,false)

        notificationFragmentBinding.fbCreateNotification.setOnClickListener{
            val intent = Intent(activity,CreateNotificationActivity::class.java)
           startActivity(intent)
        }

        notificationFragmentBinding.toolbarNotification.setNavigationOnClickListener {
            activity?.findViewById<DrawerLayout>(R.id.main_drawer)?.open()
        }

        notificationViewModel.allNotificationsResponse.observe(requireActivity()){
            response -> parseAllNotificationsResponse(response)
        }

        notificationViewModel.getAllNotifications(TokenHandler.getTokens(requireContext()))

        return notificationFragmentBinding.root
    }

    private fun parseAllNotificationsResponse(state: ScreenState<AllNotificationResponse>) {
        when(state){

            is ScreenState.Loading -> {
                notificationFragmentBinding.sflNotifications.visibility = View.VISIBLE
                notificationFragmentBinding.rvNotificationInbox.visibility = View.GONE
                notificationFragmentBinding.sflNotifications.startShimmerAnimation()

            }

            is ScreenState.Success -> {
                notificationFragmentBinding.sflNotifications.visibility = View.GONE
                notificationFragmentBinding.rvNotificationInbox.visibility = View.VISIBLE
                notificationFragmentBinding.sflNotifications.stopShimmerAnimation()

                if(state.data != null){
                    inboxNotifications = state.data.notifications

                    notificationFragmentBinding.rvNotificationInbox.layoutManager =
                       StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)


                    val adapter =  NotificationsAdapter(requireActivity(),
                        inboxNotifications)

                    adapter.setOnClickListener(object : NotificationsAdapter.OnClickListener{
                        override fun onClick(position: Int) {
                            val currentNotification = state.data.notifications[position]
                            val intent = Intent(this@Notifications.context,SingleNotificationActivity::class.java)
                            intent.putExtra(Constants.NOTIFICATION,currentNotification)
                            startActivity(intent)
                        }

                    })

                    notificationFragmentBinding.rvNotificationInbox.adapter = adapter
                }
            }

            is ScreenState.Error -> {
                notificationFragmentBinding.sflNotifications.visibility = View.GONE
                notificationFragmentBinding.rvNotificationInbox.visibility = View.VISIBLE
                notificationFragmentBinding.sflNotifications.stopShimmerAnimation()
                Log.e("ERROR_ALL",state.message!!)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
        mSocket = null
    }

}
