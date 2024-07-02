package com.godlife.domain

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.LatestStimulusPostRepository
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetLatestStimulusPostUseCase @Inject constructor(
    private val latestStimulusPostRepository: LatestStimulusPostRepository
) {
    fun executeGetLatestStimulusPost() = latestStimulusPostRepository.getLatestStimulusPost()
}