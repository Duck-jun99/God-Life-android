package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend fun executeProfileImageUpload(
        imagePath: Uri
    ) = networkRepository.profileImageUpload(imagePath)

    suspend fun executeBackgroundImageUpload(
        imagePath: Uri
    ) = networkRepository.backgroundImageUpload(imagePath)

    suspend fun executeUpdateIntroduce(
        introduce: String
    ) = networkRepository.updateIntroduce(introduce)
}