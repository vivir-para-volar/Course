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
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.post.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.blog.AddBlogActivity
import com.irinalyamina.appnetworkforphotographers.controllers.post.AddPostActivity
import com.irinalyamina.appnetworkforphotographers.controllers.authorization.AuthorizationActivity
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.photographer.SubscriptionActivity
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
        postsAdapter = PostsAdapter(this, FromActivity.userProfile)
        binding.recyclerViewPosts.adapter = postsAdapter

        initialDate()

        binding.btnAddPost.setOnClickListener{
            startActivity(Intent(this, AddPostActivity::class.java))
        }

        binding.btnFollowers.setOnClickListener {
            btnFollowersOnClickListener(
                PhotographerService.getCurrentUser().id,
                FromActivity.userProfile
            )
        }
        binding.btnFollowing.setOnClickListener {
            btnFollowingOnClickListener(
                PhotographerService.getCurrentUser().id,
                FromActivity.userProfile
            )
        }
    }

    private fun initialDate() {
        val photographer = PhotographerService.getCurrentUser()

        binding.textUsername.text = photographer.username + "(" + photographer.name + ")"

        if (photographer.profilePhoto != null) {
            binding.profilePhoto.setImageBitmap(photographer.profilePhoto)
        }

        if (photographer.profileDescription != null && photographer.profileDescription!!.isNotEmpty()) {
            binding.textDescription.text = photographer.profileDescription
        }

        val photographerService = PhotographerService(this)
        binding.textFollowers.text =
            photographerService.getCountFollowers(photographer.id).toString()
        binding.textFollowing.text =
            photographerService.getCountFollowing(photographer.id).toString()

        binding.textEmail.text = photographer.email

        if (photographer.photographyEquipment != null && photographer.photographyEquipment!!.isNotEmpty()) {
            binding.textPhotographyEquipment.text = photographer.photographyEquipment
        }

        if (photographer.photographyAwards != null && photographer.photographyAwards!!.isNotEmpty()) {
            binding.textPhotographyAwards.text = photographer.photographyAwards
        }

        val postService = PostService(this@UserProfileActivity)
        val listPosts = postService.getPhotographerPosts(photographer.id)
        if (listPosts.isNotEmpty()) {
            postsAdapter.setListPosts(listPosts)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    private fun onTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        binding.layoutAbout.visibility = View.VISIBLE
                        binding.recyclerViewPosts.visibility = View.GONE
                        binding.recyclerViewBlogs.visibility = View.GONE
                    }
                    1 -> {
                        binding.layoutAbout.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.VISIBLE
                        binding.recyclerViewBlogs.visibility = View.GONE
                    }
                    2 -> {
                        binding.layoutAbout.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.GONE
                        binding.recyclerViewBlogs.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun onCreateBottomNavigationView() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.nav_profile

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
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}