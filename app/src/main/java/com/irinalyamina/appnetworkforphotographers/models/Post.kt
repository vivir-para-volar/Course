package com.irinalyamina.appnetworkforphotographers.models

import java.time.LocalDate

class Post {
    var id: Int?
    var pathPhoto: String
    var caption: String
    var uploadDate: LocalDate
    var photographerId: Int

    constructor(caption: String, photographerId: Int) {
        this.id = null
        this.pathPhoto = "temp"
        this.caption = caption
        this.uploadDate = LocalDate.now()
        this.photographerId = photographerId
    }

    constructor(id: Int, pathPhoto: String, caption: String, uploadDate: LocalDate, photographerId: Int) {
        this.id = id
        this.pathPhoto = pathPhoto
        this.caption = caption
        this.uploadDate = uploadDate
        this.photographerId = photographerId
    }
}