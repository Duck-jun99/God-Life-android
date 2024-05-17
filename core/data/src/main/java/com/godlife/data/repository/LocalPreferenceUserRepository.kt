package com.godlife.data.repository

interface LocalPreferenceUserRepository {

    fun getAccessToken(): String

    fun saveAccessToken(accessToken: String)

    fun removeAccessToken()

    fun getRefreshToken(): String

    fun saveRefreshToken(refreshToken: String)

    fun removeRefreshToken()

    fun getUserId(): String

    fun saveUserId(userId: String)

    fun removeUserId()
}