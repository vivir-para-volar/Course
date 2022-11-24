package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.database.DatabaseUser
import com.irinalyamina.appnetworkforphotographers.database.ImageProcessing
import com.irinalyamina.appnetworkforphotographers.models.Photographer

class UserService(private var context: Context) {

    private lateinit var database: DatabaseUser

    init {
        try {
            database = DatabaseUser(context)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
        }
    }

    companion object{
        fun getCurrentUser(): Photographer {
            return DatabaseUser.user!!
        }

        fun clearCurrentUser() {
            DatabaseUser.clearUser()
        }
    }

    fun authorization(username: String, password: String): Boolean {
        return try {
            database.authorization(username, password)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun registration(newUser: Photographer): Boolean {
        return try {
            database.checkForUniqueness(newUser)
            database.registration(newUser)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun getProfilePhoto(photographer: Photographer): Bitmap?{
        if(photographer.pathProfilePhoto == null){
            return null
        }

        return try {
            val imageProcessing = ImageProcessing(context)
            imageProcessing.getPhoto(photographer.pathProfilePhoto!!)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            null
        }
    }

    fun editProfile(changedUser: Photographer): Boolean {
        return try {
            database.checkForUniqueness(changedUser)
            database.updateUser(changedUser)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun editProfilePhoto(image: Uri): Boolean {
        return try {
            val id: Int = getCurrentUser().id!!

            val imageProcessing = ImageProcessing(context)
            val path = imageProcessing.savePhotoProfile(image, id)

            database.updatePathProfilePhoto(path)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }
}