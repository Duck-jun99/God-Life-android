package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeDeleteComment(
        commentId: String,
    ) = networkRepository.deleteComment(commentId)
}