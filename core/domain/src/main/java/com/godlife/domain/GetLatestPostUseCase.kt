package com.godlife.domain

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetLatestPostUseCase @Inject constructor(
    private val latestPostRepository: LatestPostRepository
) {
    fun executeGetLatestPost() = latestPostRepository.getLatestPost()
}