package com.example.vpcampus.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotificationSentFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel:NotificationViewModel

    private lateinit var binding:FragmentNotificationSentBinding

    private lateinit var sentNotifications:List<Notification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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


                    val adapter =  NotificationsAdapter(requireActivity(),
                        sentNotifications)

                    adapter.setOnClickListener(object : NotificationsAdapter.OnClickListener{
                        override fun onClick(position: Int) {
                            val currentNotification = state.data.notifications[position]
                            val intent = Intent(this@NotificationSentFragment.context,
                                SingleNotificationActivity::class.java)
                            intent.putExtra(Constants.NOTIFICATION,currentNotification)
                            startActivity(intent)
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationSentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}