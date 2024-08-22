package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetNotificationListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetNotificationList(
    ) = networkRepository.getNotificationList()
}