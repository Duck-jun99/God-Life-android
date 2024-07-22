package com.godlife.data

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import com.godlife.network.NetworkDataSource
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
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : NetworkRepository {
    override suspend fun checkUserExistence(
        id: String
    ): UserExistenceCheckResult? {
        //return Mapper.mapperGithub(githubDataSource.getGithub(remoteErrorEmitter, owner))
        return networkDataSource.checkUserExistence(id)
    }

    override suspend fun checkNickname(nickname: String): SignUpCheckNicknameQuery? {
        return networkDataSource.checkNickname(nickname)
    }

    override suspend fun checkEmail(email: String): SignUpCheckEmailQuery? {
        return networkDataSource.checkEmail(email)
    }

    override suspend fun signUp(
        nickname: String,
        email: String,
        age: Int,
        sex: String,
        providerId: String,
        providerName: String
    ): SignUpQuery {
        return networkDataSource.signUp(nickname, email, age, sex, providerId, providerName)
    }

    override suspend fun getUserInfo(): ApiResponse<UserInfoQuery> {
        return networkDataSource.getUserInfo()
    }

    override suspend fun logout(): ApiResponse<LogoutQuery> {
        return networkDataSource.logout()
    }

    override suspend fun getUserProfile(

        memberId: String
    ): ApiResponse<UserProfileQuery> {
        return networkDataSource.getUserProfile(memberId)
    }

    override suspend fun reissue(): ApiResponse<ReissueQuery> {
        return networkDataSource.reissue()
    }

    override suspend fun registerFcmToken(
        fcmToken: String
    ): ApiResponse<SignUpCheckNicknameQuery> {
        return networkDataSource.registerFcmToken(fcmToken)
    }

    override suspend fun profileImageUpload(

        image: Uri
    ): ApiResponse<ImageUploadQuery> {
        return networkDataSource.profileImageUpload(image)
    }

    override suspend fun backgroundImageUpload(

        image: Uri
    ): ApiResponse<ImageUploadQuery> {
        return networkDataSource.backgroundImageUpload(image)
    }


    override suspend fun updateIntroduce(

        introduce: String
    ): ApiResponse<UpdateIntroduceQuery> {
        return networkDataSource.updateIntroduce(introduce)
    }

    override suspend fun createPost(

        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery> {
        return networkDataSource.createPost(title, content, tags, imagePath)
    }

    override suspend fun updatePost(

        postId: String,
        title: String,
        content: String,
        categoryType: String?,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery> {
        return networkDataSource.updatePost(postId, title, content, categoryType, tags, imagePath)
    }

    override suspend fun deletePost(

        postId: String
    ): ApiResponse<DeletePostQuery> {
        return networkDataSource.deletePost(postId)
    }


    override suspend fun getLatestPost(

        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery> {
        TODO("Not yet implemented")
    }

    /*
    override suspend fun getLatestPost(

        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery> {
        return networkDataSource.getLatestPost(, page, keyword, tag)
    }

     */

    override suspend fun getWeeklyFamousPost(): ApiResponse<LatestPostQuery> {
        return networkDataSource.getWeeklyFamousPost()
    }

    override suspend fun getAllFamousPost(): ApiResponse<LatestPostQuery> {
        return networkDataSource.getAllFamousPost()
    }

    override suspend fun getSearchedPost(

        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): ApiResponse<LatestPostQuery> {
        return networkDataSource.getSearchedPost(page, keyword, tag, nickname)
    }

    override suspend fun getPostDetail( postId: String): ApiResponse<PostDetailQuery> {
        return networkDataSource.getPostDetail(postId)
    }

    override suspend fun getComments( postId: String): ApiResponse<GetCommentsQuery> {
        return networkDataSource.getComments(postId)
    }

    override suspend fun createComment(

        postId: String,
        comment: String
    ): ApiResponse<CommentQuery> {
        return networkDataSource.createComment(postId, comment)
    }

    override suspend fun deleteComment( commentId: String): ApiResponse<CommentQuery> {
        return networkDataSource.deleteComment(commentId)
    }

    override suspend fun agreeGodLife( postId: Int): ApiResponse<GodScoreQuery> {
        return networkDataSource.agreeGodLife(postId)
    }

    override suspend fun getWeeklyFamousMembers(): ApiResponse<RankingQuery> {
        return networkDataSource.getWeeklyFamousMembers()
    }

    override suspend fun getAllFamousMembers(): ApiResponse<RankingQuery> {
        return networkDataSource.getAllFamousMembers()
    }

    override suspend fun postNotificationTime(

        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkDataSource.postNotificationTime(notificationTime)
    }

    override suspend fun patchNotificationTime(

        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkDataSource.patchNotificationTime(notificationTime)
    }

    override suspend fun deleteNotificationTime(): ApiResponse<NotificationQuery> {
        return networkDataSource.deleteNotificationTime()
    }

    override suspend fun createStimulusPostTemp(): ApiResponse<StimulusPostQuery> {
        return networkDataSource.createStimulusPostTemp()
    }

    override suspend fun uploadStimulusPostImage(

        tmpBoardId: Int,
        image: Uri
    ): ApiResponse<ImageUploadStimulusQuery> {
        return networkDataSource.uploadStimulusPostImage(tmpBoardId, image)
    }

    override suspend fun createStimulusPost(

        stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery> {
        return networkDataSource.createStimulusPost(stimulusPostBody)
    }

    override suspend fun getStimulusFamousPost(): ApiResponse<StimulusPostListQuery> {
        return networkDataSource.getStimulusFamousPost()
    }

    override suspend fun getStimulusMostViewPost(): ApiResponse<StimulusPostListQuery> {
        return networkDataSource.getStimulusMostViewPost()
    }

    override suspend fun getStimulusFamousAuthorPost(): ApiResponse<StimulusPostListQuery> {
        return networkDataSource.getStimulusFamousAuthorPost()
    }

    override suspend fun getStimulusRecommendPost(): ApiResponse<StimulusPostListQuery> {
        return networkDataSource.getStimulusRecommendPost()
    }

    override suspend fun getStimulusPostDetail(

        boardId: String
    ): ApiResponse<StimulusPostDetailQuery> {
        return networkDataSource.getStimulusPostDetail(boardId)
    }

    override suspend fun searchStimulusPost(

        title: String,
        nickname: String,
        introduction: String
    ): ApiResponse<StimulusPostListQuery> {
        return networkDataSource.searchStimulusPost(title, nickname, introduction)
    }

    override suspend fun report(

        reporterNickname: String,
        reporterId: Long,
        receivedNickname: String,
        receivedId: Long,
        reason: String,
        reportContent: String,
        reportId: Long,
        //reportTime: LocalDateTime,
        reportType: String
    ): ApiResponse<CommentQuery> {
        return networkDataSource.report(reporterNickname, reporterId, receivedNickname, receivedId, reason, reportContent, reportId, reportType)
    }

}