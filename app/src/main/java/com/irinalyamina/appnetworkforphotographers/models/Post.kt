package com.irinalyamina.appnetworkforphotographers.models

import android.graphics.Bitmap
import java.time.LocalDateTime
import java.util.ArrayList

class Post {
    var id: Int
    var photo: Bitmap
    var caption: String
    var uploadDate: LocalDateTime
    var photographerId: Int
    var categoryId: Int

    var listLikes: ArrayList<Int>
    var listComments: ArrayList<PostComment>
    var countComments: Int

    var photographerUsername: String
    var photographerProfilePhoto: Bitmap?

    constructor(caption: String, photographerId: Int, categoryId: Int) {
        this.id = -1
        this.photo = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        this.caption = caption
        this.uploadDate = LocalDateTime.now()
        this.photographerId = photographerId
        this.categoryId = categoryId

        this.listLikes = arrayListOf()
        this.listComments = arrayListOf()
        this.countComments = 0

        this.photographerUsername = ""
        this.photographerProfilePhoto = null
    }

    constructor(
        id: Int,
        photo: Bitmap,
        caption: String,
        uploadDate: LocalDateTime,
        photographerId: Int,
        categoryId: Int,
        listLikes: ArrayList<Int>,
        countComments: Int,
        photographerUsername: String,
        photographerProfilePhoto: Bitmap?
    ) {
        this.id = id
        this.photo = photo
        this.caption = caption
        this.uploadDate = uploadDate
        this.photographerId = photographerId
        this.categoryId = categoryId

        this.listLikes = listLikes
        this.listComments = arrayListOf()
        this.countComments = countComments

        this.photographerUsername = photographerUsername
        this.photographerProfilePhoto = photographerProfilePhoto
    }
}