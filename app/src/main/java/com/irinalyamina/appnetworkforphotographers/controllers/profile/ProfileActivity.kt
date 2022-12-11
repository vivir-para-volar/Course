package com.irinalyamina.appnetworkforphotographers.controllers.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityProfileBinding
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val fromActivity = intent.getStringExtra("fromActivity")
        onCreateBottomNavigationView(fromActivity)

        onTabLayout()

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        postsAdapter = PostsAdapter(this)
        binding.recyclerViewPosts.adapter = postsAdapter

        val photographerId = intent.getIntExtra("photographerId", -1)
        if(photographerId != -1){
            initialDate(photographerId)
        }
    }

    private fun initialDate(photographerId: Int) {
        val photographerService = PhotographerService(this)
        val photographer = photographerService.getPhotographerById(photographerId)

        if (photographer != null){
            binding.textUsername.text = photographer.username + "(" + photographer.name + ")"

            if (photographer.profilePhoto != null) {
                binding.profilePhoto.setImageBitmap(photographer.profilePhoto)
            }

            val postService = PostService(this)
            val listPosts = postService.getAllPhotographerPosts(photographer.id)
            if (listPosts.isNotEmpty()) {
                postsAdapter.setListPosts(listPosts)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onTabLayout(){
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(binding.tabLayout.selectedTabPosition){
                    0 -> {
                        binding.constraintLayoutAbout.visibility = View.VISIBLE
                        binding.recyclerViewPosts.visibility = View.GONE
                        binding.recyclerViewBlogs.visibility = View.GONE
                    }
                    1 -> {
                        binding.constraintLayoutAbout.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.VISIBLE
                        binding.recyclerViewBlogs.visibility = View.GONE
                    }
                    2 -> {
                        binding.constraintLayoutAbout.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.GONE
                        binding.recyclerViewBlogs.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun onCreateBottomNavigationView(fromActivity: String?){
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        when(fromActivity){
            null -> bottomNavView.selectedItemId = R.id.nav_home
            FromActivity.home -> bottomNavView.selectedItemId = R.id.nav_home
            FromActivity.search -> bottomNavView.selectedItemId = R.id.nav_search
            FromActivity.map -> bottomNavView.selectedItemId = R.id.nav_map
            FromActivity.messenger -> bottomNavView.selectedItemId = R.id.nav_messenger
            FromActivity.profile -> bottomNavView.selectedItemId = R.id.nav_profile
        }

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0,0)
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
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}