package com.irinalyamina.appnetworkforphotographers.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.print.PrintJob
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import java.io.IOException
import java.lang.Exception
import java.sql.SQLException
import java.time.LocalDate

class DatabasePhotographer(private var context: Context) {

    companion object {
        var user: Photographer? = null

        fun clearUser() {
            user = null
        }
    }

    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    private var db: SQLiteDatabase

    init {
        try {
            dbHelper.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error(context.getString(R.string.error_update_database))
        }
        db = try {
            dbHelper.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
    }

    fun authorization(username: String, password: String) {
        val query =
            "SELECT Id, Name, Birthday, Email, PathProfilePhoto, ProfileDescription FROM Photographers " +
                    "WHERE Username = '$username' AND Password = '$password'"

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.count == 0) {
            throw Exception(context.getString(R.string.error_authorization))
        }

        cursor.moveToFirst()

        val id = cursor.getInt(0)
        val name = cursor.getString(1)
        val birthday = Parse.stringToDate(cursor.getString(2))
        val email = cursor.getString(3)
        val pathProfilePhoto = cursor.getString(4)
        val profileDescription = cursor.getString(5)

        var profilePhoto: Bitmap? = null
        if (pathProfilePhoto != null) {
            val imageProcessing = ImageProcessing(context)
            profilePhoto = imageProcessing.getPhoto(pathProfilePhoto)
        }

        user = Photographer(id, username, name, birthday, email, profilePhoto, profileDescription)
    }

    fun registration(newUser: Photographer): Long {
        val cv = ContentValues()
        cv.put("Username", newUser.username)
        cv.put("Name", newUser.name)
        cv.put("Birthday", Parse.dateToString(newUser.birthday))
        cv.put("Email", newUser.email)
        cv.put("Password", newUser.password)

        val id: Long = db.insert("Photographers", null, cv)

        if (id == -1L) {
            throw Exception(context.getString(R.string.error_registration))
        }

        return id
    }

    fun checkForUniqueness(photographer: Photographer) {
        val id = photographer.id
        val username = photographer.username
        val email = photographer.email

        var queryUsername = "SELECT Id FROM Photographers WHERE Username = '$username' "
        var queryEmail = "SELECT Id FROM Photographers WHERE Email = '$email' "
        if (id != -1) {
            queryUsername += "AND Id <> '$id'"
            queryEmail += "AND Id <> '$id'"
        }

        var cursor: Cursor = db.rawQuery(queryUsername, null)
        if (cursor.count != 0) {
            throw Exception(context.getString(R.string.error_unique_username))
        }

        cursor = db.rawQuery(queryEmail, null)
        if (cursor.count != 0) {
            throw Exception(context.getString(R.string.error_unique_email))
        }
    }

    fun updateUser(changedUser: Photographer) {
        val cv = ContentValues()
        cv.put("Username", changedUser.username)
        cv.put("Name", changedUser.name)
        cv.put("Birthday", Parse.dateToString(changedUser.birthday))
        cv.put("Email", changedUser.email)

        val count: Int = db.update("Photographers", cv, "Id=?", arrayOf(user?.id.toString()))
        if (count == 0) {
            throw Exception(context.getString(R.string.error_edit_profile))
        }

        user?.username = changedUser.username
        user?.name = changedUser.name
        user?.birthday = changedUser.birthday
        user?.email = changedUser.email
    }

    fun updateUserProfilePhoto(userPhoto: Bitmap) {
        val imageProcessing = ImageProcessing(context)
        val pathProfilePhoto = imageProcessing.savePhotoProfile(userPhoto, user!!.id)

        val cv = ContentValues()
        cv.put("PathProfilePhoto", pathProfilePhoto)

        val count: Int = db.update("Photographers", cv, "Id=?", arrayOf(user?.id.toString()))
        if (count == 0) {
            throw Exception(context.getString(R.string.error_edit_profile))
        }

        user?.profilePhoto = userPhoto
    }

