package com.godlife.data

import com.godlife.data.repository.NetworkRepository
import com.godlife.network.NetworkDataSource
import com.godlife.network.model.NetworkUserQuery
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : NetworkRepository {
    override suspend fun getUserInfo(
        id: String
    ): NetworkUserQuery? {
        //return Mapper.mapperGithub(githubDataSource.getGithub(remoteErrorEmitter, owner))
        return networkDataSource.getUserInfo(id)
    }

    override suspend fun checkNickname(nickname: String): SignUpCheckNicknameQuery? {
        return networkDataSource.checkNickname(nickname)
    }

    override suspend fun checkEmail(email: String): SignUpCheckEmailQuery? {
        return networkDataSource.checkEmail(email)
    }

}