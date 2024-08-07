package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeGetUserProfile(
        memberId : String
    ) = networkRepository.getUserProfile(memberId)

}