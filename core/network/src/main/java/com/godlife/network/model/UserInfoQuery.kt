package com.godlife.network.model

data class UserInfoQuery(
    val status: String,
    val body: UserInfoBody,
    val message: String
)

data class UserInfoBody(
    val nickname: String,
    val age: Int,
    val sex: String,
    val godLifeScore: Int,
    val profileImage: String,
    val backgroundImage: String,
    val whoAmI: String,
    val memberId: String,
)
