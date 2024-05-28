package com.godlife.data

import android.graphics.Bitmap
import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import com.godlife.network.NetworkDataSource
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.PostQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : NetworkRepository {
    override suspend fun getUserInfo(
        id: String
    ): UserExistenceCheckResult? {
        //return Mapper.mapperGithub(githubDataSource.getGithub(remoteErrorEmitter, owner))
        return networkDataSource.getUserInfo(id)
    }

    override suspend fun checkNickname(nickname: String): SignUpCheckNicknameQuery? {
        return networkDataSource.checkNickname(nickname)
    }

    override suspend fun checkEmail(email: String): SignUpCheckEmailQuery? {
        return networkDataSource.checkEmail(email)
    }

    override suspend fun signUp(
        nickname: String,
        email: String,
        age: Int,
        sex: String,
        providerId: String,
        providerName: String
    ): SignUpQuery {
        return networkDataSource.signUp(nickname, email, age, sex, providerId, providerName)
    }

    override suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): PostQuery {
        return networkDataSource.createPost(authorization, title, content, tags, imagePath)
    }

    override suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String
    ): LatestPostQuery {
        return networkDataSource.getLatestPost(authorization, page, keyword, tag)
    }

}