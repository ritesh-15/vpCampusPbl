package com.example.vpcampus.activities.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vpcampus.R
import com.example.vpcampus.databinding.ActivityActivationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ActivationActivity : AppCompatActivity() {

    private lateinit var binding:ActivityActivationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // choose year btn click
        binding.btnSelectYearOfStudy.setOnClickListener {
            selectYearOfStudyHandler()
        }

    }

    // select year of study handler
    private fun selectYearOfStudyHandler(selectedYearIndex:Int = 0){
        val yearOfStudies = arrayOf("First Year", "Second Year", "Third Year", "Fourth Year")
        val index =   showMenuItems(yearOfStudies)
        Toast.makeText(this, yearOfStudies[index],Toast.LENGTH_SHORT).show()
    }

    // show menu items
    private fun showMenuItems(yearOfStudies:Array<String>,seletedIndex:Int = 0):Int{

        var selectedItemIndex = 0;

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.choose_year_of_study))
            .setPositiveButton(resources.getString(R.string.choose)) { dialog, _ ->
                // Respond to positive button press
                dialog.dismiss()
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(yearOfStudies,seletedIndex) { _, which ->
                selectedItemIndex = which
            }
            .show()

        return selectedItemIndex
    }

    // handle back press here
    override fun onBackPressed() {
        // show custom dialog

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.account_activation_dialog_title))
            .setMessage(resources.getString(R.string.account_activation_dialog_description))
            .setNegativeButton(resources.getString(R.string.account_activation_dialog_cancel)) { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.account_activation_dialog_proceed)) { _, _ ->
                // Respond to positive button press
                super.onBackPressed()
            }
            .show()
    }
}