package com.godlife.network.model

import com.google.gson.annotations.SerializedName

data class UserExistenceCheckResult(
    val status:String,

    @SerializedName("body")
    val body: BodyQuery,

    val message: String
)

data class SignUpCheckNicknameQuery(
    val status:String,

    @SerializedName("body")
    val check: Boolean,

    val message: String
)

data class SignUpCheckEmailQuery(
    val status:String,

    @SerializedName("body")
    val check: Boolean,

    val message: String
)

data class SignUpQuery(
    val status: String,
    val body: BodySignUp,
    val message: String
)

data class BodySignUp(
    val accessToken: String,
    val refreshToken: String,
)

data class BodyQuery(
    val accessToken: String?,
    val refreshToken: String?,
    val alreadySignUp: Boolean
)