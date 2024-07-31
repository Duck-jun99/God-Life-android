package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import com.godlife.network.model.CreatePostRequest
import javax.inject.Inject

class UpdateStimulusPostUseCase  @Inject constructor(
    private val networkRepository: NetworkRepository
): CreateStimulusPostUseCase(networkRepository) {
    suspend fun executeUpdateStimulusPost(
        stimulusPostBody: CreatePostRequest
    ) = networkRepository.updateStimulusPost(stimulusPostBody = stimulusPostBody)

    override suspend fun executeUploadStimulusPostImage(
        boardId: Int,
        image: Uri
    ) = networkRepository.uploadStimulusPostImage(boardId, image)

}