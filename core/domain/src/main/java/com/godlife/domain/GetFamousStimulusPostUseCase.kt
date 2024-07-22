package com.godlife.domain

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.LatestStimulusPostRepository
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetFamousStimulusPostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetFamousStimulusPost(
    ) = networkRepository.getStimulusFamousPost()
}