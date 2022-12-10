package com.irinalyamina.appnetworkforphotographers.controllers.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.controllers.home.HomeActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityAuthorizationBinding
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textLink.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(0, 0)
        }

        binding.btnAuthorization.setOnClickListener { btnAuthorizationOnClickListener() }

        val tv: TextView = binding.editTextUsername
        val tv1: TextView = binding.editTextPassword
        tv.text = "irina"
        tv1.text = "irina12345"
    }

    private fun btnAuthorizationOnClickListener() {
        val username = binding.editTextUsername.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (username.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_username))
            return
        }
        if (password.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_password))
            return
        }

        val service = PhotographerService(this)
        val answer = service.authorization(username, password)

        if (answer) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onBackPressed() {
        return
    }
}