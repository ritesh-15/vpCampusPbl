package com.example.vpcampus.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vpcampus.ClubsFragment
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityMainBinding
import com.example.vpcampus.Notifications
import com.example.vpcampus.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun replaceFragment(fragment:Fragment){
            supportFragmentManager.beginTransaction().replace(binding.flMain.id, fragment).commit()
    }

}