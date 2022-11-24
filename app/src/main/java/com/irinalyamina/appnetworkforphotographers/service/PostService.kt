package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.database.DatabasePost
import com.irinalyamina.appnetworkforphotographers.database.ImageProcessing
import com.irinalyamina.appnetworkforphotographers.models.Post

class PostService (private var context: Context) {

    private lateinit var database: DatabasePost

    init {
        try {
            database = DatabasePost(context)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
        }
    }

    fun addPost(newPost: Post, image: Bitmap): Boolean{
        return try {
            database.add(newPost, image)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }
}