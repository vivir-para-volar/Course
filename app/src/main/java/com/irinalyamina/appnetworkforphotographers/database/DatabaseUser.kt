package com.irinalyamina.appnetworkforphotographers.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import java.io.IOException
import java.lang.Exception
import java.sql.SQLException

class DatabaseUser(private var context: Context) {

    companion object {
        var user = Photographer()

        fun clearUser(){
            user = Photographer()
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
        val query = "SELECT Id, Username, Name, Birthday, Email, PathProfilePhoto FROM Photographers " +
                "WHERE Username = '$username' AND Password = '$password'"

        val cursor: Cursor = db.rawQuery(query, null)

        if(cursor.count == 0){
            throw Exception(context.getString(R.string.error_authorization))
        }

        cursor.moveToFirst()
        user.id = cursor.getInt(0)
        user.username = cursor.getString(1)
        user.name = cursor.getString(2)
        user.birthday = Parse.stringToDate(cursor.getString(3))
        user.email = cursor.getString(4)
        user.pathProfilePhoto = cursor.getString(5)
    }

    fun registration(newUser: Photographer) {
        val cv = ContentValues()
        cv.put("Username", newUser.username)
        cv.put("Name", newUser.name)
        cv.put("Birthday", Parse.dateToString(newUser.birthday))
        cv.put("Email", newUser.email)
        cv.put("Password", newUser.password)

        val cursor: Long = db.insert("Photographers", null, cv)

        val l: Long = -1
        if (cursor == l) {
            throw Exception(context.getString(R.string.error_registration))
        }
    }

    fun checkForUniqueness(photographer: Photographer){
        val id = photographer.id
        val username = photographer.username
        val email = photographer.email

        var queryUsername = "SELECT Id FROM Photographers WHERE Username = '$username' "
        var queryEmail = "SELECT Id FROM Photographers WHERE Email = '$email'"
        if(id != null){
            queryUsername += "AND Id <> '$id'"
            queryEmail += "AND Id <> '$id'"
        }

        var cursor: Cursor = db.rawQuery(queryUsername, null)
        if(cursor.count != 0){
            throw Exception(context.getString(R.string.error_unique_username))
        }

        cursor = db.rawQuery(queryEmail, null)
        if(cursor.count != 0){
            throw Exception(context.getString(R.string.error_unique_email))
        }
    }

    fun editUser(changedUser: Photographer){
        val cv = ContentValues()
        cv.put("Username", changedUser.username)
        cv.put("Name", changedUser.name)
        cv.put("Birthday", Parse.dateToString(changedUser.birthday))
        cv.put("Email", changedUser.email)

        val cursor: Int = db.update("Photographers", cv, "Id=?", arrayOf(user.id.toString()))
        if(cursor == -1){
            throw Exception(context.getString(R.string.error_edit_profile))
        }

        user.username = changedUser.username
        user.name = changedUser.name
        user.birthday = changedUser.birthday
        user.email = changedUser.email
    }

    fun editPathProfilePhoto(pathProfilePhoto: String){
        val cv = ContentValues()
        cv.put("PathProfilePhoto", pathProfilePhoto)

        val cursor: Int = db.update("Photographers", cv, "Id=?", arrayOf(user.id.toString()))
        if(cursor == -1){
            throw Exception(context.getString(R.string.error_edit_profile))
        }

        user.pathProfilePhoto = pathProfilePhoto
    }
}