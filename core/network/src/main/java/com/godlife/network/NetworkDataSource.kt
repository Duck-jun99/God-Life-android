package com.godlife.network

import android.net.Uri
import com.godlife.network.model.GetCommentsQuery
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.CommentQuery
import com.godlife.network.model.CreatePostRequest
import com.godlife.network.model.DeletePostQuery
import com.godlife.network.model.GodScoreQuery
import com.godlife.network.model.ImageUploadQuery
import com.godlife.network.model.ImageUploadStimulusQuery
import com.godlife.network.model.LogoutQuery
import com.godlife.network.model.StimulusPostListQuery
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

    suspend fun getUserInfo(): ApiResponse<UserInfoQuery>

    suspend fun logout(): ApiResponse<LogoutQuery>

    suspend fun getUserProfile(
        memberId: String
    ): ApiResponse<UserProfileQuery>

    suspend fun reissue(): ApiResponse<ReissueQuery>

    suspend fun registerFcmToken(
        fcmToken: String
    ): ApiResponse<SignUpCheckNicknameQuery>

    suspend fun profileImageUpload(
        image: Uri
    ):ApiResponse<ImageUploadQuery>

    suspend fun backgroundImageUpload(
        image: Uri
    ):ApiResponse<ImageUploadQuery>

    suspend fun updateIntroduce(
        introduce: String
    ): ApiResponse<UpdateIntroduceQuery>


    suspend fun createPost(
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery>

    suspend fun updatePost(
        postId: String,
        title: String,
        content: String,
        categoryType: String?,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery>

    suspend fun deletePost(
        postId: String
    ): ApiResponse<DeletePostQuery>

    suspend fun getLatestPost(
        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery>

    suspend fun getWeeklyFamousPost(
    ): ApiResponse<LatestPostQuery>

    suspend fun getAllFamousPost(
    ): ApiResponse<LatestPostQuery>

    suspend fun getSearchedPost(
        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): ApiResponse<LatestPostQuery>

    suspend fun getPostDetail(
        postId: String
    ): ApiResponse<PostDetailQuery>

    suspend fun getComments(
        postId: String
    ): ApiResponse<GetCommentsQuery>

    suspend fun createComment(
        postId: String,
        comment: String
    ): ApiResponse<CommentQuery>

    suspend fun deleteComment(
        commentId: String
    ): ApiResponse<CommentQuery>

    suspend fun agreeGodLife(
        postId: Int
    ): ApiResponse<GodScoreQuery>


    suspend fun getWeeklyFamousMembers(
    ): ApiResponse<RankingQuery>

    suspend fun getAllFamousMembers(
    ): ApiResponse<RankingQuery>

    suspend fun postNotificationTime(
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery>

    suspend fun patchNotificationTime(
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery>

    suspend fun deleteNotificationTime(
    ): ApiResponse<NotificationQuery>

    suspend fun createStimulusPostTemp(
    ): ApiResponse<StimulusPostQuery>

    suspend fun uploadStimulusPostImage(
        tmpBoardId: Int,
        image: Uri
    ): ApiResponse<ImageUploadStimulusQuery>

    suspend fun createStimulusPost(
        stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery>

    suspend fun getStimulusLatestPost(
        page: Int
    ): ApiResponse<StimulusPostListQuery>

    suspend fun getStimulusFamousPost(
    ): ApiResponse<StimulusPostListQuery>

    suspend fun getStimulusMostViewPost(
    ): ApiResponse<StimulusPostListQuery>

    suspend fun getStimulusFamousAuthorPost(
    ): ApiResponse<StimulusPostListQuery>

    suspend fun getStimulusRecommendPost(
    ): ApiResponse<StimulusPostListQuery>

    suspend fun getStimulusPostDetail(
        boardId: String
    ): ApiResponse<StimulusPostDetailQuery>

    suspend fun searchStimulusPost(
        title: String,
        nickname: String,
        introduction: String
    ): ApiResponse<StimulusPostListQuery>

    suspend fun report(
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
