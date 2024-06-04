package com.godlife.network.retrofit

import androidx.tracing.trace
import com.godlife.network.BuildConfig
import com.godlife.network.NetworkDataSource
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.PostDetailQuery
import com.godlife.network.model.PostQuery
import com.godlife.network.model.UserExistenceCheckResult
import com.godlife.network.model.SignUpCheckEmailQuery
import com.godlife.network.model.SignUpCheckNicknameQuery
import com.godlife.network.model.SignUpQuery
import com.godlife.network.model.SignUpRequest
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface RetrofitNetworkApi {

    @GET("check/id")
    suspend fun getUserInfo(
        @Query("memberId") id: String?,
    ): UserExistenceCheckResult

    @GET("check/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname :String?
    ): SignUpCheckNicknameQuery

    @GET("check/email")
    suspend fun checkEmail(
        @Query("email") email :String?
    ): SignUpCheckEmailQuery


    @POST("/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): SignUpQuery

    @Multipart
    @POST("/board")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Part("title") title: String,
        @Part("content") content: String,
        @Part("tags") tags: List<String>,
        @Part images: List<MultipartBody.Part>?,
    ): PostQuery

    @GET("/boards")
    suspend fun getLatestPost(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
        @Query("Tag") tag: String,
    ): LatestPostQuery

    @GET("/board/{id}")
    suspend fun getPostDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
    ): PostDetailQuery



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

    override suspend fun getUserInfo(
        //remoteErrorEmitter: RemoteErrorEmitter,
        id: String
    ): UserExistenceCheckResult? =
        networkApi.getUserInfo(id = id)

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

