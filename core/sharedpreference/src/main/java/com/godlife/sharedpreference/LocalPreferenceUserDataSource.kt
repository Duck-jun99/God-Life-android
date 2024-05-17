package com.godlife.sharedpreference

interface LocalPreferenceUserDataSource {

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
