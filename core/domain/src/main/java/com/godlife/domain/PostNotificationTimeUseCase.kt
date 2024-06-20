package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import com.godlife.model.todo.NotificationTimeData
import com.godlife.network.model.NotificationRequest
import javax.inject.Inject

class PostNotificationTimeUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executePostNotificationTime(
        authorId: String,
        notificationTime: NotificationTimeData
    ) = networkRepository.postNotificationTime(authorization = authorId, notificationTime = NotificationRequest(notificationTime.y, notificationTime.m, notificationTime.d, notificationTime.hour, notificationTime.minute))

}