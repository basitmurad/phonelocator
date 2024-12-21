package com.example.locationtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.locationtracker.databinding.ActivityHomeBinding
import com.example.locationtracker.fragments.ConnectFragment
import com.example.locationtracker.fragments.DeviceFragment
import com.example.locationtracker.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up BottomNavigationView listener
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.nav_search -> {
                    loadFragment(ConnectFragment())
                    true
                }  R.id.nav_notifications -> {
                loadFragment(DeviceFragment())
                true
            }
                else -> false
            }
        }

        // Load the default fragment (e.g., HomeFragment)
        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment) // Use the ID of the FrameLayout
            .commit()
    }
}
