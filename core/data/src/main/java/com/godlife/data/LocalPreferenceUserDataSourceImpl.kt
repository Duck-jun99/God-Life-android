package com.godlife.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.godlife.sharedpreference.LocalPreferenceUserDataSource
import javax.inject.Inject

class LocalPreferenceUserDataSourceImpl @Inject constructor(
    private val localPreferences: SharedPreferences
) : LocalPreferenceUserDataSource {
    override fun getAccessToken(): String =
        localPreferences.getString(READ_ME_ACCESS_TOKEN, "") ?: ""

    override fun saveAccessToken(accessToken: String) {
        localPreferences.edit { putString(READ_ME_ACCESS_TOKEN, accessToken) }
    }

    override fun removeAccessToken() {
        localPreferences.edit { remove(READ_ME_ACCESS_TOKEN) }
    }

    override fun getRefreshToken(): String =
        localPreferences.getString(READ_ME_REFRESH_TOKEN, "") ?: ""


    override fun saveRefreshToken(refreshToken: String) {
        localPreferences.edit{ putString(READ_ME_REFRESH_TOKEN, refreshToken) }
    }

    override fun removeRefreshToken() {
        localPreferences.edit{ remove(READ_ME_REFRESH_TOKEN) }
    }

    override fun getUserId(): String =
        localPreferences.getString(USER_ID, "") ?: ""

    override fun saveUserId(userId: String) {
        localPreferences.edit { putString(USER_ID, userId) }
    }

    override fun removeUserId() {
        localPreferences.edit{ remove(USER_ID) }
    }



    companion object {
        const val READ_ME_ACCESS_TOKEN = "READ_ME_ACCESS_TOKEN"
        const val READ_ME_REFRESH_TOKEN = "READ_ME_REFRESH_TOKEN"
        const val USER_ID = "USER_ID"
    }
}