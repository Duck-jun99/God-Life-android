package com.godlife.network.model

data class NotificationListQuery(
    val status: String,
    val body: List<NotificationListBody>,
    val message: String
)

data class NotificationListBody(
    val alarmId: Int,
    val boardId: Int,
    val title: String,
    val content: String
)