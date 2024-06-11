package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetComments(
        authorization: String,
        postId: String
    ) = networkRepository.getComments(authorization, postId)
}