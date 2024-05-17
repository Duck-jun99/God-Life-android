package com.godlife.domain

import com.godlife.data.repository.LocalPreferenceUserRepository
import javax.inject.Inject

class LocalPreferenceUserUseCase @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository) {

    fun getAccessToken(): String = localPreferenceUserRepository.getAccessToken()

    fun saveAccessToken(accessToken: String) = localPreferenceUserRepository.saveAccessToken(accessToken)

    fun removeAccessToken() = localPreferenceUserRepository.removeAccessToken()

    fun getRefreshToken(): String = localPreferenceUserRepository.getRefreshToken()

    fun saveRefreshToken(refreshToken: String) = localPreferenceUserRepository.saveRefreshToken(refreshToken)

    fun removeRefreshToken() = localPreferenceUserRepository.removeRefreshToken()

    fun getUserId(): String = localPreferenceUserRepository.getUserId()

    fun saveUserId(userId: String) = localPreferenceUserRepository.saveUserId(userId)

    fun removeUserId() = localPreferenceUserRepository.removeUserId()


}