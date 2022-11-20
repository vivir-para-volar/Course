package com.irinalyamina.appnetworkforphotographers.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateBottomNavigationView()
    }

    private fun onCreateBottomNavigationView(){
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.nav_profile

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {
                    return@setOnItemSelectedListener true
                }
                R.id.nav_map -> {
                    startActivity(Intent(applicationContext, MapActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_messenger -> {
                    startActivity(Intent(applicationContext, MessengerActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}