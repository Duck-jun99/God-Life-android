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
    val body: List<StimulusPost>,
    val message: String,
)

data class StimulusPost(
    val title: String,
    val boardId: Int,
    val thumbnailUrl: String,
    val introduction: String,
    val nickname: String,
    val godLifeScore: Int,
    val owner: Boolean,
    val content: String,
    val writerId: Int,
)

data class StimulusPostDetailQuery(
    val status: String,
    val body: StimulusPost,
    val message: String,
)