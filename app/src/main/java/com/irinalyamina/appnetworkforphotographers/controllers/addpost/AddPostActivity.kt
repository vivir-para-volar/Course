package com.irinalyamina.appnetworkforphotographers.controllers.addpost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}