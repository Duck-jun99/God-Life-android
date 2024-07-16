package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeUpdatePost(
        authorization: String,
        postId: String,
        title: String,
        content: String,
        categoryType: String?,
        tags: List<String>,
        imagePath: List<Uri>?
    ) = networkRepository.updatePost(authorization, postId, title, content, categoryType, tags, imagePath)
}