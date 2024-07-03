package com.godlife.database.util

import androidx.room.TypeConverter
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoTimeData


class TodoTimeDataConverter{
    @TypeConverter
    fun fromTodoTimeData(todoTimeData: TodoTimeData): String {
        return "${todoTimeData.y} ${todoTimeData.m} ${todoTimeData.d}"
    }

    @TypeConverter
    fun toTodoTimeData(todoTimeDataString: String): TodoTimeData {
        val parts = todoTimeDataString.split(" ")
        return TodoTimeData(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
    }

}


class NotificationTimeDataConverter {
    @TypeConverter
    fun fromNotificationTimeData(notificationTimeData: NotificationTimeData): String {
        return "${notificationTimeData.y} ${notificationTimeData.m} ${notificationTimeData.d} ${notificationTimeData.hour} ${notificationTimeData.minute}"
    }

    @TypeConverter
    fun toNotificationTimeData(notificationTimeDataString: String): NotificationTimeData {
        val parts = notificationTimeDataString.split(" ")
        return NotificationTimeData(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt())
    }
}