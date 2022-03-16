package com.example.vpcampus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vpcampus.activities.notifications.CreateNotificationActivity
import com.example.vpcampus.adapters.NotificationsAdapter
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Notifications.newInstance] factory method to
 * create an instance of this fragment.
 */
class Notifications : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var notificationViewModel:NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        notificationViewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(NotificationRepository())
        ).get(NotificationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_notifications, container, false);

        val createBtn = view.findViewById<ExtendedFloatingActionButton>(R.id.fb_create_notification)

        createBtn.setOnClickListener{
           startActivity(Intent(activity,CreateNotificationActivity::class.java))
        }

        notificationViewModel.getAllNotifications(TokenHandler.getTokens(requireActivity()))


        notificationViewModel.allNotificationsResponse.observe(requireActivity()){
            response -> parseAllNotificationsResponse(response)
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun parseAllNotificationsResponse(state: ScreenState<AllNotificationResponse>) {
        when(state){

            is ScreenState.Loading -> {
                view?.findViewById<LinearLayout>(R.id.pb_notifications)?.visibility = View.VISIBLE
                view?.findViewById<ScrollView>(R.id.sv_notifications)?.visibility = View.GONE
            }

            is ScreenState.Success -> {
                view?.findViewById<LinearLayout>(R.id.pb_notifications)?.visibility = View.GONE
                view?.findViewById<ScrollView>(R.id.sv_notifications)?.visibility = View.VISIBLE
                if(state.data != null){
                    val rvNotifications = view?.findViewById<RecyclerView>(R.id.rv_notification)
                    rvNotifications?.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
                    rvNotifications?.adapter = NotificationsAdapter(requireActivity(),
                        state.data.notifications)
                }
            }

            is ScreenState.Error -> {
                view?.findViewById<LinearLayout>(R.id.pb_notifications)?.visibility = View.GONE
                view?.findViewById<ScrollView>(R.id.sv_notifications)?.visibility = View.VISIBLE
                Log.e("ERROR_ALL",state.message!!)
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Notifications.
         */
        // TODO: Rename and change types and number of parameters
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
