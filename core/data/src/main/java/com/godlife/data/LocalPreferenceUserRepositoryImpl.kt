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

    override fun removeAccessToken() {
        return localPreferenceUserDataSource.removeAccessToken()
    }

    override fun getRefreshToken(): String {
        return localPreferenceUserDataSource.getRefreshToken()
    }

    override fun saveRefreshToken(refreshToken: String) {
        return localPreferenceUserDataSource.saveRefreshToken(refreshToken)
    }

    override fun removeRefreshToken() {
        return localPreferenceUserDataSource.removeRefreshToken()
    }

    override fun getUserId(): String {
        return localPreferenceUserDataSource.getUserId()
    }

    override fun saveUserId(userId: String) {
        return localPreferenceUserDataSource.saveUserId(userId)
    }

    override fun removeUserId() {
        return localPreferenceUserDataSource.removeUserId()
    }


}