package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeCreateComment(
        authorization: String,
        postId: String,
        comment: String
    ) = networkRepository.createComment(authorization, postId, comment)
}