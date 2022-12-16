package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.net.Uri
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.database.DatabasePhotographer
import com.irinalyamina.appnetworkforphotographers.models.Photographer

class PhotographerService(private var context: Context) {

    private lateinit var database: DatabasePhotographer

    init {
        try {
            database = DatabasePhotographer(context)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
        }
    }

    companion object {
        fun getCurrentUser(): Photographer {
            return DatabasePhotographer.user!!
        }

        fun clearCurrentUser() {
            DatabasePhotographer.clearUser()
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

    fun editUserInfo(changedUser: Photographer): Boolean {
        return try {
            database.checkForUniqueness(changedUser)
            database.updateUserInfo(changedUser)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun editUserProfilePhoto(image: Uri): Boolean {
        return try {
            val imageParse = ImageParse(context)
            database.updateUserProfilePhoto(imageParse.uriToBitmap(image))
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun editProfileInfo(changedUser: Photographer): Boolean {
        return try {
            database.checkForUniqueness(changedUser)
            database.updateProfileInfo(changedUser)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun getPhotographerById(photographerId: Int): Photographer? {
        return try {
            database.getPhotographerById(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            null
        }
    }

    fun addSubscription(photographerId: Int, subscriberId: Int): Boolean {
        return try {
            database.addSubscription(photographerId, subscriberId)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun deleteSubscription(photographerId: Int, subscriberId: Int): Boolean {
        return try {
            database.deleteSubscription(photographerId, subscriberId)
            true
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun checkSubscription(photographerId: Int, subscriberId: Int): Boolean {
        return try {
            database.checkSubscription(photographerId, subscriberId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            false
        }
    }

    fun getCountFollowers(photographerId: Int): Int {
        return try {
            database.getCountFollowers(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            0
        }
    }

    fun getCountFollowing(photographerId: Int): Int {
        return try {
            database.getCountFollowing(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            0
        }
    }

    fun getFollowers(photographerId: Int): ArrayList<Photographer> {
        return try {
            database.getFollowers(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            arrayListOf()
        }
    }

    fun getFollowing(photographerId: Int): ArrayList<Photographer> {
        return try {
            database.getFollowing(photographerId)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            arrayListOf()
        }
    }
}

