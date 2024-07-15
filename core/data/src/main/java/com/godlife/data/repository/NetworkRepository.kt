package com.godlife.data.repository

import android.net.Uri
import com.godlife.network.model.GetCommentsQuery
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.CommentQuery
import com.godlife.network.model.CreatePostRequest
import com.godlife.network.model.DeletePostQuery
import com.godlife.network.model.GodScoreQuery
import com.godlife.network.model.ImageUploadQuery
import com.godlife.network.model.ImageUploadStimulusQuery
import com.godlife.network.model.LatestStimulusPostQuery
import com.godlife.network.model.NotificationQuery
import com.godlife.network.model.NotificationRequest
import com.godlife.network.model.PostDetailQuery
import com.godlife.network.model.PostQuery
import com.godlife.network.model.ReissueQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery
import com.godlife.network.model.StimulusPostDetailQuery
import com.godlife.network.model.StimulusPostQuery
import com.godlife.network.model.UpdateIntroduceQuery
import com.godlife.network.model.UserInfoQuery
import com.godlife.network.model.UserProfileQuery
import com.godlife.network.model.RankingQuery
import com.skydoves.sandwich.ApiResponse
import java.time.LocalDateTime

interface NetworkRepository {
    suspend fun checkUserExistence(id : String) : UserExistenceCheckResult?

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

    suspend fun getUserInfo(authorization: String): ApiResponse<UserInfoQuery>

    suspend fun getUserProfile(
        authorization: String,
        memberId: String
    ): ApiResponse<UserProfileQuery>

    suspend fun reissue(authorization: String): ApiResponse<ReissueQuery>

    suspend fun profileImageUpload(
        authorization: String,
        image: Uri
    ):ApiResponse<ImageUploadQuery>

    suspend fun backgroundImageUpload(
        authorization: String,
        image: Uri
    ):ApiResponse<ImageUploadQuery>

    suspend fun updateIntroduce(
        authorization: String,
        introduce: String
    ): ApiResponse<UpdateIntroduceQuery>

    suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery>

    suspend fun updatePost(
        authorization: String,
        postId: String,
        title: String,
        content: String,
        categoryType: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery>

    suspend fun deletePost(
        authorization: String,
        postId: String
    ): ApiResponse<DeletePostQuery>

    suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery>

    /*
    suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
    ): ApiResponse<LatestPostQuery>

     */

    suspend fun getWeeklyFamousPost(
        authorization: String
    ): ApiResponse<LatestPostQuery>

    suspend fun getAllFamousPost(
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


    suspend fun getWeeklyFamousMembers(
        authorization: String
    ): ApiResponse<RankingQuery>

    suspend fun getAllFamousMembers(
        authorization: String
    ): ApiResponse<RankingQuery>

    suspend fun postNotificationTime(
        authorization: String,
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery>

    suspend fun createStimulusPostTemp(
        authorization: String
    ): ApiResponse<StimulusPostQuery>

    suspend fun uploadStimulusPostImage(
        authorization: String,
        tmpBoardId: Int,
        image: Uri
    ): ApiResponse<ImageUploadStimulusQuery>

    suspend fun createStimulusPost(
        authorization: String,
        stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery>

    suspend fun getStimulusPostDetail(
        authorization: String,
        boardId: String
    ): ApiResponse<StimulusPostDetailQuery>

    suspend fun searchStimulusPost(
        authorization: String,
        title: String,
        nickname: String,
        introduction: String
    ): ApiResponse<LatestStimulusPostQuery>

    suspend fun report(
        authorization: String,
        reporterNickname: String,
        reporterId: Long,
        receivedNickname: String,
        receivedId: Long,
        reason: String,
        reportContent: String,
        reportId: Long,
        //reportTime: LocalDateTime,
        reportType: String
    ): ApiResponse<CommentQuery>



}