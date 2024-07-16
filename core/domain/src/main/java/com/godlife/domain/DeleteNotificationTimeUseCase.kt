package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import com.godlife.model.todo.NotificationTimeData
import com.godlife.network.model.NotificationRequest
import javax.inject.Inject

class DeleteNotificationTimeUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeDeleteNotificationTime(
        authorId: String
    ) = networkRepository.deleteNotificationTime(authorization = authorId)

}