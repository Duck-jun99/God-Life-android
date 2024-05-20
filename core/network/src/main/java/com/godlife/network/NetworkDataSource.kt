package com.godlife.network

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

}
