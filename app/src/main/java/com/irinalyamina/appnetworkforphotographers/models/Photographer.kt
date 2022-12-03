package com.irinalyamina.appnetworkforphotographers.models

import android.graphics.Bitmap
import java.time.LocalDate

class Photographer {
    var id: Int
    var username: String
    var name: String
    var birthday: LocalDate
    var email: String
    var password: String?
    var profilePhoto: Bitmap?

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

        this.id = -1
        this.profilePhoto = null
    }

    constructor(
        id: Int,
        username: String,
        name: String,
        birthday: LocalDate,
        email: String,
        pathProfilePhoto: Bitmap?
    ) {
        this.id = id
        this.username = username
        this.name = name
        this.birthday = birthday
        this.email = email
        this.profilePhoto = pathProfilePhoto

        this.password = null
    }
}