//
//package com.example.locationtracker.screens
//
//import android.content.res.Configuration
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.locationtracker.adapters.LanguageAdapter
//import com.example.locationtracker.databinding.ActivityLanguageBinding
//import java.util.*
//
//class LanguageActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityLanguageBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLanguageBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.btnBack.setOnClickListener {
//            finish()
//        }
//
//        // List of languages with their locale codes
//        val languages = listOf("English", "Tamil", "Urdu")
//        val languageCodes = listOf("en", "ta", "ur")
//
//        // Setup RecyclerView
//        binding.languageRecyclerView.layoutManager = LinearLayoutManager(this)
//        binding.languageRecyclerView.adapter = LanguageAdapter(languages) { position ->
//            val selectedLanguageCode = languageCodes[position]
//            changeAppLanguage(selectedLanguageCode)
//        }
//    }
//
//
//    private fun changeAppLanguage(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//
//        val configuration = Configuration(resources.configuration)
//        configuration.setLocale(locale)
//
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//
//        // Log to confirm the language change
//        Log.d("LanguageActivity", "Language changed to: $languageCode")
//
//        // Optionally, restart the activity to apply changes dynamically
//        recreate()
//    }
//}


package com.example.locationtracker.screens

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtracker.adapters.LanguageAdapter
import com.example.locationtracker.databinding.ActivityLanguageBinding
import java.util.*

class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply saved language before inflating the layout
        applySavedLanguage()

        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Save the language preference
        saveLanguagePreference(languageCode)

        Log.d("LanguageActivity", "Language changed to: $languageCode")

        // Restart the activity to apply the language change dynamically
        recreate()
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
}
