package com.godlife.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoList
import com.godlife.model.todo.TodoTimeData

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: TodoTimeData,

    @ColumnInfo(name = "todoList")
    var todoList: List<TodoList>,

    @ColumnInfo(name = "notificationBoolean")
    var notificationBoolean: Boolean,

    @ColumnInfo(name = "notificationTime")
    var notificationTime: NotificationTimeData,

    @ColumnInfo(name = "isCompleted")
    var isCompleted: Boolean,
)