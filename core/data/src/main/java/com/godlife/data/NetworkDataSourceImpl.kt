package com.godlife.data

import com.godlife.network.NetworkDataSource
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery
import com.godlife.network.model.SignUpRequest
import com.godlife.network.retrofit.RetrofitNetworkApi
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val networkApi: RetrofitNetworkApi
) : NetworkDataSource {

    override suspend fun getUserInfo( id : String): UserExistenceCheckResult? {
        return networkApi.getUserInfo(id = id)

    }

    override suspend fun checkNickname(nickname: String): SignUpCheckNicknameQuery? {
        return networkApi.checkNickname(nickname = nickname)
    }

    override suspend fun checkEmail(email: String): SignUpCheckEmailQuery? {
        return networkApi.checkEmail(email = email)
    }

    override suspend fun signUp(
        nickname: String,
        email: String,
        age: Int,
        sex: String,
        providerId: String,
        providerName: String
    ): SignUpQuery {
        return networkApi.signUp(SignUpRequest( nickname, email, age, sex, providerId, providerName))
    }

}