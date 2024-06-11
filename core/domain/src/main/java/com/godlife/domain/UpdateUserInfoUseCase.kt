package com.godlife.domain

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeImageUpload(
        authorization: String,
        imageType: String,
        imagePath: Uri
    ) = networkRepository.imageUpload(authorization, imageType, imagePath)

    suspend fun executeUpdateIntroduce(
        authorization: String,
        introduce: String
    ) = networkRepository.updateIntroduce(authorization, introduce)
}