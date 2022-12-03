package com.irinalyamina.appnetworkforphotographers.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import com.irinalyamina.appnetworkforphotographers.models.Post
import java.io.IOException
import java.sql.SQLException
import java.time.LocalDate
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

    fun add(newPost: Post, postPhoto: Bitmap) {
        val cv = ContentValues()
        cv.put("PathPhoto", "pathPhoto")
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
            val pathPhoto = imageProcessing.savePhotoPost(postPhoto, id.toInt())

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

    /*fun update(post: Post, photoPost: Bitmap): Int {
        val cv = ContentValues()
        cv.put("PathPhoto", post.pathPhoto)
        cv.put("Caption", post.caption)

        val countRaw = db.update("Posts", cv, "Id=?", arrayOf(post.id.toString()))
        return countRaw
    }*/

    fun delete(id: Int): Int {
        val countRaw = db.delete("Posts", "Id=?", arrayOf(id.toString()))
        return countRaw
    }

    fun allPhotographerPosts(photographerId: Int): List<Post> {
        val list = arrayListOf<Post>()

        val query = "SELECT * FROM Posts WHERE PhotographerId = '$photographerId'"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(0)
            val pathPhoto = cursor.getString(1)
            val caption = cursor.getString(2)
            val uploadDate = Parse.stringToDate(cursor.getString(3))
            val photographerId = cursor.getInt(4)

            val imageProcessing = ImageProcessing(context)
            val photo = imageProcessing.getPhoto(pathPhoto)

            val post = Post(id, photo, caption, uploadDate, photographerId)
            list.add(post)
        }

        return list
    }
}