package com.godlife.data

import com.godlife.data.repository.NetworkRepository
import com.godlife.network.NetworkDataSource
import com.godlife.network.model.NetworkUserQuery
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

}