    fun getPhotographerById(id: Int): Photographer {
        val query =
            "SELECT Username, Name, Birthday, Email, PathProfilePhoto, ProfileDescription FROM Photographers WHERE Id = '$id'"

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.count == 0) {
            throw Exception(context.getString(R.string.error_get_by_id))
        }

        cursor.moveToFirst()

        val username = cursor.getString(0)
        val name = cursor.getString(1)
        val birthday = Parse.stringToDate(cursor.getString(2))
        val email = cursor.getString(3)
        val pathProfilePhoto = cursor.getString(4)
        val profileDescription = cursor.getString(5)

        var profilePhoto: Bitmap? = null
        if (pathProfilePhoto != null) {
            val imageProcessing = ImageProcessing(context)
            profilePhoto = imageProcessing.getPhoto(pathProfilePhoto)
        }

        return Photographer(id, username, name, birthday, email, profilePhoto, profileDescription)
    }

    fun addSubscription(photographerId: Int, subscriberId: Int): Long {
        val cv = ContentValues()
        cv.put("PhotographerId", photographerId)
        cv.put("SubscriberId", subscriberId)

        val id: Long = db.insert("Subscriptions", null, cv)

        if (id == -1L) {
            throw Exception(context.getString(R.string.error_add_subscription))
        }

        return id
    }

    fun deleteSubscription(photographerId: Int, subscriberId: Int) {
        val countRaw = db.delete(
            "Subscriptions",
            "PhotographerId = ? AND SubscriberId = ?",
            arrayOf(photographerId.toString(), subscriberId.toString())
        )

        if (countRaw != 1) {
            throw Exception(context.getString(R.string.error_delete_subscription))
        }
    }

    fun checkSubscription(photographerId: Int, subscriberId: Int): Boolean {
        val query =
            "SELECT COUNT(*) FROM Subscriptions WHERE PhotographerId = '$photographerId' AND SubscriberId = '$subscriberId'"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.moveToFirst()
        val count = cursor.getInt(0)
        return count == 1
    }

    fun getCountFollowers(photographerId: Int): Int {
        val query = "SELECT COUNT(*) FROM Subscriptions WHERE PhotographerId = '$photographerId'"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.moveToFirst()
        val count = cursor.getInt(0)
        return count
    }

    fun getCountFollowing(photographerId: Int): Int {
        val query = "SELECT COUNT(*) FROM Subscriptions WHERE SubscriberId = '$photographerId'"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.moveToFirst()
        val count = cursor.getInt(0)
        return count
    }

    fun getFollowers(photographerId: Int): ArrayList<Photographer> {
        val list = arrayListOf<Photographer>()

        val query =
            "SELECT SubscriberId FROM Subscriptions WHERE PhotographerId = '$photographerId'"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val subscriberId = cursor.getInt(0)

            val subscriber = getPhotographerShortInfoById(subscriberId)
            list.add(subscriber)
        }

        return list
    }

    fun getFollowing(photographerId: Int): ArrayList<Photographer> {
        val list = arrayListOf<Photographer>()

        val query =
            "SELECT PhotographerId FROM Subscriptions WHERE SubscriberId = '$photographerId'"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val photographerId = cursor.getInt(0)

            val photographer = getPhotographerShortInfoById(photographerId)
            list.add(photographer)
        }

        return list
    }

    private fun getPhotographerShortInfoById(id: Int): Photographer {
        val query = "SELECT Username, Name, PathProfilePhoto FROM Photographers WHERE Id = '$id'"

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.count == 0) {
            throw Exception(context.getString(R.string.error_get_by_id))
        }

        cursor.moveToFirst()

        val username = cursor.getString(0)
        val name = cursor.getString(1)
        val pathProfilePhoto = cursor.getString(2)

        var profilePhoto: Bitmap? = null
        if (pathProfilePhoto != null) {
            val imageProcessing = ImageProcessing(context)
            profilePhoto = imageProcessing.getPhoto(pathProfilePhoto)
        }

        return Photographer(id, username, name, LocalDate.now(), "", profilePhoto, null)
    }
}