package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import com.godlife.network.model.LatestPostQuery
import javax.inject.Inject

class GetLatestPostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
    ) = networkRepository.getLatestPost(authorization, page, keyword, tag)
}