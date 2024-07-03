package com.godlife.network.model

data class StimulusPostQuery(
    val status: String,
    val body: Long,
    val message: String,
)

data class CreatePostRequest(
    val boardId: Long,
    val title: String,
    val content: String,
    val thumbnailUrl: String,
    val introduction: String
)

data class LatestStimulusPostQuery(
    val status: String,
    val body: List<StimulusPostList>,
    val message: String,
)

data class StimulusPostList(
    val title: String,
    val boardId: Int,
    val thumbnailUrl: String,
    val introduction: String,
    val nickname: String,
    val godLifeScore: Int,
    val views: Int,
)

data class StimulusPostDetailQuery(
    val status: String,
    val body: StimulusPost,
    val message: String,
)

data class StimulusPost(
    val title: String,
    val boardId: Int,
    val thumbnailUrl: String,
    val introduction: String,
    val nickname: String,
    val godLifeScore: Int,
    val views: Int,
    val owner: Boolean,
    val content: String,
    val writerId: Int,
    val createDate: String
)