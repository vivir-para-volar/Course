package com.irinalyamina.appnetworkforphotographers.controllers.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.controllers.HomeActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityAuthorizationBinding
import com.irinalyamina.appnetworkforphotographers.service.UserService

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextUsername = binding.editTextUsername
        editTextPassword = binding.editTextPassword


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
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (username.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_username))
            return
        }
        if (password.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_password))
            return
        }

        val service = UserService(this)
        val answer = service.authorization(username, password)

        if (answer) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}