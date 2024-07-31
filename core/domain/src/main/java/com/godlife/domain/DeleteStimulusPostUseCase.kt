package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class DeleteStimulusPostUseCase  @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeDeleteStimulusPost(
        boardId: String
    ) = networkRepository.deleteStimulusPost(boardId)


}