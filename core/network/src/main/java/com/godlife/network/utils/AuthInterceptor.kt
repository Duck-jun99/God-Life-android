package com.godlife.network.utils

import android.content.SharedPreferences
import android.util.Log
import com.godlife.network.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val localPreferences: SharedPreferences
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request


        if(originRequest.header("Authorization").isNullOrEmpty()){
            Log.e("AuthInterceptor", "originRequest.header(\"Authorization\").isNullOrEmpty()")
            return null
        }
        val refreshToken = runBlocking {
            localPreferences.getString("READ_ME_REFRESH_TOKEN", "")
        }
        Log.e("AuthInterceptor", "refreshToken: ${refreshToken!!}")
        //재발급 api 요청하기
        val refreshRequest = Request.Builder()
            .url(BuildConfig.SERVER_DOMAIN + "reissue")
            .post("".toRequestBody())
            .addHeader("Authorization", "Bearer ${refreshToken!!}")
            .build()

        val refreshResponse = OkHttpClient().newCall(refreshRequest).execute()

        refreshResponse.use { refreshResponseBody ->
            Log.e("AuthInterceptor", "refreshResponse.code : ${refreshResponseBody.code}")

            //재발급 성공
            if(refreshResponseBody.code == 200) {
                val gson = Gson()
                val responseBodyString = refreshResponseBody.body?.string()
                val refreshResponseJson = gson.fromJson(responseBodyString, Map::class.java)
                Log.e("AuthInterceptor", "refreshResponseJson: $refreshResponseJson")

                val body = refreshResponseJson["body"] as? Map<*, *>
                val newAccessToken = body?.get("accessToken")?.toString() ?: ""
                val newRefreshToken = body?.get("refreshToken")?.toString() ?: ""

                localPreferences.edit().putString("READ_ME_ACCESS_TOKEN", newAccessToken).apply()
                localPreferences.edit().putString("READ_ME_REFRESH_TOKEN", newRefreshToken).apply()
                val newRequest = originRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
                Log.e("AuthInterceptor", "newRequest : $newRequest")
                return newRequest
            } else {//재발급 실패 - refreshToken 만료
                Log.e("AuthInterceptor", "refreshToken 만료")
                localPreferences.edit().remove("READ_ME_ACCESS_TOKEN").apply()
                localPreferences.edit().remove("READ_ME_REFRESH_TOKEN").apply()
                localPreferences.edit().remove("USER_ID").apply()
            }
        }
        return null
    }
}