package com.godlife.data.repository

import com.godlife.network.model.NetworkUserQuery
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery

interface NetworkRepository {
    suspend fun getUserInfo(id : String) : NetworkUserQuery?

    //SignUp Check
    suspend fun checkNickname(nickname : String) : SignUpCheckNicknameQuery?
    suspend fun checkEmail(nickname : String) : SignUpCheckEmailQuery?


}