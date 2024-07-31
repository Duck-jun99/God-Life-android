package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class ReissueUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeReissue() = networkRepository.reissue()

}