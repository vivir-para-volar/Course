package com.irinalyamina.appnetworkforphotographers.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageProcessing (private var context: Context) {

    private lateinit var mainDir: String

    init {
        try {
            mainDir = context.applicationInfo.dataDir
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
        }
    }

    fun savePhotoProfile(photoProfile: Bitmap, id: Int): String {
        val dir = File("$mainDir/photos/profile/$id")
        val fileName = "profile.jpg"

        try{
            writeImage(photoProfile, dir, fileName)
        }
        catch (exp: Exception){
            throw Exception(context.getString(R.string.error_edit_profile_photo))
        }

        return "/photos/profile/$id/$fileName"
    }

    fun savePhotoPost(bitmap: Bitmap, id: Int): String {
        val dir = File("$mainDir/photos/post/$id")
        val fileName = "post.jpg"

        try{
            writeImage(bitmap, dir, fileName)
        }
        catch (exp: Exception){
            throw Exception(context.getString(R.string.error_add_photo_post))
        }

        return "/photos/post/$id/$fileName"
    }

    fun getPhoto(path: String): Bitmap {
        val dir = File("$mainDir/$path")

        try{
            val content = ByteArray(dir.length().toInt())

            val stream = FileInputStream(dir)
            stream.read(content)

            return BitmapFactory.decodeByteArray(content, 0, content.size)
        }
        catch (exp: Exception){
            throw Exception(context.getString(R.string.error_get_photo))
        }
    }

    private fun writeImage(bitmap: Bitmap, dir: File, fileName: String){
        try {
            if(!dir.exists()){
                dir.mkdirs()
            }

            val writer = FileOutputStream(File(dir, fileName))

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageArrByte = baos.toByteArray()

            writer.write(imageArrByte)
            writer.close()
        } catch (exp: Exception) {
            throw Exception(exp.message)
        }
    }
}