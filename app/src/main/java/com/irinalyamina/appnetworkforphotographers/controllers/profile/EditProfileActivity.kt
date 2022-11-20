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
import com.irinalyamina.appnetworkforphotographers.service.UserService

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var editTextUsername: TextView
    private lateinit var editTextName: TextView
    private lateinit var editTextBirthday: TextView
    private lateinit var editTextEmail: TextView

    private lateinit var profilePhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        editTextUsername = binding.editTextUsername
        editTextName = binding.editTextName
        editTextBirthday = binding.editTextBirthday
        editTextEmail = binding.editTextEmail

        profilePhoto = binding.profileImage

        Parse.onDatePicker(this, binding.editTextBirthday, binding.btnChangeBirthday)

        initialDate()

        binding.btnChangeProfile.setOnClickListener { btnChangeProfileOnClickListener() }
        binding.btnChangePhoto.setOnClickListener { btnChangePhotoOnClickListener() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialDate() {
        val user = UserService.getCurrentUser()

        editTextUsername.text = user.username
        editTextName.text = user.name
        editTextBirthday.text = Parse.dateToString(user.birthday)
        editTextEmail.text = user.email

        val userService = UserService(this)
        val photo = userService.getProfilePhoto(user)
        if(photo != null){
            profilePhoto.setImageBitmap(photo)
        }
    }

    private fun btnChangeProfileOnClickListener() {
        val username = editTextUsername.text.toString().trim()
        val name = editTextName.text.toString().trim()
        val birthday = Parse.stringToDate(editTextBirthday.text.toString())
        val email = editTextEmail.text.toString().trim()

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

        val changedUser = Photographer(UserService.getCurrentUser().id, username, name, birthday, email, null)

        val service = UserService(this)
        val answer = service.editProfile(changedUser)

        if(answer){
            ShowMessage.toast(this, getString(R.string.success_change_profile))
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnChangePhotoOnClickListener(){
        val PICK_IMAGE = 1

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!

            val service = UserService(this)
            val answer = service.editProfilePhoto((selectedImage))

            if(answer){
                profilePhoto.setImageURI(selectedImage)
            }
        }
    }
}