package com.godlife.domain

import android.graphics.Bitmap
import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeCreatePost(
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ) = networkRepository.createPost(title, content, tags, imagePath)
}