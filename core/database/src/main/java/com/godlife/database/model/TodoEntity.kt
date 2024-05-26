package com.godlife.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoList

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "todoList")
    var todoList: List<TodoList>,

    @ColumnInfo(name = "endTime")
    val endTime: EndTimeData,

    @ColumnInfo(name = "notificationTime")
    val notificationTime: NotificationTimeData
)