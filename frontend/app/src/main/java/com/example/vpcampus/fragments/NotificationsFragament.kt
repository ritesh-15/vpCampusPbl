package com.example.vpcampus.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
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
import com.facebook.shimmer.ShimmerFrameLayout


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Notifications : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var notificationViewModel:NotificationViewModel

    private lateinit var notificationFragmentBinding:FragmentNotificationsBinding

    private lateinit var inboxNotifications:List<Notification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        SocketInstance().setSocket()
        SocketInstance().connect()
        val mSocket = SocketInstance().getSocket()

        mSocket?.emit("join")

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
           startActivity(Intent(activity,CreateNotificationActivity::class.java))
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Notifications().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
