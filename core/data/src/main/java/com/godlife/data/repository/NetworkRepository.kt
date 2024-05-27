package com.godlife.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.godlife.network.model.PostQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery

interface NetworkRepository {
    suspend fun getUserInfo(id : String) : UserExistenceCheckResult?

    //SignUp Check
    suspend fun checkNickname(nickname : String) : SignUpCheckNicknameQuery?
    suspend fun checkEmail(email : String) : SignUpCheckEmailQuery?

    suspend fun signUp(nickname: String,
                       email: String,
                       age: Int,
                       sex: String,
                       providerId: String,
                       providerName: String
                       ): SignUpQuery

    suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): PostQuery


}