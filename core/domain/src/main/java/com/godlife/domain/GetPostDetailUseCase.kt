package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetPostDetail(authorId: String, postId: String) = networkRepository.getPostDetail(authorization = authorId, postId = postId)

    suspend fun executeGetStimulusPostDetail(authorId: String, postId: String) = networkRepository.getStimulusPostDetail(authorization = authorId, boardId = postId)
}