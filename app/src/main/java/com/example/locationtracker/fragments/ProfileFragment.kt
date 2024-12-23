package com.example.locationtracker.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.locationtracker.R
import com.example.locationtracker.screens.PrivacyPolicyActivity

class ProfileFragment : Fragment() {

    private lateinit var buttonOpen: LinearLayout
    private lateinit var buttonRateUs: LinearLayout
    private lateinit var buttonChangeName: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        buttonOpen = view.findViewById(R.id.btnLayout)
        buttonRateUs = view.findViewById(R.id.btnRateUs)
        buttonChangeName = view.findViewById(R.id.profileName)

        // Set the click listener to open the privacy screen
        buttonOpen.setOnClickListener {
            goNextScreen()
        }
        buttonChangeName.setOnClickListener {
            showDeviceName()
        }

        // Set the click listener to show the rating dialog
        buttonRateUs.setOnClickListener {
            showRatingDialog()
        }

        return view
    }

    private fun goNextScreen() {
        // Navigate to PrivacyPolicyActivity
        val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
        startActivity(intent)
    }

    private fun showRatingDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rating, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Ensuring the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog
        alertDialog.show()

    }
    private fun showDeviceName() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.device_name_layout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Ensuring the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog
        alertDialog.show()

    }
}
