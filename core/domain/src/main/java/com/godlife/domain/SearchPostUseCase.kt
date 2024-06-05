package com.godlife.domain

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.NetworkRepository
import com.godlife.data.repository.SearchPostRepository
import javax.inject.Inject

class SearchPostUseCase @Inject constructor(
    private val searchPostRepository: SearchPostRepository
) {
    fun executeSearchPost(
        keyword: String,
        tags: String,
        nickname: String
    ) = searchPostRepository.getSearchedPost(keyword, tags, nickname)
}