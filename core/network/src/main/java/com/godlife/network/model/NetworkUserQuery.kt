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
    val accessToken: String,
    val refershToken: String,
)

data class BodyQuery(
    val accessToken: String?,
    val refreshToken: String?,
    val alreadySignUp: Boolean
)