package com.irinalyamina.appnetworkforphotographers.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.models.Post
import java.io.IOException
import java.sql.SQLException
import kotlin.Exception

class DatabasePost(private var context: Context) {

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

    fun add(newPost: Post, image: Bitmap) {
        val cv = ContentValues()
        cv.put("PathPhoto", newPost.pathPhoto)
        cv.put("Caption", newPost.caption)
        cv.put("UploadDate", Parse.dateToString(newPost.uploadDate))
        cv.put("PhotographerId", newPost.photographerId)

        db.beginTransaction()
        try {
            val id: Long = db.insert("Posts", null, cv)
            if (id == -1L) {
                throw Exception(context.getString(R.string.error_add_post))
            }

            val imageProcessing = ImageProcessing(context)
            val pathPhoto = imageProcessing.savePhotoPost(image, id.toInt())

            val cvPhoto = ContentValues()
            cvPhoto.put("PathPhoto", pathPhoto)

            val count: Int = db.update("Posts", cvPhoto, "Id=?", arrayOf(id.toString()))
            if (count == 0) {
                throw Exception(context.getString(R.string.error_add_photo_post))
            }

            db.setTransactionSuccessful()
        } catch (exp: Exception) {
            throw Exception(exp.message)
        } finally {
            db.endTransaction()
        }
    }

    fun update(post: Post): Int {
        val cv = ContentValues()
        cv.put("PathPhoto", post.pathPhoto)
        cv.put("Caption", post.caption)

        val countRaw = db.update("Posts", cv, "Id=?", arrayOf(post.id.toString()))
        return countRaw
    }

    fun delete(id: Int): Int {
        val countRaw = db.delete("Posts", "Id=?", arrayOf(id.toString()))
        return countRaw
    }
}