package com.irinalyamina.appnetworkforphotographers.controllers.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityMessengerBinding

class MessengerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateBottomNavigationView()
    }

    private fun onCreateBottomNavigationView() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.nav_messenger

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_map -> {
                    startActivity(Intent(applicationContext, MapActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_messenger -> {
                    return@setOnItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}