package com.godlife.network

import android.graphics.Bitmap
import android.net.Uri
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.PostQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery

interface NetworkDataSource {
    suspend fun getUserInfo(id: String): UserExistenceCheckResult?

    suspend fun checkNickname(nickname: String): SignUpCheckNicknameQuery?

    suspend fun checkEmail(email: String): SignUpCheckEmailQuery?

    suspend fun signUp(nickname: String,
                       email: String,
                       age: Int,
                       sex: String,
                       providerId: String,
                       providerName: String
    ): SignUpQuery

    suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): PostQuery

    suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
    ): LatestPostQuery

}
