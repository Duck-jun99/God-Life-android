package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeLogout() = networkRepository.logout()
}