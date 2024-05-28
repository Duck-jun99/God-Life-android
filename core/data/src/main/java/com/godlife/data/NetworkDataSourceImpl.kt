package com.godlife.data

import android.net.Uri
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.godlife.network.NetworkDataSource
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.PostQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery
import com.godlife.network.model.SignUpRequest
import com.godlife.network.retrofit.RetrofitNetworkApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.Flow
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

    override suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): PostQuery {

        val imageParts = imagePath?.map { it ->

            val file = File(it.path)
            Log.e("NetworkDataSourceImpl", file.readBytes().toString())
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            MultipartBody.Part.createFormData("images", file.name, requestFile)

        }

        return networkApi.createPost(authorization, title, content, tags, imageParts)
    }



    override suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String
    ): LatestPostQuery {

        return networkApi.getLatestPost(authorization, page, keyword, tag)
    }




}