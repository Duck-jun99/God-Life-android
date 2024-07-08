package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetRankingUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetWeeklyRanking(authorId: String) = networkRepository.getWeeklyFamousMembers(authorization = authorId)

    suspend fun executeGetAllRanking(authorId: String) = networkRepository.getAllFamousMembers(authorization = authorId)
}