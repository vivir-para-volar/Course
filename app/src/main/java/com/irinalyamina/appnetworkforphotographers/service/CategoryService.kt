package com.irinalyamina.appnetworkforphotographers.service

import android.content.Context
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.database.DatabaseCategory
import com.irinalyamina.appnetworkforphotographers.models.Category
import com.irinalyamina.appnetworkforphotographers.models.CategoryDirectory

class CategoryService(private var context: Context) {

    private lateinit var database: DatabaseCategory

    init {
        try {
            database = DatabaseCategory(context)
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
        }
    }

    fun getAllCategoriesWithDirectories(): LinkedHashMap<CategoryDirectory, ArrayList<Category>>? {
        return try {
            val listCategoryDirectories = database.allCategoryDirectories()
            val listCategories = database.allCategories()

            val hashMap = LinkedHashMap<CategoryDirectory, ArrayList<Category>>()

            for (categoryDirectory in listCategoryDirectories) {
                val list = arrayListOf<Category>()

                for (category in listCategories) {
                    if (category.categoryDirectoryId == categoryDirectory.id) {
                        list.add(category)
                    }
                }

                hashMap[categoryDirectory] = list
            }

            hashMap
        } catch (exp: Exception) {
            ShowMessage.toast(context, exp.message)
            null
        }
    }
}