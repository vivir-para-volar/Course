package com.irinalyamina.appnetworkforphotographers.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.models.Category
import com.irinalyamina.appnetworkforphotographers.models.CategoryDirectory
import java.io.IOException
import java.sql.SQLException
import java.util.ArrayList

class DatabaseCategory (private var context: Context) {

    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    private var db: SQLiteDatabase

    init {
        try {
            dbHelper.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error(context.getString(R.string.error_update_database))
        }
        db = try {
            dbHelper.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
    }

    fun allCategoryDirectories(): ArrayList<CategoryDirectory> {
        val list = arrayListOf<CategoryDirectory>()

        val query = "SELECT * FROM CategoryDirectories ORDER BY Name"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)

            val categoryDirectories = CategoryDirectory(id, name)
            list.add(categoryDirectories)
        }
        return list
    }

    fun allCategories(): ArrayList<Category> {
        val list = arrayListOf<Category>()

        val query = "SELECT * FROM Categories ORDER BY Name"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val categoryDirectoryId = cursor.getInt(2)

            val category = Category(id, name, categoryDirectoryId)
            list.add(category)
        }
        return list
    }
}