package com.example.vpcampus.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.ScrollCaptureCallback
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vpcampus.ClubsFragment
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityMainBinding
import com.example.vpcampus.Notifications
import com.example.vpcampus.ProfileFragment
import com.example.vpcampus.activities.auth.LoginActivity
import com.example.vpcampus.api.authApi.LogOutResponse
import com.example.vpcampus.databinding.NavDrawerHeaderLayoutBinding
import com.example.vpcampus.network.factory.AuthViewModelFactory
import com.example.vpcampus.network.models.AuthViewModel
import com.example.vpcampus.repository.AuthRepository
import com.example.vpcampus.store.UserState
import com.example.vpcampus.utils.ScreenState
import com.example.vpcampus.utils.TokenHandler
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding:ActivityMainBinding

    private lateinit var navHeaderBinding:NavDrawerHeaderLayoutBinding

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

        // navbar header layout binding
        navHeaderBinding = NavDrawerHeaderLayoutBinding.inflate(layoutInflater)
        navHeaderBinding.tvUserEmail.text = UserState.user!!.email
        navHeaderBinding.tvUserName.text = UserState.user!!.name

        Glide
            .with(this)
            .load(UserState.user!!.avatar.url)
            .centerCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .into(navHeaderBinding.ivUserAvatar)


        binding.mainNavigation.setNavigationItemSelectedListener(this)

        replaceFragment(Notifications())

        binding.bnMain.setOnItemSelectedListener {
            item ->

            when(item.itemId){

                R.id.menu_notifications -> {
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

    // log out response parser
    private fun parseLogoutResponse(state: ScreenState<LogOutResponse>) {
        when(state){
            is ScreenState.Loading -> {
                showProgressDialog("Logging Out...")
            }

            is ScreenState.Success -> {
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

    // navigation item listener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this,"Selected",Toast.LENGTH_SHORT).show()
        when(item.itemId){
            R.id.menu_item_logout -> {
                authViewModel.logout(TokenHandler.getTokens(this))

            }
        }
        return true
    }

    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().replace(binding.flMain.id, fragment).commit()
    }

}