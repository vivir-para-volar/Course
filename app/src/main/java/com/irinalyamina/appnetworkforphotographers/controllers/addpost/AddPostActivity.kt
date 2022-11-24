package com.irinalyamina.appnetworkforphotographers.controllers.addpost

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.core.view.drawToBitmap
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityAddPostBinding
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.service.PostService
import com.irinalyamina.appnetworkforphotographers.service.UserService

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding

    private var flagChoosePhoto: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnLoadPhoto.setOnClickListener { btnLoadPhotoOnClickListener() }
        binding.btnSavePost.setOnClickListener { btnSavePostOnClickListener() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun btnLoadPhotoOnClickListener(){
        val PICK_IMAGE = 1

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!
            binding.photo.setImageURI(selectedImage)
            flagChoosePhoto = true
        }
    }

    private fun btnSavePostOnClickListener(){
        if(!flagChoosePhoto){
            ShowMessage.toast(this, getString(R.string.empty_photo_post))
            return
        }

        val caption = binding.editTextCaption.text.toString().trim()
        if (caption.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_caption))
            return
        }

        val newPost = Post(caption, UserService.getCurrentUser().id!!)

        val postService = PostService(this)
        val answer = postService.addPost(newPost, binding.photo.drawToBitmap())

        if(answer){
            ShowMessage.toast(this, getString(R.string.success_add_post))
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(0,0)
        }
    }
}