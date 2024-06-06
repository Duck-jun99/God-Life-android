package com.godlife.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.godlife.network.model.GetCommentsQuery
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.PostCommentQuery
import com.godlife.network.model.PostDetailQuery
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

    suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
    ): LatestPostQuery

    suspend fun getSearchedPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): LatestPostQuery

    suspend fun getPostDetail(
        authorization: String,
        postId: String
    ): PostDetailQuery

    suspend fun getComments(
        authorization: String,
        postId: String
    ): GetCommentsQuery

    suspend fun createComment(
        authorization: String,
        postId: String,
        comment: String
    ): PostCommentQuery


}