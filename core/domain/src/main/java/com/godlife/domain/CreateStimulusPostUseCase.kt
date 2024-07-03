package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import com.godlife.network.model.CreatePostRequest
import javax.inject.Inject

class CreateStimulusPostUseCase  @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend fun executeCreateStimulusPostTemp(
        authorization: String
    ) = networkRepository.createStimulusPostTemp(authorization)

    suspend fun executeUploadStimulusPostImage(
        authorization: String,
        tmpBoardId: Int,
        image: Uri
    ) = networkRepository.uploadStimulusPostImage(authorization, tmpBoardId, image)

    suspend fun executeCreateStimulusPost(
        authorization: String,
        stimulusPostBody: CreatePostRequest
    ) = networkRepository.createStimulusPost(authorization, stimulusPostBody)

}