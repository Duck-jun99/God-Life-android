package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend fun executeProfileImageUpload(
        authorization: String,
        imagePath: Uri
    ) = networkRepository.profileImageUpload(authorization, imagePath)

    suspend fun executeBackgroundImageUpload(
        authorization: String,
        imagePath: Uri
    ) = networkRepository.backgroundImageUpload(authorization, imagePath)

    suspend fun executeUpdateIntroduce(
        authorization: String,
        introduce: String
    ) = networkRepository.updateIntroduce(authorization, introduce)
}