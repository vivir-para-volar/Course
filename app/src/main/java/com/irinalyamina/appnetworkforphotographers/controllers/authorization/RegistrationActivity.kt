package com.irinalyamina.appnetworkforphotographers.controllers.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityRegistrationBinding
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import com.irinalyamina.appnetworkforphotographers.service.UserService

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textLink.setOnClickListener {
            startActivity(Intent(this, AuthorizationActivity::class.java))
            overridePendingTransition(0,0)
        }

        binding.btnRegistration.setOnClickListener { btnRegistrationOnClickListener() }

        Parse.onDatePicker(this, binding.editTextBirthday, binding.btnChangeBirthday)
    }

    private fun btnRegistrationOnClickListener() {
        val username = binding.editTextUsername.text.toString().trim()
        val name = binding.editTextName.text.toString().trim()
        val birthday = Parse.stringToDate(binding.editTextBirthday.text.toString())
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

        if (username.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_username))
            return
        }
        if (name.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_name))
            return
        }
        if (email.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_email))
            return
        }
        if (password.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_password))
            return
        }
        if(password != confirmPassword){
            ShowMessage.toast(this, getString(R.string.error_password))
            return
        }

        val newUser = Photographer(username, name, birthday, email, password)

        val service = UserService(this)
        val answer = service.registration(newUser)

        if(answer){
            startActivity(Intent(this, AuthorizationActivity::class.java))
            overridePendingTransition(0,0)
        }
    }
}