package com.irinalyamina.appnetworkforphotographers.controllers.photographer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivitySubscriptionBinding
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService

class SubscriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var photographersAdapter: PhotographersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromActivity = intent.getStringExtra("fromActivity")
        val whatActivity = intent.getStringExtra("whatActivity")
        val photographerId = intent.getIntExtra("photographerId", -1)
        if (fromActivity == null || whatActivity == null || photographerId == -1) {
            finish()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        onCreateBottomNavigationView(fromActivity)

        binding.recyclerViewPhotographers.layoutManager = LinearLayoutManager(this)
        photographersAdapter = PhotographersAdapter(this, fromActivity!!)
        binding.recyclerViewPhotographers.adapter = photographersAdapter

        initialDate(photographerId, whatActivity!!)
    }

    private fun initialDate(photographerId: Int, whatActivity: String) {
        val photographerService = PhotographerService(this)
        val photographer = photographerService.getPhotographerById(photographerId)

        if (photographer != null) {
            if (whatActivity == "followers") {
                val list = photographerService.getFollowers(photographerId)

                binding.textTitle.text = getString(R.string.text_followers)
                binding.textCount.text = list.size.toString()

                val listPhotographers = photographerService.getFollowers(photographerId)
                photographersAdapter.setListPhotographers(listPhotographers)

            } else if (whatActivity == "following") {
                val list = photographerService.getFollowing(photographerId)

                binding.textTitle.text = getString(R.string.text_following)
                binding.textCount.text = list.size.toString()

                val listPhotographers = photographerService.getFollowing(photographerId)
                photographersAdapter.setListPhotographers(listPhotographers)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onCreateBottomNavigationView(fromActivity: String?) {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        when (fromActivity) {
            null -> bottomNavView.selectedItemId = R.id.nav_home
            FromActivity.home -> bottomNavView.selectedItemId = R.id.nav_home
            FromActivity.search -> bottomNavView.selectedItemId = R.id.nav_search
            FromActivity.map -> bottomNavView.selectedItemId = R.id.nav_map
            FromActivity.messenger -> bottomNavView.selectedItemId = R.id.nav_messenger
            FromActivity.userProfile -> bottomNavView.selectedItemId = R.id.nav_profile
        }

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
                    startActivity(Intent(applicationContext, MessengerActivity::class.java))
                    overridePendingTransition(0, 0)
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