package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetComments(
        postId: String
    ) = networkRepository.getComments(postId)
}