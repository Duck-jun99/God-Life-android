package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetWeeklyFamousPostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetWeeklyFamousPost(authorId: String) = networkRepository.getWeeklyFamousPost(authorization = authorId)
}