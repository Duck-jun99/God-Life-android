package com.godlife.database.util

import androidx.room.TypeConverter
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData

class EndTimeDataConverter {
    @TypeConverter
    fun fromEndTimeData(endTimeData: EndTimeData): String {
        return "${endTimeData.y} ${endTimeData.m} ${endTimeData.d} ${endTimeData.hour} ${endTimeData.minute}"
    }

    @TypeConverter
    fun toEndTimeData(endTimeDataString: String): EndTimeData {
        val parts = endTimeDataString.split(" ")
        return EndTimeData(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt())
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
