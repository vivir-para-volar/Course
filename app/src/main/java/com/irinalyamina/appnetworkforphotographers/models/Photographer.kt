package com.irinalyamina.appnetworkforphotographers.models

import java.time.LocalDate

class Photographer {
    var id: Int?
    var username: String
    var name: String
    var birthday: LocalDate
    var email: String
    var password: String?
    var pathProfilePhoto: String?

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

        this.id = null
        this.pathProfilePhoto = null
    }

    constructor(
        id: Int,
        username: String,
        name: String,
        birthday: LocalDate,
        email: String,
        pathProfilePhoto: String?
    ) {
        this.id = id
        this.username = username
        this.name = name
        this.birthday = birthday
        this.email = email
        this.pathProfilePhoto = pathProfilePhoto

        this.password = null
    }
}