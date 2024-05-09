package com.godlife.data

import com.godlife.data.repository.LocalPreferenceUserRepository
import com.godlife.sharedpreference.LocalPreferenceUserDataSource
import javax.inject.Inject

class LocalPreferenceUserRepositoryImpl @Inject constructor(
    private val localPreferenceUserDataSource: LocalPreferenceUserDataSource
) : LocalPreferenceUserRepository {
    override fun getAccessToken(): String {
        return localPreferenceUserDataSource.getAccessToken()
    }

    override fun saveAccessToken(accessToken: String) {
        return localPreferenceUserDataSource.saveAccessToken(accessToken)
    }

    override fun getUserNickname(): String {
        return localPreferenceUserDataSource.getUserNickname()
    }

    override fun getUserId(): Int {
        return localPreferenceUserDataSource.getUserId()
    }

    override fun saveUserId(userId: Int) {
        return localPreferenceUserDataSource.saveUserId(userId)
    }

    override fun saveUserNickname(userNickname: String) {
        return localPreferenceUserDataSource.saveUserNickname(userNickname)
    }

    override fun removeAccessToken() {
        return localPreferenceUserDataSource.removeAccessToken()
    }

    override fun removeUserNickname() {
        return localPreferenceUserDataSource.removeUserNickname()
    }

}