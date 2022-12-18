package com.irinalyamina.appnetworkforphotographers.controllers.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.photographer.PhotographersAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.post.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivitySearchBinding
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var listPeople: ArrayList<Photographer>
    private lateinit var listPosts: ArrayList<Post>

    private lateinit var peopleAdapter: PhotographersAdapter
    private lateinit var postsAdapter: PostsAdapter

    private var tabLayoutIndex: Int = 0

    private var searchString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateBottomNavigationView()

        binding.recyclerViewPeople.layoutManager = LinearLayoutManager(this)
        peopleAdapter = PhotographersAdapter(this, FromActivity.search)
        binding.recyclerViewPeople.adapter = peopleAdapter

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        postsAdapter = PostsAdapter(this, FromActivity.search)
        binding.recyclerViewPosts.adapter = postsAdapter

        onTabLayout()

        binding.btnSearch.setOnClickListener { btnSearchOnClickListener() }
    }

    private fun btnSearchOnClickListener() {
        searchString = binding.editTextSearch.text.toString().trim()
        if (searchString.isEmpty()) return

        listPeople = arrayListOf()
        listPosts = arrayListOf()

        if (tabLayoutIndex == 0) {
            searchPeople()
        } else {
            searchPosts()
        }
    }

    private fun searchPeople() {
        if (listPeople.isNotEmpty()) {
            binding.textSearchEmpty.visibility = View.GONE
            return
        }

        val photographerService = PhotographerService(this)
        listPeople = photographerService.searchPhotographers(searchString)

        if (listPeople.isEmpty()) {
            binding.textSearchEmpty.visibility = View.VISIBLE
        } else {
            binding.textSearchEmpty.visibility = View.GONE
        }

        peopleAdapter.setListPhotographers(listPeople)
    }

    private fun searchPosts() {
        if (listPosts.isNotEmpty()) {
            binding.textSearchEmpty.visibility = View.GONE
            return
        }

        val postService = PostService(this)
        listPosts = postService.searchPosts(searchString)

        if (listPosts.isEmpty()) {
            binding.textSearchEmpty.visibility = View.VISIBLE
        } else {
            binding.textSearchEmpty.visibility = View.GONE
        }

        postsAdapter.setListPosts(listPosts)
    }

    private fun onTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        tabLayoutIndex = 0
                        binding.recyclerViewPeople.visibility = View.VISIBLE
                        binding.recyclerViewPosts.visibility = View.GONE

                        if (searchString.isNotEmpty()) {
                            searchPeople()
                        }
                    }
                    1 -> {
                        tabLayoutIndex = 1
                        binding.recyclerViewPeople.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.VISIBLE

                        if (searchString.isNotEmpty()) {
                            searchPosts()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun onCreateBottomNavigationView() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.nav_search

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {
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