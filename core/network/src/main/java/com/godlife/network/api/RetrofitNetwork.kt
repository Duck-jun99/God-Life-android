package com.godlife.network.api

import com.godlife.network.model.GetCommentsQuery
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.CommentQuery
import com.godlife.network.model.CreatePostRequest
import com.godlife.network.model.DeletePostQuery
import com.godlife.network.model.GodScoreQuery
import com.godlife.network.model.ImageUploadQuery
import com.godlife.network.model.ImageUploadStimulusQuery
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
import com.godlife.network.model.SignUpRequest
import com.godlife.network.model.StimulusPostDetailQuery
import com.godlife.network.model.StimulusPostQuery
import com.godlife.network.model.UpdateIntroduceQuery
import com.godlife.network.model.UserInfoQuery
import com.godlife.network.model.UserProfileQuery
import com.godlife.network.model.RankingQuery
import com.godlife.network.model.ReportRequest
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitNetworkApi {

    // 아이디 존재여부 확인
    @GET("check/id")
    suspend fun checkUserExistence(
        @Query("memberId") id: String?,
    ): UserExistenceCheckResult

    // 닉네임 중복체크
    @GET("check/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname :String?
    ): SignUpCheckNicknameQuery

    // 이메일 중복체크
    @GET("check/email")
    suspend fun checkEmail(
        @Query("email") email :String?
    ): SignUpCheckEmailQuery


    // 회원가입
    @POST("/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): SignUpQuery

    // 로그인 시 유저 정보 받아옴
    @GET("/member")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String,
    ): ApiResponse<UserInfoQuery>

    // 회원 정보 조회 (프로필)
    @GET("/member/{memberId}")
    suspend fun getUserProfile(
        @Header("Authorization") authorization: String,
        @Path("memberId") memberId: String
    ): ApiResponse<UserProfileQuery>

    // 엑세스 토큰 갱신
    @POST("/reissue")
    suspend fun reissue(
        @Header("Authorization") authorization: String,
    ): ApiResponse<ReissueQuery>

    // fcm 토큰 등록 or 갱신
    @POST("/member/fcm")
    suspend fun registerFcmToken(
        @Header("Authorization") authorization: String,
        @Body fcmToken: String
    ): ApiResponse<SignUpCheckNicknameQuery>

    //프로필 이미지 변경
    @Multipart
    @POST("/member/profile")
    suspend fun profileImageUpload(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): ApiResponse<ImageUploadQuery>

    //프로필 배경사진 변경
    @Multipart
    @POST("/member/background")
    suspend fun backgroundImageUpload(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): ApiResponse<ImageUploadQuery>

    //소개글 수정
    @PATCH("/member")
    suspend fun updateIntroduce(
        @Header("Authorization") authorization: String,
        @Body whoAmI: String
    ): ApiResponse<UpdateIntroduceQuery>


    // 굿생 인증 게시물 생성
    @JvmSuppressWildcards
    @Multipart
    @POST("/board")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("tags") tags: List<RequestBody>,
        @Part images: List<MultipartBody.Part>?,
    ): ApiResponse<PostQuery>

    // 굿생 인증 게시물 수정
    @JvmSuppressWildcards
    @Multipart
    @PUT("/board/{id}")
    suspend fun updatePost(
        @Header("Authorization") authorization: String,
        @Path("id") boardId: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("categoryType") categoryType: RequestBody?,
        @Part("tags") tags: List<RequestBody>,
        @Part images: List<MultipartBody.Part>?,
    ): ApiResponse<PostQuery>

    // 굿생 인증 게시물 삭제
    @DELETE("/board/{id}")
    suspend fun deletePost(
        @Header("Authorization") authorization: String,
        @Path("id") boardId: String,
    ): ApiResponse<DeletePostQuery>


    // 최신 게시물 조회
    @GET("/boards")
    suspend fun getLatestPost(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
        @Query("Tag") tag: String,
        //@Query("nickname") nickname: String = ""
    ): ApiResponse<LatestPostQuery>

    //일주일 인기 게시물 조회
    @GET("/popular/boards/weekly")
    suspend fun getWeeklyFamousPost(
        @Header("Authorization") authorization: String
    ): ApiResponse<LatestPostQuery>

    //전체 인기 게시물 조회
    @GET("/popular/boards/all-time")
    suspend fun getAllFamousPost(
        @Header("Authorization") authorization: String
    ): ApiResponse<LatestPostQuery>


    //게시물 검색
    @GET("/boards")
    suspend fun searchPost(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
        @Query("Tag") tag: String,
        @Query("Nickname") nickname: String
    ): ApiResponse<LatestPostQuery>


    //게시물 상세 조회
    @GET("/board/{id}")
    suspend fun getPostDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
    ): ApiResponse<PostDetailQuery>

    //댓글 조회
    @GET("/comment/{boardId}")
    suspend fun getComments(
        @Header("Authorization") authorization: String,
        @Path("boardId") boardId: String,
    ): ApiResponse<GetCommentsQuery>

    //댓글 생성
    @POST("/comment/{boardId}")
    suspend fun createComment(
        @Header("Authorization") authorization: String,
        @Path("boardId") boardId: String,
        @Body comment: String,
    ): ApiResponse<CommentQuery>

    //댓글 삭제
    @DELETE("/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") authorization: String,
        @Path("commentId") commentId: String,
    ): ApiResponse<CommentQuery>

    //굿생 인정
    @POST("/like/board/{boardId}")
    suspend fun agreeGodLife(
        @Header("Authorization") authorization: String,
        @Path("boardId") boardId: Int,
    ): ApiResponse<GodScoreQuery>

    //주간 명예의 전당
    @GET("/popular/members/weekly")
    suspend fun getWeeklyFamousMembers(
        @Header("Authorization") authorization: String
    ): ApiResponse<RankingQuery>

    //전체 명예의 전당
    @GET("/popular/members/all-time")
    suspend fun getAllFamousMembers(
        @Header("Authorization") authorization: String
    ): ApiResponse<RankingQuery>

    //알람 시간 전송
    @POST("/fcm/alarm")
    suspend fun postNotificationTime(
        @Header("Authorization") authorization: String,
        @Body notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery>

    //알람 시간 수정
    @PUT("/fcm/alarm")
    suspend fun patchNotificationTime(
        @Header("Authorization") authorization: String,
        @Body notificationTime: NotificationRequest
    ): ApiResponse<NotificationQuery>

    //알람 시간 삭제
    @DELETE("/fcm/alarm")
    suspend fun deleteNotificationTime(
        @Header("Authorization") authorization: String
    ): ApiResponse<NotificationQuery>


    //굿생 자극 게시물 임시 생성
    @POST("/board/tmp")
    suspend fun createStimulusPostTemp(
        @Header("Authorization") authorization: String
    ): ApiResponse<StimulusPostQuery>

    //굿생 자극 게시물 이미지 업로드
    @Multipart
    @POST("/board/image-upload")
    suspend fun uploadStimulusPostImage(
        @Header("Authorization") authorization: String,
        @Query("tmpBoardId") boardId: Int,
        @Part image: MultipartBody.Part
    ): ApiResponse<ImageUploadStimulusQuery>

    //굿생 자극 게시물 최종 생성
    @POST("/board/stimulation")
    suspend fun createStimulusPost(
        @Header("Authorization") authorization: String,
        @Body stimulusPostBody: CreatePostRequest
    ): ApiResponse<StimulusPostQuery>


    // 굿생 자극 최신 게시물 리스트 조회
    @GET("/boards/stimulation")
    suspend fun getStimulusLatestPost(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int
    ): ApiResponse<StimulusPostListQuery>

    // 굿생 자극 인기 게시물 리스트 조회
    @GET("/popular/stimulus/boards/all-time")
    suspend fun getStimulusFamousPost(
        @Header("Authorization") authorization: String
    ): ApiResponse<StimulusPostListQuery>

    // 굿생 자극 조회 수 많은 게시물 리스트 조회
    @GET("/popular/stimulus/boards/view")
    suspend fun getStimulusMostViewPost(
        @Header("Authorization") authorization: String
    ): ApiResponse<StimulusPostListQuery>

    // 굿생 자극 추천 작가의 게시물 리스트 조회 (관리자 선정)
    @GET("/recommend/author")
    suspend fun getStimulusFamousAuthorPost(
        @Header("Authorization") authorization: String
    ): ApiResponse<StimulusPostListQuery>

    // 굿생 자극 추천 게시물 리스트 조회 (관리자 선정)
    @GET("/recommend/board")
    suspend fun getStimulusRecommendPost(
        @Header("Authorization") authorization: String
    ): ApiResponse<StimulusPostListQuery>

    // 굿생 자극 게시물 상세 조회
    @GET("/board/stimulation/{boardId}")
    suspend fun getStimulusPostDetail(
        @Header("Authorization") authorization: String,
        @Path("boardId") boardId: String,
    ): ApiResponse<StimulusPostDetailQuery>

    //굿생 자극 게시물 검색
    @GET("/boards/stimulation/filter")
    suspend fun searchStimulusPost(
        @Header("Authorization") authorization: String,
        @Query("title") title: String,
        @Query("nickname") nickname: String,
        @Query("introduction") introduction: String
    ): ApiResponse<StimulusPostListQuery>

    //신고하기
    @POST("/report")
    suspend fun report(
        @Header("Authorization") authorization: String,
        @Body requestBody: ReportRequest
    ): ApiResponse<CommentQuery>

}



/*
@Singleton
internal class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : NetworkDataSource {

    private val networkApi = trace("RetrofitNetwork") {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_DOMAIN)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitNetworkApi::class.java)
    }

    override suspend fun checkUserExistence(
        //remoteErrorEmitter: RemoteErrorEmitter,
        id: String
    ): UserExistenceCheckResult? =
        networkApi.checkUserExistence(id = id)

    override suspend fun checkNickname(
        nickname: String
    ): SignUpCheckNicknameQuery? =
        networkApi.checkNickname(nickname = nickname)

    override suspend fun checkEmail(email: String): SignUpCheckEmailQuery?
    = networkApi.checkEmail(email = email)

    override suspend fun signUp(
        nickname: String,
        email: String,
        age: Int,
        sex: String,
        providerId: String,
        providerName: String
    ): SignUpQuery
    = networkApi.signUp(SignUpRequest(nickname, email, age, sex, providerId, providerName))

    override suspend fun createPost(
        title: String,
        content: String,
        tags: List<String>,
        imagePath: List<String>
    ) = networkApi.createPost(title, content, tags, imagePath)

}

 */

