package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetFamousPostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetWeeklyFamousPost() = networkRepository.getWeeklyFamousPost()

    suspend fun executeGetAllFamousPost() = networkRepository.getAllFamousPost()
}