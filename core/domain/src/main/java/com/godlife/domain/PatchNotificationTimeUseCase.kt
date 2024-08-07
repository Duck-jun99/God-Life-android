package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import com.godlife.model.todo.NotificationTimeData
import com.godlife.network.model.NotificationRequest
import javax.inject.Inject

class PatchNotificationTimeUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executePatchNotificationTime(
        notificationTime: NotificationTimeData
    ) = networkRepository.patchNotificationTime(notificationTime = NotificationRequest(notificationTime.y, notificationTime.m, notificationTime.d, notificationTime.hour, notificationTime.minute))

}