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
import com.godlife.network.model.NotificationListQuery
import com.godlife.network.model.StimulusPostListQuery
import com.godlife.network.model.NotificationQuery
import com.godlife.network.model.NotificationRequest
import com.godlife.network.model.StimulusPostDetailQuery
import com.godlife.network.model.StimulusPostQuery
import com.godlife.network.model.UpdateIntroduceQuery
import com.godlife.network.model.UserProfileQuery
import com.godlife.network.model.RankingQuery
import com.godlife.network.model.RecommendPostQuery
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
    ): ApiResponse<SignUpQuery> {
        return networkApi.signUp(SignUpRequest( nickname, email, age, sex, providerId, providerName))
    }

    override suspend fun getUserInfo(): ApiResponse<UserInfoQuery> {
        return networkApi.getUserInfo()
    }

    override suspend fun getNotificationList(): ApiResponse<NotificationListQuery> {
        return networkApi.getNotificationList()
    }

    override suspend fun patchNotificationRead(alarmId: Int): ApiResponse<NotificationQuery> {
        return networkApi.patchNotificationRead(alarmId)
    }

    override suspend fun logout(): ApiResponse<LogoutQuery> {
        return networkApi.logout()
    }

    override suspend fun getUserProfile(
        memberId: String
    ): ApiResponse<UserProfileQuery> {
        return networkApi.getUserProfile(memberId)
    }

    override suspend fun reissue(): ApiResponse<ReissueQuery> {
        return networkApi.reissue()
    }

    override suspend fun registerFcmToken(
        fcmToken: String
    ): ApiResponse<SignUpCheckNicknameQuery> {
        return networkApi.registerFcmToken(fcmToken)
    }

    override suspend fun profileImageUpload(
        image: Uri
    ): ApiResponse<ImageUploadQuery> {

        val file = File(image.path!!)

        Log.e("NetworkDataSourceImpl", image.path!!.toString())

        val requestFile = file.asRequestBody("image/*".toMediaType())

        val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return networkApi.profileImageUpload(imageMultiPart)

    }

    override suspend fun backgroundImageUpload(
        image: Uri
    ): ApiResponse<ImageUploadQuery> {

        val file = File(image.path!!)

        Log.e("NetworkDataSourceImpl", image.path!!.toString())

        val requestFile = file.asRequestBody("image/*".toMediaType())

        val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return networkApi.backgroundImageUpload(imageMultiPart)
    }



    override suspend fun updateIntroduce(
        introduce: String
    ): ApiResponse<UpdateIntroduceQuery> {
        return networkApi.updateIntroduce(whoAmI = introduce)
    }

    override suspend fun createPost(
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

        return networkApi.createPost(title, content, tags, imageParts)
    }

    override suspend fun updatePost(
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

        return networkApi.updatePost(postId, title, content, categoryType, tags, imageParts)
    }

    override suspend fun deletePost(
        postId: String
    ): ApiResponse<DeletePostQuery> {
        return networkApi.deletePost(postId)
    }


    override suspend fun getLatestPost(
        page: Int,
        keyword: String,
        tag: String
    ): ApiResponse<LatestPostQuery> {

        return networkApi.getLatestPost(page, keyword, tag)
    }

    override suspend fun getWeeklyFamousPost(): ApiResponse<LatestPostQuery> {
        return networkApi.getWeeklyFamousPost()
    }

    override suspend fun getAllFamousPost(): ApiResponse<LatestPostQuery> {
        return networkApi.getAllFamousPost()
    }

    override suspend fun getSearchedPost(
        page: Int,
        keyword: String,
        tag: String,
        nickname: String
    ): ApiResponse<LatestPostQuery> {
        return networkApi.searchPost(page, keyword, tag, nickname)
    }

    override suspend fun getPostDetail(postId: String): ApiResponse<PostDetailQuery> {
        return networkApi.getPostDetail(postId)
    }

    override suspend fun getComments(postId: String): ApiResponse<GetCommentsQuery> {
        return networkApi.getComments(postId)
    }

    override suspend fun createComment(
        postId: String,
        comment: String
    ): ApiResponse<CommentQuery> {
        return networkApi.createComment(postId, comment)
    }

    override suspend fun deleteComment(commentId: String): ApiResponse<CommentQuery> {
        return networkApi.deleteComment(commentId)
    }

    override suspend fun agreeGodLife(postId: Int): ApiResponse<GodScoreQuery> {
        return networkApi.agreeGodLife(postId)
    }

    override suspend fun getWeeklyFamousMembers(): ApiResponse<RankingQuery> {
        return networkApi.getWeeklyFamousMembers()
    }

    override suspend fun getAllFamousMembers(): ApiResponse<RankingQuery> {
        return networkApi.getAllFamousMembers()
    }

    override suspend fun postNotificationTime(
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkApi.postNotificationTime(notificationTime)
    }

    override suspend fun patchNotificationTime(
        notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery> {
        return networkApi.patchNotificationTime(notificationTime)
    }

    override suspend fun deleteNotificationTime(): ApiResponse<NotificationQuery> {
        return networkApi.deleteNotificationTime()
    }

    override suspend fun createStimulusPostTemp(): ApiResponse<StimulusPostQuery> {
        return networkApi.createStimulusPostTemp()
    }

    override suspend fun uploadStimulusPostImage(
        tmpBoardId: Int,
        image: Uri
    ): ApiResponse<ImageUploadStimulusQuery> {

        val file = File(image.path!!)

        Log.e("NetworkDataSourceImpl", image.path!!.toString())

        val requestFile = file.asRequestBody("image/*".toMediaType())

        val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return networkApi.uploadStimulusPostImage(tmpBoardId, imageMultiPart)
    }

    override suspend fun createStimulusPost(
        stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery> {
        return networkApi.createStimulusPost(stimulusPostBody)
    }

    override suspend fun updateStimulusPost(stimulusPostBody: CreatePostRequest): ApiResponse<StimulusPostQuery> {
        return networkApi.updateStimulusPost(stimulusPostBody)
    }

    override suspend fun deleteStimulusPost(boardId: String): ApiResponse<DeletePostQuery> {
        return networkApi.deleteStimulusPost(boardId)
    }

    override suspend fun getStimulusLatestPost(
        page: Int
    ): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusLatestPost(page)
    }

    override suspend fun getStimulusFamousPost(): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusFamousPost()
    }

    override suspend fun getStimulusMostViewPost(): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusMostViewPost()
    }

    override suspend fun getStimulusFamousAuthorPost(): ApiResponse<RecommendPostQuery> {
        return networkApi.getStimulusFamousAuthorPost()
    }

    override suspend fun getStimulusRecommendPost(): ApiResponse<StimulusPostListQuery> {
        return networkApi.getStimulusRecommendPost()
    }

    override suspend fun getStimulusPostDetail(
        boardId: String
    ): ApiResponse<StimulusPostDetailQuery> {
        return networkApi.getStimulusPostDetail(boardId)
    }

    override suspend fun searchStimulusPost(
        title: String,
        nickname: String,
        introduction: String
    ): ApiResponse<StimulusPostListQuery> {
        return networkApi.searchStimulusPost(title, nickname, introduction)
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
        return networkApi.report(ReportRequest(reporterNickname, reporterId, receivedNickname, receivedId, reason, reportContent, reportId, reportType) )
    }


}