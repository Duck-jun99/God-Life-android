package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeDeletePost(
        postId: String,
    ) = networkRepository.deletePost(postId)
}