package com.irinalyamina.appnetworkforphotographers.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import java.io.IOException
import java.lang.Exception
import java.sql.SQLException

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
        val query = "SELECT Id, Name, Birthday, Email, PathProfilePhoto, ProfileDescription FROM Photographers " +
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
        val query = "SELECT Username, Name, Birthday, Email, PathProfilePhoto, ProfileDescription FROM Photographers WHERE Id = '$id'"

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
}