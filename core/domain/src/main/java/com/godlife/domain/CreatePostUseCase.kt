package com.godlife.domain

import android.graphics.Bitmap
import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeCreatePost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ) = networkRepository.createPost(authorization, title, content, tags, imagePath)
}