package com.godlife.data.repository

import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery

interface NetworkRepository {
    suspend fun getUserInfo(id : String) : UserExistenceCheckResult?

    //SignUp Check
    suspend fun checkNickname(nickname : String) : SignUpCheckNicknameQuery?
    suspend fun checkEmail(nickname : String) : SignUpCheckEmailQuery?


}