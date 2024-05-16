package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeCheckNickname(nickname : String) = networkRepository.checkNickname(nickname)
    suspend fun executeCheckEmail(email : String) = networkRepository.checkEmail(email)

}