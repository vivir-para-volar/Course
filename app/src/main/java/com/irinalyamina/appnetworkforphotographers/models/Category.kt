package com.irinalyamina.appnetworkforphotographers.models

class Category {
    var id: Int
    var name: String
    var categoryDirectoryId: Int

    constructor(id: Int, name: String, categoryDirectoryId: Int) {
        this.id = id
        this.name = name
        this.categoryDirectoryId = categoryDirectoryId
    }

    override fun toString(): String {
        return name
    }
}