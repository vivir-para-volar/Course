package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File

class SaveImage {
    companion object {

        private val dirPath = "app/src/main/assets/photos"

        fun savePhotoProfile(context: Context, uri: Uri, id: Int) {
            val file_path = "$dirPath/profile/$id"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()

            val file = File(uri.path)

            file.copyTo(dir, true)

//            val file = File(dir, "profile.png")
//            val fOut = FileOutputStream(file)
//
//            val bmp = uriToBitmap(context, uri)
//
//            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut)
//
//            fOut.flush()
//            fOut.close()
        }

        private fun uriToBitmap(context: Context, uri: Uri): Bitmap {
            return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }
}