package com.irinalyamina.appnetworkforphotographers.controllers.addpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityAddPostBinding
import com.irinalyamina.appnetworkforphotographers.models.Category
import com.irinalyamina.appnetworkforphotographers.models.CategoryDirectory
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.service.CategoryService
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding

    private var flagChoosePhoto: Boolean = false
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnLoadPostPhoto.setOnClickListener { btnLoadPhotoOnClickListener() }
        binding.btnSavePost.setOnClickListener { btnSavePostOnClickListener() }

        initSpinner()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun btnLoadPhotoOnClickListener() {
        val PICK_IMAGE = 1

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!
            binding.postPhoto.setImageURI(selectedImage)
            flagChoosePhoto = true
        }
    }

    private fun btnSavePostOnClickListener() {
        if (!flagChoosePhoto) {
            ShowMessage.toast(this, getString(R.string.empty_photo_post))
            return
        }

        val caption = binding.editTextCaption.text.toString().trim()
        if (caption.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.empty_caption))
            return
        }

        val newPost = Post(caption, PhotographerService.getCurrentUser().id, categoryId)

        val postService = PostService(this)
        val answer = postService.addPost(newPost, binding.postPhoto.drawToBitmap())

        if (answer) {
            ShowMessage.toast(this, getString(R.string.success_add_post))
            startActivity(Intent(this, UserProfileActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    private fun initSpinner() {
        val categoryService = CategoryService(this)
        val hashMap = categoryService.getAllCategoriesWithDirectories()

        if (hashMap != null) {
            val listCategoryDirectories: ArrayList<CategoryDirectory> = arrayListOf()
            for ((key, value) in hashMap) {
                listCategoryDirectories.add(key)
            }

            val adapterCategoryDirectories: ArrayAdapter<CategoryDirectory> =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategoryDirectories)
            adapterCategoryDirectories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerCategoryDirectory.adapter = adapterCategoryDirectories
            binding.spinnerCategoryDirectory.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        var index = 0
                        for ((key, value) in hashMap) {
                            if (index == position) {
                                val indexNow = index
                                val adapterCategories: ArrayAdapter<Category> = ArrayAdapter(
                                    this@AddPostActivity,
                                    android.R.layout.simple_spinner_item,
                                    value
                                )
                                adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                                binding.spinnerCategory.adapter = adapterCategories
                                binding.spinnerCategory.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                                        override fun onItemSelected(
                                            parent: AdapterView<*>?,
                                            view: View?,
                                            position: Int,
                                            id: Long
                                        ) {
                                            var indexPosition = 0
                                            for ((key, value) in hashMap) {
                                                if (indexPosition == indexNow) {
                                                    categoryId = value[position].id
                                                }
                                                indexPosition++
                                            }
                                        }
                                    }
                            }
                            index++
                        }
                    }
                }
        }
    }
}