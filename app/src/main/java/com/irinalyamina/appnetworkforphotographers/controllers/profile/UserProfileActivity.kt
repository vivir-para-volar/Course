package com.irinalyamina.appnetworkforphotographers.controllers.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.addpost.AddBlogActivity
import com.irinalyamina.appnetworkforphotographers.controllers.addpost.AddPostActivity
import com.irinalyamina.appnetworkforphotographers.controllers.authorization.AuthorizationActivity
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityUserProfileBinding
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        onCreateBottomNavigationView()
        onTabLayout()

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        postsAdapter = PostsAdapter(this)
        binding.recyclerViewPosts.adapter = postsAdapter

        initialDate()
    }

    private fun initialDate() {
        val photographer = PhotographerService.getCurrentUser()

        binding.textUsername.text = photographer.username + "(" + photographer.name + ")"

        if (photographer.profilePhoto != null) {
            binding.profilePhoto.setImageBitmap(photographer.profilePhoto)
        }

        val postService = PostService(this@UserProfileActivity)
        val listPosts = postService.getAllPhotographerPosts(photographer.id)
        if (listPosts.isNotEmpty()) {
            postsAdapter.setListPosts(listPosts)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_post -> {
                startActivity(Intent(this, AddPostActivity::class.java))
                return true
            }
            R.id.menu_add_blog -> {
                startActivity(Intent(this, AddBlogActivity::class.java))
                return true
            }
            R.id.menu_edit_profile -> {
                startActivity(Intent(this, EditProfileActivity::class.java))
                return true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.menu_sign_out -> {
                PhotographerService.clearCurrentUser()
                startActivity(Intent(this, AuthorizationActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onTabLayout(){
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
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
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}