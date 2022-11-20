package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
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

    fun savePhotoProfile(uri: Uri, id: Int): String {
        val dir = File("$mainDir/profile/$id")
        val fileName = "profile.jpg"

        try{
            writeImage(uriToBitmap(uri), dir, fileName)
        }
        catch (exp: Exception){
            throw Exception(context.getString(R.string.error_edit_profile_photo))
        }

        return "/profile/$id/profile.jpg"
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
            throw Exception(context.getString(R.string.error_get_profile_photo))
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
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