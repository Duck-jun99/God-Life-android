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
