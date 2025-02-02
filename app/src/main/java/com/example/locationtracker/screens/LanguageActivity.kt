

package com.example.locationtracker.screens

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtracker.HomeActivity
import com.example.locationtracker.adapters.LanguageAdapter
import com.example.locationtracker.databinding.ActivityLanguageBinding
import java.util.*

class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    private lateinit var progressBar: ProgressBar  // ProgressBar to show loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply saved language before inflating the layout
        applySavedLanguage()

        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ProgressBar
        progressBar = binding.progressBar // Ensure you have a ProgressBar in the layout

        binding.btnBack.setOnClickListener {
            finish()
        }

        val languages = listOf("English", "Tamil", "Urdu")
        val languageCodes = listOf("en", "ta", "ur")

        binding.languageRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.languageRecyclerView.adapter = LanguageAdapter(languages) { position ->
            val selectedLanguageCode = languageCodes[position]
            changeAppLanguage(selectedLanguageCode)
        }
    }

    private fun changeAppLanguage(languageCode: String) {
        // Show progress bar while changing language
        progressBar.visibility = ProgressBar.VISIBLE

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        // Set layout direction based on language
        if (languageCode == "ur") {
            configuration.setLayoutDirection(locale)  // This ensures the layout is switched to RTL for Urdu
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Save the language preference
        saveLanguagePreference(languageCode)

        // Log for debugging
        Log.d("LanguageActivity", "Language changed to: $languageCode")

        // Simulate a delay (like showing a message after 3 seconds)
        Handler().postDelayed({
            // Hide the progress bar
            progressBar.visibility = ProgressBar.INVISIBLE

            // Show a message that the language has been changed
            Toast.makeText(this, "Language changed successfully", Toast.LENGTH_SHORT).show()

            // Go to HomeActivity after a brief delay
            navigateToHomeActivity()
        }, 3000)  // Delay of 3 seconds
    }

    private fun saveLanguagePreference(languageCode: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().putString("LanguageCode", languageCode).apply()
    }

    private fun getSavedLanguagePreference(): String? {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("LanguageCode", "en") // Default to English ("en")
    }

    private fun applySavedLanguage() {
        val savedLanguageCode = getSavedLanguagePreference() ?: return
        val locale = Locale(savedLanguageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun navigateToHomeActivity() {
        // Start the HomeActivity after language change
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()  // Finish this activity
    }
}
