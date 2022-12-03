package com.irinalyamina.appnetworkforphotographers.models

import android.graphics.Bitmap
import java.time.LocalDate

class Post {
    var id: Int
    var photo: Bitmap
    var caption: String
    var uploadDate: LocalDate
    var photographerId: Int

    constructor(caption: String, photographerId: Int) {
        this.id = -1
        this.photo = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        this.caption = caption
        this.uploadDate = LocalDate.now()
        this.photographerId = photographerId
    }

    constructor(id: Int, photo: Bitmap, caption: String, uploadDate: LocalDate, photographerId: Int) {
        this.id = id
        this.photo = photo
        this.caption = caption
        this.uploadDate = uploadDate
        this.photographerId = photographerId
    }
}