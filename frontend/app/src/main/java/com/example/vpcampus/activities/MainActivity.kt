package com.example.vpcampus.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityMainBinding
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.activities.notifications.SingleNotificationActivity
import com.example.vpcampus.adapters.NotificationsAdapter
import com.example.vpcampus.api.authApi.LogOutResponse
import com.example.vpcampus.api.notification.AllNotificationResponse
import com.example.vpcampus.databinding.NavDrawerHeaderLayoutBinding
import com.example.vpcampus.fragments.*
import com.example.vpcampus.models.Notification
import com.example.vpcampus.models.User
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.factory.NotificationViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.network.models.NotificationViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.repository.NotificationRepository
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.SocketInstance
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.navigation.NavigationView
import io.socket.client.Socket

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var authViewModel: AuthViewModel

    private var user: User? = null

    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.USER)) {
            user = intent.getSerializableExtra(Constants.USER) as User
            setUpNavHeaderData(user)
        }

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(AuthRepository())
        )[AuthViewModel::class.java]

        // auth view model log out observer
        authViewModel.logoutResponse.observe(this) { response ->
            parseLogoutResponse(response)
        }

        mSocket = SocketInstance().getSocket()
        SocketInstance().connect()
        mSocket?.emit(Constants.JOIN_NOTIFICATION_ROOM, user?._id)

        binding.mainNavigation.setNavigationItemSelectedListener(this)

        replaceFragment(Notifications())

        // bottom navigation item selected
        binding.bnMain.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.menu_notifications -> {
                    binding.mainNavigation.setCheckedItem(R.id.menu_item_inbox)
                    replaceFragment(Notifications())
                    true
                }

                R.id.menu_clubs -> {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.USER, user)
                    val fragment = ClubsFragment()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                    true
                }

                R.id.menu_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> {
                    replaceFragment(Notifications())
                    true
                }

            }
        }

    }

    // set up nav header data
    private fun setUpNavHeaderData(user: User?) {

        val headerVieww = binding.mainNavigation.getHeaderView(0)
        val navBarBinding = NavDrawerHeaderLayoutBinding.bind(headerVieww)

        Glide
            .with(this)
            .load(user?.avatar?.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(navBarBinding.ivNavUserAvatar)

        navBarBinding.tvNavUserName.text = user?.name
        navBarBinding.tvNavUserEmail.text = user?.email

    }

    // log out response parser
    private fun parseLogoutResponse(state: ScreenState<LogOutResponse>) {
        when (state) {
            is ScreenState.Loading -> {
                showProgressDialog("Logging Out...")
            }

            is ScreenState.Success -> {
                TokenHandler.deleteTokens(this)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }

            is ScreenState.Error -> {
                showErrorMessage(binding.root, "Something went wrong!")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.flMain.id, fragment)
            .commit()
    }

    // navigation item selected
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.menu_item_logout -> {
                authViewModel.logout(TokenHandler.getTokens(this))
            }

            R.id.menu_item_sent -> {
                replaceFragment(NotificationSentFragment())
                binding.bnMain.menu.findItem(R.id.menu_notifications).isChecked = true
                binding.mainDrawer.close()
            }

            R.id.menu_item_inbox -> {
                replaceFragment(Notifications())
                binding.bnMain.menu.findItem(R.id.menu_notifications).isChecked = true
                binding.mainDrawer.close()
            }
        }

        return true
    }


}