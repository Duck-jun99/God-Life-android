package com.godlife.network

import android.net.Uri
import com.godlife.network.model.GetCommentsQuery
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.CommentQuery
import com.godlife.network.model.GodScoreQuery
import com.godlife.network.model.PostDetailQuery
import com.godlife.network.model.PostQuery
import com.godlife.network.model.ReissueQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery
import com.godlife.network.model.UserInfoQuery
import com.skydoves.sandwich.ApiResponse

interface NetworkDataSource {
    suspend fun checkUserExistence(id: String): UserExistenceCheckResult?

    suspend fun checkNickname(nickname: String): SignUpCheckNicknameQuery?

    suspend fun checkEmail(email: String): SignUpCheckEmailQuery?

    suspend fun signUp(nickname: String,
                       email: String,
                       age: Int,
                       sex: String,
                       providerId: String,
                       providerName: String
    ): SignUpQuery

    suspend fun getUserInfo(authorization: String): ApiResponse<UserInfoQuery>

    suspend fun reissue(authorization: String): ApiResponse<ReissueQuery>

    suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery>

    suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery>

    suspend fun getWeeklyFamousPost(
        authorization: String
    ): ApiResponse<LatestPostQuery>

    suspend fun getSearchedPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): ApiResponse<LatestPostQuery>

    suspend fun getPostDetail(
        authorization: String,
        postId: String
    ): ApiResponse<PostDetailQuery>

    suspend fun getComments(
        authorization: String,
        postId: String
    ): ApiResponse<GetCommentsQuery>

    suspend fun createComment(
        authorization: String,
        postId: String,
        comment: String
    ): ApiResponse<CommentQuery>

    suspend fun deleteComment(
        authorization: String,
        commentId: String
    ): ApiResponse<CommentQuery>

    suspend fun agreeGodLife(
        authorization: String,
        postId: Int
    ): ApiResponse<GodScoreQuery>

}
