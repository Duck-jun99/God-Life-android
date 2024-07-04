package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetFamousPostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetWeeklyFamousPost(authorId: String) = networkRepository.getWeeklyFamousPost(authorization = authorId)

    suspend fun executeGetAllFamousPost(authorId: String) = networkRepository.getAllFamousPost(authorization = authorId)
}