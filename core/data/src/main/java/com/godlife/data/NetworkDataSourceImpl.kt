package com.godlife.data

import android.net.Uri
import android.util.Log
import com.godlife.network.NetworkDataSource
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
import com.godlife.network.model.SignUpRequest
import com.godlife.network.model.UserInfoQuery
import com.godlife.network.api.RetrofitNetworkApi
import com.godlife.network.model.CreatePostRequest
import com.godlife.network.model.DeletePostQuery
import com.godlife.network.model.ImageUploadQuery
import com.godlife.network.model.ImageUploadStimulusQuery
import com.godlife.network.model.LogoutQuery
import com.godlife.network.model.StimulusPostListQuery
import com.godlife.network.model.NotificationQuery
import com.godlife.network.model.NotificationRequest
import com.godlife.network.model.StimulusPostDetailQuery
import com.godlife.network.model.StimulusPostQuery
import com.godlife.network.model.UpdateIntroduceQuery
import com.godlife.network.model.UserProfileQuery
import com.godlife.network.model.RankingQuery
import com.godlife.network.model.ReportRequest
import com.skydoves.sandwich.ApiResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val networkApi: RetrofitNetworkApi
) : NetworkDataSource {

    override suspend fun checkUserExistence(id : String): UserExistenceCheckResult? {
        return networkApi.checkUserExistence(id = id)

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

    override suspend fun getUserInfo(authorization: String): ApiResponse<UserInfoQuery> {
        return networkApi.getUserInfo(authorization)
    }

    override suspend fun logout(authorization: String): ApiResponse<LogoutQuery> {
        return networkApi.logout(authorization)
    }

    override suspend fun getUserProfile(
        authorization: String,
        memberId: String
    ): ApiResponse<UserProfileQuery> {
        return networkApi.getUserProfile(authorization, memberId)
    }

    override suspend fun reissue(authorization: String): ApiResponse<ReissueQuery> {
        return networkApi.reissue(authorization)
    }

    override suspend fun registerFcmToken(
        authorization: String,
        fcmToken: String
    ): ApiResponse<SignUpCheckNicknameQuery> {
        return networkApi.registerFcmToken(authorization, fcmToken)
    }

    override suspend fun profileImageUpload(
        authorization: String,
        image: Uri
    ): ApiResponse<ImageUploadQuery> {

        val file = File(image.path!!)

        Log.e("NetworkDataSourceImpl", image.path!!.toString())

        val requestFile = file.asRequestBody("image/*".toMediaType())

        val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return networkApi.profileImageUpload(authorization, imageMultiPart)

    }

    override suspend fun backgroundImageUpload(
        authorization: String,
        image: Uri
    ): ApiResponse<ImageUploadQuery> {

        val file = File(image.path!!)

        Log.e("NetworkDataSourceImpl", image.path!!.toString())

        val requestFile = file.asRequestBody("image/*".toMediaType())

        val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return networkApi.backgroundImageUpload(authorization, imageMultiPart)
    }



    override suspend fun updateIntroduce(
        authorization: String,
        introduce: String
    ): ApiResponse<UpdateIntroduceQuery> {
        return networkApi.updateIntroduce(authorization = authorization, whoAmI = introduce)
    }

    override suspend fun createPost(
        authorization: String,
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery> {

        val title: RequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val content: RequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())

        val tags = tags.map { it -> it.toRequestBody("text/plain".toMediaTypeOrNull()) }

        val imageParts = imagePath?.map { it ->

            val file = File(it.path)
            Log.e("NetworkDataSourceImpl", it.path!!.toString())

            Log.e("NetworkDataSourceImpl", file.readBytes().toString())
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            MultipartBody.Part.createFormData("images", file.name, requestFile)

        }

        return networkApi.createPost(authorization, title, content, tags, imageParts)
    }

    override suspend fun updatePost(
        authorization: String,
        postId: String,
        title: String,
        content: String,
        categoryType: String?,
        tags: List<String>,
        imagePath: List<Uri>?
    ): ApiResponse<PostQuery> {
        val title: RequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val content: RequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryType: RequestBody? = categoryType?.toRequestBody("text/plain".toMediaTypeOrNull())
        val tags = tags.map { it -> it.toRequestBody("text/plain".toMediaTypeOrNull()) }

        val imageParts = imagePath?.map { it ->

            val file = File(it.path)
            Log.e("NetworkDataSourceImpl", it.path!!.toString())

            Log.e("NetworkDataSourceImpl", file.readBytes().toString())
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            MultipartBody.Part.createFormData("images", file.name, requestFile)

        }

        return networkApi.updatePost(authorization, postId, title, content, categoryType, tags, imageParts)
    }

    override suspend fun deletePost(
        authorization: String,
        postId: String
    ): ApiResponse<DeletePostQuery> {
        return networkApi.deletePost(authorization, postId)
    }


    override suspend fun getLatestPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery> {

        return networkApi.getLatestPost(authorization, page, keyword, tag)
    }

    override suspend fun getWeeklyFamousPost(authorization: String): ApiResponse<LatestPostQuery> {
        return networkApi.getWeeklyFamousPost(authorization)
    }

    override suspend fun getAllFamousPost(authorization: String): ApiResponse<LatestPostQuery> {
        return networkApi.getAllFamousPost(authorization)
    }

    override suspend fun getSearchedPost(
        authorization: String,
        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): ApiResponse<LatestPostQuery> {
        return networkApi.searchPost(authorization, page, keyword, tag, nickname)
    }

    override suspend fun getPostDetail(authorization: String, postId: String): ApiResponse<PostDetailQuery> {
        return networkApi.getPostDetail(authorization, postId)
    }

    override suspend fun getComments(authorization: String, postId: String): ApiResponse<GetCommentsQuery> {
        return networkApi.getComments(authorization, postId)
    }

    override suspend fun createComment(
        authorization: String,
        postId: String,
        comment: String
    ): ApiResponse<CommentQuery> {
        return networkApi.createComment(authorization, postId, comment)
    }

    override suspend fun deleteComment(authorization: String, commentId: String): ApiResponse<CommentQuery> {
        return networkApi.deleteComment(authorization, commentId)
    }

    override suspend fun agreeGodLife(authorization: String, postId: Int): ApiResponse<GodScoreQuery> {
        return networkApi.agreeGodLife(authorization, postId)
    }

    override suspend fun getWeeklyFamousMembers(authorization: String): ApiResponse<RankingQuery> {
        return networkApi.getWeeklyFamousMembers(authorization)
    }

    override suspend fun getAllFamousMembers(authorization: String): ApiResponse<RankingQuery> {
        return networkApi.getAllFamousMembers(authorization)
    }

    override suspend fun postNotificationTime(
        authorization: String,
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkApi.postNotificationTime(authorization, notificationTime)
    }

    override suspend fun patchNotificationTime(
        authorization: String,
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkApi.patchNotificationTime(authorization, notificationTime)
    }

    override suspend fun deleteNotificationTime(authorization: String): ApiResponse<NotificationQuery> {
        return networkApi.deleteNotificationTime(authorization)
    }

    override suspend fun createStimulusPostTemp(authorization: String): ApiResponse<StimulusPostQuery> {
        return networkApi.createStimulusPostTemp(authorization)
    }

    override suspend fun uploadStimulusPostImage(
        authorization: String,
        tmpBoardId: Int,
        image: Uri
    ): ApiResponse<ImageUploadStimulusQuery> {

        val file = File(image.path!!)

        Log.e("NetworkDataSourceImpl", image.path!!.toString())

        val requestFile = file.asRequestBody("image/*".toMediaType())

        val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return networkApi.uploadStimulusPostImage(authorization, tmpBoardId, imageMultiPart)
    }

    override suspend fun createStimulusPost(
        authorization: String,
        stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery> {
        return networkApi.createStimulusPost(authorization, stimulusPostBody)
    }

    override suspend fun getStimulusLatestPost(
        authorization: String,
        page: Int
    ): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusLatestPost(authorization, page)
    }

    override suspend fun getStimulusFamousPost(authorization: String): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusFamousPost(authorization)
    }

    override suspend fun getStimulusMostViewPost(authorization: String): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusMostViewPost(authorization)
    }

    override suspend fun getStimulusFamousAuthorPost(authorization: String): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusFamousAuthorPost(authorization)
    }

    override suspend fun getStimulusRecommendPost(authorization: String): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusRecommendPost(authorization)
    }

    override suspend fun getStimulusPostDetail(
        authorization: String,
        boardId: String
    ): ApiResponse<StimulusPostDetailQuery> {
        return networkApi.getStimulusPostDetail(authorization, boardId)
    }

    override suspend fun searchStimulusPost(
        authorization: String,
        title: String,
        nickname: String,
        introduction: String
    ): ApiResponse<StimulusPostListQuery> {
        return networkApi.searchStimulusPost(authorization, title, nickname, introduction)
    }

    override suspend fun report(
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
    ): ApiResponse<CommentQuery> {
        return networkApi.report(authorization, ReportRequest(reporterNickname, reporterId, receivedNickname, receivedId, reason, reportContent, reportId, reportType) )
    }


}