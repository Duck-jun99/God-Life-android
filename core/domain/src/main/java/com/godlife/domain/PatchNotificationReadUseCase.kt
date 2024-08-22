package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class PatchNotificationReadUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executePatchNotificationRead(alarmId: Int)
    = networkRepository.patchNotificationRead(alarmId)
}