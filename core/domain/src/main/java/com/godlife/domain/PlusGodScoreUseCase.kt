package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class PlusGodScoreUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executePlusGodScore(
        postId: Int,
    ) = networkRepository.agreeGodLife(postId)
}