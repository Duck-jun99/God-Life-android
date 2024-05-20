package com.godlife.database.util

import androidx.room.TypeConverter
import com.godlife.model.todo.TodoList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun fromString(value: String): List<TodoList> {
        val listType = object : TypeToken<List<TodoList>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<TodoList>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun listToJson(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        return Gson().fromJson(value,Array<String>::class.java)?.toList()
    }
}