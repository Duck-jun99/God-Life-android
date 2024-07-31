package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetPostDetail(
        postId: String
    ) = networkRepository.getPostDetail(postId = postId)

    suspend fun executeGetStimulusPostDetail(
        postId: String
    ) = networkRepository.getStimulusPostDetail(boardId = postId)
}