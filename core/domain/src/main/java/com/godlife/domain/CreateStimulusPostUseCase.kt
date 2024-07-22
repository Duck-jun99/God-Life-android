package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import com.godlife.network.model.CreatePostRequest
import javax.inject.Inject

class CreateStimulusPostUseCase  @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend fun executeCreateStimulusPostTemp(
    ) = networkRepository.createStimulusPostTemp()

    suspend fun executeUploadStimulusPostImage(
        tmpBoardId: Int,
        image: Uri
    ) = networkRepository.uploadStimulusPostImage(tmpBoardId, image)

    suspend fun executeCreateStimulusPost(
        stimulusPostBody: CreatePostRequest
    ) = networkRepository.createStimulusPost(stimulusPostBody)

}