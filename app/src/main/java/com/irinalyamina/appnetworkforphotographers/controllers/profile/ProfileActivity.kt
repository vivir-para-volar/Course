package com.irinalyamina.appnetworkforphotographers.controllers.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.PostsAdapter
import com.irinalyamina.appnetworkforphotographers.controllers.addpost.AddBlogActivity
import com.irinalyamina.appnetworkforphotographers.controllers.addpost.AddPostActivity
import com.irinalyamina.appnetworkforphotographers.controllers.authorization.AuthorizationActivity
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.controllers.map.MapActivity
import com.irinalyamina.appnetworkforphotographers.controllers.messenger.MessengerActivity
import com.irinalyamina.appnetworkforphotographers.controllers.search.SearchActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityProfileBinding
import com.irinalyamina.appnetworkforphotographers.service.PostService
import com.irinalyamina.appnetworkforphotographers.service.UserService

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        onCreateBottomNavigationView()


        /*binding.imagesRecycler.layoutManager = GridLayoutManager(this, 3)
        val mAdapter = PostsAdapter()
        binding.imagesRecycler.adapter = mAdapter*/


        initialDate()
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
            R.id.menu_exit -> {
                UserService.clearCurrentUser()
                startActivity(Intent(this, AuthorizationActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialDate() {
        val user = UserService.getCurrentUser()

        binding.textUsername.text = user.username + "(" + user.name + ")"

        if (user.profilePhoto != null) {
            binding.profilePhoto.setImageBitmap(user.profilePhoto)
        }

        val postService = PostService(this)
        val listPosts = postService.getAllPhotographerPosts(user.id)
        if (listPosts.isNotEmpty()) {

        }
    }
}