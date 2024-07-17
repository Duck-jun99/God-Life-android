package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import com.godlife.model.todo.NotificationTimeData
import com.godlife.network.model.NotificationRequest
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeRegisterFCMToken(
        authorId: String,
        fcmToken: String
    ) = networkRepository.registerFcmToken(authorization = authorId, fcmToken = fcmToken)

}