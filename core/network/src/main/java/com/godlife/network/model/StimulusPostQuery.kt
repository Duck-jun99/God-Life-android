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

data class StimulusPostListQuery(
    val status: String,
    val body: List<StimulusPostList>,
    val message: String,
)


data class RecommendPostQuery(
    val status: String,
    val body: RecommendPostBody,
    val message: String,
)

data class RecommendPostBody(
    val nickname: String,
    val backgroundUrl: String,
    val whoAmI: String,
    val profileUrl: String,
    val responses: List<StimulusPostList>
)


data class StimulusPostList(
    val title: String,
    val boardId: Int,
    val thumbnailUrl: String,
    val introduction: String,
    val nickname: String,
    val godLifeScore: Int,
    val view: Int,
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
    val view: Int,
    val owner: Boolean,
    val content: String,
    val writerId: Int,
    val createDate: String,
    val memberLikedBoard: Boolean
)