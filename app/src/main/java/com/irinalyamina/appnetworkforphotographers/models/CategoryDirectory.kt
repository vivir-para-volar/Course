package com.irinalyamina.appnetworkforphotographers.models

class CategoryDirectory {
    var id: Int
    var name: String

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    override fun toString(): String {
        return name
    }
}