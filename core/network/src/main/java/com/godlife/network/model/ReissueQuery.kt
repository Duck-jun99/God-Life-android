package com.godlife.network.model

data class ReissueQuery(
    val status: String,
    val body: ReissueBody,
    val message: String,
)

data class ReissueBody(
    val accessToken: String,
    val refreshToken: String,
)