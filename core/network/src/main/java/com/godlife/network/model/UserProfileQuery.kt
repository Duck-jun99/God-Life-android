package com.godlife.network.model

data class UserProfileQuery(
    val status: String,
    val body: UserProfileBody,
    val message: String,
)

data class UserProfileBody(
    val nickname: String,
    val whoAmI: String,
    val profileImageURL: String,
    val backgroundImageURL: String,
    val godLifeScore: Int,
    val memberBoardCount: Int,
    val owner: Boolean
)
