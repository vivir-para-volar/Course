package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.database.DatabasePost
import com.irinalyamina.appnetworkforphotographers.database.ImageProcessing
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import com.irinalyamina.appnetworkforphotographers.models.Post

class PostService(private var context: Context) {

    private lateinit var database: DatabasePost

    init {
        try {
            database = DatabasePost(context)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
        }
    }

    fun addPost(newPost: Post, postPhoto: Bitmap): Boolean {
        return try {
            database.add(newPost, postPhoto)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun getAllPhotographerPosts(photographerId: Int): List<Post> {
        return try {
            database.allPhotographerPosts(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            emptyList()
        }
    }
}