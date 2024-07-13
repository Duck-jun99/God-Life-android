package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeDeletePost(
        authorization: String,
        postId: String,
    ) = networkRepository.deletePost(authorization, postId)
}