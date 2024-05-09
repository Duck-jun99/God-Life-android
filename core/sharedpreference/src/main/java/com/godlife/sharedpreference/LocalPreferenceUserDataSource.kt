package com.godlife.sharedpreference

interface LocalPreferenceUserDataSource {

    fun getAccessToken(): String

    fun saveAccessToken(accessToken: String)

    fun getUserNickname(): String

    fun getUserId(): Int

    fun saveUserId(userId: Int)

    fun saveUserNickname(userNickname: String)

    fun removeAccessToken()

    fun removeUserNickname()


}
