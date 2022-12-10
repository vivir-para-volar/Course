package com.irinalyamina.appnetworkforphotographers.controllers.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityEditProfileBinding
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        Parse.onDatePicker(this, binding.editTextBirthday, binding.btnChangeBirthday)

        initialDate()

        binding.btnChangePhoto.setOnClickListener { btnChangePhotoOnClickListener() }
        binding.btnChangeProfile.setOnClickListener { btnChangeProfileOnClickListener() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialDate() {
        val user = PhotographerService.getCurrentUser()

        (binding.editTextUsername as TextView).text = user.username
        (binding.editTextName as TextView).text = user.name
        (binding.editTextBirthday as TextView).text = Parse.dateToString(user.birthday)
        (binding.editTextEmail as TextView).text = user.email

        if (user.profilePhoto != null) {
            binding.profilePhoto.setImageBitmap(user.profilePhoto)
        }
    }

    private fun btnChangeProfileOnClickListener() {
        val username = binding.editTextUsername.text.toString().trim()
        val name = binding.editTextName.text.toString().trim()
        val birthday = Parse.stringToDate(binding.editTextBirthday.text.toString())
        val email = binding.editTextEmail.text.toString().trim()

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

        val changedUser =
            Photographer(PhotographerService.getCurrentUser().id, username, name, birthday, email, null)

        val service = PhotographerService(this)
        val answer = service.editProfile(changedUser)

        if (answer) {
            ShowMessage.toast(this, getString(R.string.success_change_profile))
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnChangePhotoOnClickListener() {
        val PICK_IMAGE = 1

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!

            val service = PhotographerService(this)
            val answer = service.editUserProfilePhoto((selectedImage))

            if (answer) {
                ShowMessage.toast(this, getString(R.string.success_change_profile_photo))
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}