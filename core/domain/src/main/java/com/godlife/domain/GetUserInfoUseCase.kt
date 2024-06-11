package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    //suspend fun execute(remoteErrorEmitter: RemoteErrorEmitter, id : String) = notionRepository.getPortfolio(remoteErrorEmitter, id)
    suspend fun executeGetUserInfo(authorization : String) = networkRepository.getUserInfo(authorization)

}