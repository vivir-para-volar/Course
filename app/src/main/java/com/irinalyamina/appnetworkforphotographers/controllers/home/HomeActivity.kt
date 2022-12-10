package com.irinalyamina.appnetworkforphotographers.controllers.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityHomeBinding
import com.irinalyamina.appnetworkforphotographers.service.PostService

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateBottomNavigationView()

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        postsAdapter = PostsAdapter(this)
        binding.recyclerViewPosts.adapter = postsAdapter

        initialDate()
    }

    private fun onCreateBottomNavigationView(){
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.nav_home

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
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
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun initialDate() {
        val postService = PostService(this)
        val listPosts = postService.getAllPosts()
        if (listPosts.isNotEmpty()) {
            postsAdapter.setListPosts(listPosts)
        }
    }
}