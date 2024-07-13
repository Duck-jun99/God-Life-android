package com.godlife.network.model

data class RankingQuery(
    val status: String,
    val body: List<RankingBody>,
    val message: String
)

data class RankingBody(
    val memberId: Int,
    val nickname: String,
    val godLifeScore: Int,
    val whoAmI: String,
    val profileURL: String,
    val backgroundUrl: String
)