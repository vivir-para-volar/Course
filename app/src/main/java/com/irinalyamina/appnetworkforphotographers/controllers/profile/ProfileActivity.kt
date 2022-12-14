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
import com.irinalyamina.appnetworkforphotographers.controllers.post.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.photographer.SubscriptionActivity
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

        val fromActivity = intent.getStringExtra("fromActivity")
        val photographerId = intent.getIntExtra("photographerId", -1)
        if (fromActivity == null || photographerId == -1) {
            finish()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        onCreateBottomNavigationView(fromActivity)

        onTabLayout()

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        postsAdapter = PostsAdapter(this, fromActivity!!)
        binding.recyclerViewPosts.adapter = postsAdapter

        initialDate(photographerId)

        binding.btnSubscribe.setOnClickListener { btnSubscribeOnClickListener(photographerId) }
        binding.btnUnsubscribe.setOnClickListener { btnUnsubscribeOnClickListener(photographerId) }

        binding.btnFollowers.setOnClickListener {
            btnFollowersOnClickListener(
                photographerId,
                fromActivity
            )
        }
        binding.btnFollowing.setOnClickListener {
            btnFollowingOnClickListener(
                photographerId,
                fromActivity
            )
        }
    }

    private fun initialDate(photographerId: Int) {
        val photographerService = PhotographerService(this)
        val photographer = photographerService.getPhotographerById(photographerId)

        if (photographer != null) {
            binding.textUsername.text = photographer.username + "(" + photographer.name + ")"

            if (photographer.profilePhoto != null) {
                binding.profilePhoto.setImageBitmap(photographer.profilePhoto)
            }

            if (photographer.profileDescription != null) {
                binding.textDescription.text = photographer.profileDescription
            }

            val res = photographerService.checkSubscription(
                photographerId,
                PhotographerService.getCurrentUser().id
            )
            if (res) {
                binding.btnSubscribe.visibility = View.INVISIBLE
                binding.btnUnsubscribe.visibility = View.VISIBLE
            }

            binding.textFollowers.text =
                photographerService.getCountFollowers(photographer.id).toString()
            binding.textFollowing.text =
                photographerService.getCountFollowing(photographer.id).toString()

            val postService = PostService(this)
            val listPosts = postService.getPhotographerPosts(photographer.id)
            if (listPosts.isNotEmpty()) {
                postsAdapter.setListPosts(listPosts)
            }
        }
    }

    private fun btnSubscribeOnClickListener(photographerId: Int) {
        val photographerService = PhotographerService(this)
        val answer = photographerService.addSubscription(
            photographerId,
            PhotographerService.getCurrentUser().id
        )

        if (answer) {
            binding.btnSubscribe.visibility = View.INVISIBLE
            binding.btnUnsubscribe.visibility = View.VISIBLE

            binding.textFollowers.text =
                photographerService.getCountFollowers(photographerId).toString()
        }
    }

    private fun btnUnsubscribeOnClickListener(photographerId: Int) {
        val photographerService = PhotographerService(this)
        val answer = photographerService.deleteSubscription(
            photographerId,
            PhotographerService.getCurrentUser().id
        )

        if (answer) {
            binding.btnSubscribe.visibility = View.VISIBLE
            binding.btnUnsubscribe.visibility = View.INVISIBLE

            binding.textFollowers.text =
                photographerService.getCountFollowers(photographerId).toString()
        }
    }

    private fun btnFollowersOnClickListener(photographerId: Int, fromActivity: String) {
        val intent = Intent(this, SubscriptionActivity::class.java)
        intent.putExtra("photographerId", photographerId)
        intent.putExtra("whatActivity", "followers")
        intent.putExtra("fromActivity", fromActivity)
        startActivity(intent)
    }

    private fun btnFollowingOnClickListener(photographerId: Int, fromActivity: String) {
        val intent = Intent(this, SubscriptionActivity::class.java)
        intent.putExtra("photographerId", photographerId)
        intent.putExtra("whatActivity", "following")
        intent.putExtra("fromActivity", fromActivity)
        startActivity(intent)
    }

    private fun onTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabLayout.selectedTabPosition) {
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