package com.example.vpcampus.activities


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityMainBinding
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.api.authApi.LogOutResponse
import com.example.vpcampus.fragments.*
import com.example.vpcampus.models.User
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding:ActivityMainBinding

    private lateinit var authViewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(AuthRepository())
        )[AuthViewModel::class.java]

        // auth view model log out observer
        authViewModel.logoutResponse.observe(this){
            response -> parseLogoutResponse(response)
        }

        binding.mainNavigation.setNavigationItemSelectedListener(this)

        replaceFragment(Notifications())

        // bottom navigation item selected
        binding.bnMain.setOnItemSelectedListener {
            item ->

            when(item.itemId){

                R.id.menu_notifications -> {
                    binding.mainNavigation.setCheckedItem(R.id.menu_item_inbox)
                    replaceFragment(Notifications())
                    true
                }

                R.id.menu_clubs -> {
                    replaceFragment(ClubsFragment())
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
    private fun setUpNavHeaderData(user:User){
            Glide
                .with(this)
                .load(user.avatar.url)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .into(findViewById(R.id.iv_nav_user_avatar))

            findViewById<TextView>(R.id.tv_nav_user_name).text =user.name
            findViewById<TextView>(R.id.tv_nav_user_email).text = user.email

    }

    // log out response parser
    private fun parseLogoutResponse(state: ScreenState<LogOutResponse>) {
        when(state){
            is ScreenState.Loading -> {
                showProgressDialog("Logging Out...")
            }

            is ScreenState.Success -> {
                TokenHandler.deleteTokens(this)
                val intent = Intent(this,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }

            is ScreenState.Error -> {
                showErrorMessage(binding.root,"Something went wrong!")
            }
        }
    }

    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().replace(binding.flMain.id, fragment).commit()
    }

    // navigation item selected
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.menu_item_logout -> {
                authViewModel.logout(TokenHandler.getTokens(this))
            }

            R.id.menu_item_sent -> {
                replaceFragment(NotificationSentFragment())
                binding.mainDrawer.close()
            }

            R.id.menu_item_inbox -> {
                replaceFragment(Notifications())
                binding.mainDrawer.close()
            }
        }

        return true
    }


}