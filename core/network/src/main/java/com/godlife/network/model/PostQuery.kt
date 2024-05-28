package com.godlife.network.model

data class PostQuery(
    val status: String,
    val body: Int,
    val message: String
)

//최신 게시물 조회
data class LatestPostQuery(
    val status: String,
    val body: List<PostDetailBody>,
    val message: String
)


//게시물 상세 바디
data class PostDetailBody(
    val board_id: Int,
    val imagesURL: List<String>?,
    val writtenAt: String,
    val views: Int,
    val godScore: Int,
    val body: String,
    val isBoardOwner: Boolean,
    val tags: List<String>?,
    val title: String,
    val commentCount: Int
)