package com.example.vpcampus.activities.clubs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.vpcampus.R
import com.example.vpcampus.activities.BaseActivity
import com.example.vpcampus.databinding.ActivitySingleClubBinding
import com.example.vpcampus.fragments.ClubChatsFragment
import com.example.vpcampus.models.Club
import com.example.vpcampus.models.User
import com.example.vpcampus.utils.Constants
import com.example.vpcampus.utils.SocketInstance
import io.socket.client.Socket

class SingleClubActivity : BaseActivity() {

    private lateinit var binding: ActivitySingleClubBinding
    private var user: User? = null
    private var club: Club? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleClubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        extractFromIntent()

        SocketInstance().getSocket()?.emit("join-club-room",club!!._id)

        val bundle = Bundle()
        bundle.putSerializable(Constants.USER, user)
        bundle.putSerializable(Constants.CLUB, club)

        val chatsFragment = ClubChatsFragment()
        chatsFragment.arguments = bundle
        replaceFragment(chatsFragment)
    }


    private fun extractFromIntent() {
        if (intent.hasExtra(Constants.USER)) {
            user = intent.getSerializableExtra(Constants.USER) as User
        }

        if (intent.hasExtra(Constants.CLUB)) {
            club = intent.getSerializableExtra(Constants.CLUB) as Club
        }

    }

    fun replaceFragment(fragment: Fragment, addToBackStack:Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flSingleClub.id, fragment)
            if(addToBackStack){
                addToBackStack(null)
            }
            commit()
        }
    }

}