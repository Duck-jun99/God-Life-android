package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetRankingUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetWeeklyRanking() = networkRepository.getWeeklyFamousMembers()

    suspend fun executeGetAllRanking() = networkRepository.getAllFamousMembers()
}