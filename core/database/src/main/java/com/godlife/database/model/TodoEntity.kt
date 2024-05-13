package com.godlife.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "todoList")
    val todoList: List<String>,

    @ColumnInfo(name = "endTime")
    val endTime: List<String>,

    @ColumnInfo(name = "notificationTime")
    val notificationTime: List<String>
)