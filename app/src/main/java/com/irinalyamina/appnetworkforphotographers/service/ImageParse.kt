package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

class ImageParse (private var context: Context){

    fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
}
