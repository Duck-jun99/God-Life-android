package com.godlife.data

import android.net.Uri
import com.godlife.data.repository.NetworkRepository
import com.godlife.network.NetworkDataSource
import com.godlife.network.model.GetCommentsQuery
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.CommentQuery
import com.godlife.network.model.CreatePostRequest
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
import com.godlife.network.model.WeeklyRankingQuery
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

    override suspend fun getUserInfo(authorization: String): ApiResponse<UserInfoQuery> {
        return networkDataSource.getUserInfo(authorization)
    }

    override suspend fun getUserProfile(
        authorization: String,
        memberId: String
    ): ApiResponse<UserProfileQuery> {
        return networkDataSource.getUserProfile(authorization, memberId)
    }

    override suspend fun reissue(authorization: String): ApiResponse<ReissueQuery> {
        return networkDataSource.reissue(authorization)
    }

    override suspend fun imageUpload(
        authorization: String,
        imageType: String,
        image: Uri
    ): ApiResponse<ImageUploadQuery> {
        return networkDataSource.imageUpload(authorization, imageType, image)
    }

    override suspend fun updateIntroduce(
        authorization: String,
        introduce: String
    ): ApiResponse<UpdateIntroduceQuery> {
        return networkDataSource.updateIntroduce(authorization, introduce)
    }

    override suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery> {
        return networkDataSource.createPost(authorization, title, content, tags, imagePath)
    }

    /*
    override suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery> {
        return networkDataSource.getLatestPost(authorization, page, keyword, tag)
    }

     */

    override suspend fun getWeeklyFamousPost(authorization: String): ApiResponse<LatestPostQuery> {
        return networkDataSource.getWeeklyFamousPost(authorization)
    }

    override suspend fun getSearchedPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): ApiResponse<LatestPostQuery> {
        return networkDataSource.getSearchedPost(authorization, page, keyword, tag, nickname)
    }

    override suspend fun getPostDetail(authorization: String, postId: String): ApiResponse<PostDetailQuery> {
        return networkDataSource.getPostDetail(authorization, postId)
    }

    override suspend fun getComments(authorization: String, postId: String): ApiResponse<GetCommentsQuery> {
        return networkDataSource.getComments(authorization, postId)
    }

    override suspend fun createComment(
        authorization: String,
        postId: String,
        comment: String
    ): ApiResponse<CommentQuery> {
        return networkDataSource.createComment(authorization, postId, comment)
    }

    override suspend fun deleteComment(authorization: String, commentId: String): ApiResponse<CommentQuery> {
        return networkDataSource.deleteComment(authorization, commentId)
    }

    override suspend fun agreeGodLife(authorization: String, postId: Int): ApiResponse<GodScoreQuery> {
        return networkDataSource.agreeGodLife(authorization, postId)
    }

    override suspend fun getWeeklyFamousMembers(authorization: String): ApiResponse<WeeklyRankingQuery> {
        return networkDataSource.getWeeklyFamousMembers(authorization)
    }

    override suspend fun postNotificationTime(
        authorization: String,
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkDataSource.postNotificationTime(authorization, notificationTime)
    }

    override suspend fun createStimulusPostTemp(authorization: String): ApiResponse<StimulusPostQuery> {
        return networkDataSource.createStimulusPostTemp(authorization)
    }

    override suspend fun uploadStimulusPostImage(
        authorization: String,
        tmpBoardId: Int,
        image: Uri
    ): ApiResponse<ImageUploadStimulusQuery> {
        return networkDataSource.uploadStimulusPostImage(authorization, tmpBoardId, image)
    }

    override suspend fun createStimulusPost(
        authorization: String,
        stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery> {
        return networkDataSource.createStimulusPost(authorization, stimulusPostBody)
    }

    override suspend fun getStimulusPostDetail(
        authorization: String,
        boardId: String
    ): ApiResponse<StimulusPostDetailQuery> {
        return networkDataSource.getStimulusPostDetail(authorization, boardId)
    }

    override suspend fun searchStimulusPost(
        authorization: String,
        title: Int,
        nickname: String,
        introduction: String
    ): ApiResponse<LatestStimulusPostQuery> {
        return networkDataSource.searchStimulusPost(authorization, title, nickname, introduction)
    }

}