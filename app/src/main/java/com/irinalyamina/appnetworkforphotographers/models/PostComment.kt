package com.irinalyamina.appnetworkforphotographers.models

import android.graphics.Bitmap
import java.time.LocalDate
import java.time.LocalDateTime

class PostComment {
    var id: Int
    var date: LocalDateTime
    var text: String
    var postId: Int
    var photographerId: Int

    var photographerUsername: String
    var photographerProfilePhoto: Bitmap?

    constructor(text: String, postId: Int, photographerId: Int, photographerUsername: String, photographerProfilePhoto: Bitmap?) {
        this.id = -1
        this.date = LocalDateTime.now()
        this.text = text
        this.postId = postId
        this.photographerId = photographerId

        this.photographerUsername = photographerUsername
        this.photographerProfilePhoto = photographerProfilePhoto
    }

    constructor(
        id: Int,
        date: LocalDateTime,
        text: String,
        postId: Int,
        photographerId: Int,
        photographerUsername: String,
        photographerProfilePhoto: Bitmap?
    ) {
        this.id = id
        this.date = date
        this.text = text
        this.postId = postId
        this.photographerId = photographerId
        this.photographerUsername = photographerUsername
        this.photographerProfilePhoto = photographerProfilePhoto
    }
}