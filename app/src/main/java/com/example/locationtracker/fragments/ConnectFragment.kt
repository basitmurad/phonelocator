package com.example.locationtracker.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.locationtracker.R


class ConnectFragment : Fragment() {

private lateinit var appCompatButton2:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        appCompatButton2= view.findViewById(R.id.appCompatButton2)


        appCompatButton2.setOnClickListener {
            openDialoge()
        }

   return view
    }

    private fun openDialoge() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.confirm_dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Ensure the dialog background is transparent
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Show the dialog first to get its window
        alertDialog.show()

        // Set dialog margins
        val window = alertDialog.window
        if (window != null) {
            val layoutParams = window.attributes
            val margin = 50 // Set margin in pixels (e.g., 50px)
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(
                (resources.displayMetrics.widthPixels - 2 * margin),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.attributes = layoutParams
        }
    }


}