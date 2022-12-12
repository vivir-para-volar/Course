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
import com.irinalyamina.appnetworkforphotographers.models.PostComment
import java.io.IOException
import java.sql.SQLException
import java.time.LocalDate
import kotlin.collections.ArrayList

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

    fun add(newPost: Post, postPhoto: Bitmap): Long {
        val cv = ContentValues()
        cv.put("PathPhoto", "pathPhoto")
        cv.put("Caption", newPost.caption)
        cv.put("UploadDate", Parse.dateTimeToString(newPost.uploadDate))
        cv.put("PhotographerId", newPost.photographerId)
        cv.put("CategoryId", newPost.categoryId)

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

            return id
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
    }

    fun delete(id: Int): Int {
        val countRaw = db.delete("Posts", "Id=?", arrayOf(id.toString()))
        return countRaw
    }*/


    fun addLike(postId: Int, photographerId: Int): Long {
        val cv = ContentValues()
        cv.put("PostId", postId)
        cv.put("PhotographerId", photographerId)

        val id: Long = db.insert("PostLikes", null, cv)

        if (id == -1L) {
            throw Exception(context.getString(R.string.error_add_like))
        }

        return id
    }

    fun deleteLike(postId: Int, photographerId: Int) {
        val countRaw = db.delete(
            "PostLikes",
            "PostId = ? AND PhotographerId = ?",
            arrayOf(postId.toString(), photographerId.toString())
        )

        if (countRaw != 1) {
            throw Exception(context.getString(R.string.error_delete_like))
        }
    }

    fun addPostComment(postComment: PostComment): Long {
        val cv = ContentValues()
        cv.put("Date", Parse.dateTimeToString(postComment.date))
        cv.put("Text", postComment.text)
        cv.put("PostId", postComment.postId)
        cv.put("PhotographerId", postComment.photographerId)

        val id: Long = db.insert("PostComments", null, cv)

        if (id == -1L) {
            throw Exception(context.getString(R.string.error_add_like))
        }

        return id
    }


    fun getPhotographerPosts(photographerId: Int): ArrayList<Post> {
        val photographer = getPhotographerById(photographerId)

        val query = "SELECT * FROM Posts WHERE PhotographerId = '$photographerId' ORDER BY Id DESC"
        val cursor: Cursor = db.rawQuery(query, null)

        return getListPosts(cursor, photographer)
    }

    fun getPosts(): ArrayList<Post> {
        val query = "SELECT * FROM Posts ORDER BY Id DESC"
        val cursor: Cursor = db.rawQuery(query, null)

        return getListPosts(cursor)
    }

    fun getPostComments(postId: Int): ArrayList<PostComment>{
        val list = arrayListOf<PostComment>()

        val query = "SELECT * FROM PostComments WHERE PostId = '$postId'"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val date = Parse.stringToDateTime(cursor.getString(1))
            val text = cursor.getString(2)
            val postId = cursor.getInt(3)
            val photographerId = cursor.getInt(4)

            val photographer = getPhotographerById(photographerId)

            val postComment = PostComment(
                id,
                date,
                text,
                postId,
                photographerId,
                photographer.username,
                photographer.profilePhoto
            )
            list.add(postComment)
        }
        return list
    }

    private fun getListPosts(cursor: Cursor, photographer: Photographer? = null): ArrayList<Post> {
        val list: ArrayList<Post> = arrayListOf()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val pathPhoto = cursor.getString(1)
            val caption = cursor.getString(2)
            val uploadDate = Parse.stringToDateTime(cursor.getString(3))
            val photographerId = cursor.getInt(4)
            val categoryId = cursor.getInt(5)

            val imageProcessing = ImageProcessing(context)
            val photo = imageProcessing.getPhoto(pathPhoto)

            val photographerTemp = photographer ?: getPhotographerById(photographerId)

            val post = Post(
                id,
                photo,
                caption,
                uploadDate,
                photographerId,
                categoryId,
                getAllLikes(id),
                getCountPostComments(id),
                photographerTemp.username,
                photographerTemp.profilePhoto
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

        return Photographer(id, username, "", LocalDate.now(), "", profilePhoto, null)
    }

    private fun getAllLikes(postId: Int): ArrayList<Int> {
        val list = arrayListOf<Int>()

        val query = "SELECT PhotographerId FROM PostLikes WHERE PostId = '$postId'"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val photographerId = cursor.getInt(0)
            list.add(photographerId)
        }
        return list
    }

    private fun getCountPostComments(postId: Int): Int{
        val query = "SELECT COUNT(*) FROM PostComments WHERE PostId = '$postId'"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.moveToNext()
        val count = cursor.getInt(0)
        return count
    }
}