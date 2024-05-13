package com.godlife.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val todoList: List<String>,
    val endTime: EndTimeData
)