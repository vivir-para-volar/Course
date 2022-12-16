package com.irinalyamina.appnetworkforphotographers.models

import android.graphics.Bitmap
import java.time.LocalDate
import java.time.LocalDateTime

class Photographer {
    var id: Int
    var username: String
    var name: String
    var birthday: LocalDate
    var email: String
    var password: String?
    var profilePhoto: Bitmap?
    var lastLoginDate: LocalDateTime

    var profileDescription: String? = null
    var photographyEquipment: String? = null
    var photographyAwards: String? = null

    constructor(
        username: String,
        name: String,
        birthday: LocalDate,
        email: String,
        password: String
    ) {
        this.username = username
        this.name = name
        this.birthday = birthday
        this.email = email
        this.password = password
        this.lastLoginDate = LocalDateTime.now()

        this.id = -1
        this.profilePhoto = null
    }

    constructor(
        id: Int,
        username: String,
        name: String,
        birthday: LocalDate,
        email: String,
        pathProfilePhoto: Bitmap?,
        lastLoginDate: LocalDateTime
    ) {
        this.id = id
        this.username = username
        this.name = name
        this.birthday = birthday
        this.email = email
        this.profilePhoto = pathProfilePhoto
        this.lastLoginDate = lastLoginDate

        this.password = null
    }
}