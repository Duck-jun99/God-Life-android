package com.godlife.domain

import com.godlife.data.repository.LocalPreferenceUserRepository
import javax.inject.Inject

class LocalPreferenceUserUseCase @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository) {

    fun getAccessToken(): String = localPreferenceUserRepository.getAccessToken()

    fun saveAccessToken(accessToken: String) = localPreferenceUserRepository.saveAccessToken(accessToken)

    fun getUserNickname(): String = localPreferenceUserRepository.getUserNickname()

    fun getUserId(): Int = localPreferenceUserRepository.getUserId()

    fun saveUserId(userId: Int) = localPreferenceUserRepository.saveUserId(userId)

    fun saveUserNickname(userNickname: String) = localPreferenceUserRepository.saveUserNickname(userNickname)

    fun removeAccessToken() = localPreferenceUserRepository.removeAccessToken()

    fun removeUserNickname() = localPreferenceUserRepository.removeUserNickname()
}