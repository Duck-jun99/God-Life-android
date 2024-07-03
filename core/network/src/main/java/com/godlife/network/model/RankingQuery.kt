package com.godlife.network.model

data class WeeklyRankingQuery(
    val status: String,
    val body: WeeklyRankingBody,
    val message: String
)

data class WeeklyRankingBody(
    val memberId: Int,
    val nickname: String,
    val godLifeScore: Int,
    val whoAmI: String,
    val profileURL: String
)