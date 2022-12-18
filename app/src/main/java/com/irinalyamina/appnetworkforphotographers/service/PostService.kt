package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.database.DatabasePost
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.models.PostComment

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

    fun deletePost(postId: Int): Boolean {
        return try {
            database.delete(postId)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun addLike(postId: Int, photographerId: Int): Boolean {
        return try {
            database.addLike(postId, photographerId)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun deleteLike(postId: Int, photographerId: Int): Boolean {
        return try {
            database.deleteLike(postId, photographerId)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun addPostComment(comment: PostComment): Boolean {
        return try {
            database.addPostComment(comment)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun getPhotographerPosts(photographerId: Int): ArrayList<Post> {
        return try {
            database.getPhotographerPosts(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            arrayListOf()
        }
    }

    fun getPostsNews(): ArrayList<Post> {
        return try {
            database.getPostsNews(PhotographerService.getCurrentUser())
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            arrayListOf()
        }
    }

    fun getPostsOther(): ArrayList<Post> {
        return try {
            database.getPostsOther(PhotographerService.getCurrentUser())
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            arrayListOf()
        }
    }

    fun getPostComments(postId: Int): ArrayList<PostComment> {
        return try {
            database.getPostComments(postId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            arrayListOf()
        }
    }
}