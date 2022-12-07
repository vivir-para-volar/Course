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
import java.util.*

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

    fun allPhotographerPosts(photographerId: Int): ArrayList<Post> {
        val list: ArrayList<Post> = arrayListOf()

        val photographer = getPhotographerById(photographerId)

        val query = "SELECT * FROM Posts WHERE PhotographerId = '$photographerId' ORDER BY Id DESC"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val pathPhoto = cursor.getString(1)
            val caption = cursor.getString(2)
            val uploadDate = Parse.stringToDate(cursor.getString(3))
            val photographerId = cursor.getInt(4)

            val imageProcessing = ImageProcessing(context)
            val photo = imageProcessing.getPhoto(pathPhoto)

            val post = Post(
                id,
                photo,
                caption,
                uploadDate,
                photographerId,
                photographer.username,
                photographer.profilePhoto
            )
            list.add(post)
        }

        return list
    }

    fun allPosts(): ArrayList<Post> {
        val list: ArrayList<Post> = arrayListOf()

        val query = "SELECT * FROM Posts ORDER BY Id DESC"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val pathPhoto = cursor.getString(1)
            val caption = cursor.getString(2)
            val uploadDate = Parse.stringToDate(cursor.getString(3))
            val photographerId = cursor.getInt(4)

            val imageProcessing = ImageProcessing(context)
            val photo = imageProcessing.getPhoto(pathPhoto)

            val photographer = getPhotographerById(photographerId)

            val post = Post(
                id,
                photo,
                caption,
                uploadDate,
                photographerId,
                photographer.username,
                photographer.profilePhoto
            )
            list.add(post)
        }
        return list
    }

    private fun getPhotographerById(id: Int): Photographer {
        val query = "SELECT Username, PathProfilePhoto FROM Photographers WHERE Id = '$id'"

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.count == 0) {
            throw java.lang.Exception(context.getString(R.string.error_get_by_id))
        }

        cursor.moveToFirst()

        val username = cursor.getString(0)
        val pathProfilePhoto = cursor.getString(1)

        var profilePhoto: Bitmap? = null
        if (pathProfilePhoto != null) {
            val imageProcessing = ImageProcessing(context)
            profilePhoto = imageProcessing.getPhoto(pathProfilePhoto)
        }

        return Photographer(id, username, "", LocalDate.now(), "", profilePhoto)
    }
}