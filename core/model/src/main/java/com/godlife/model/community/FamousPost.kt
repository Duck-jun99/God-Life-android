package com.godlife.model.community

data class FamousPostItem(

    val board_id: Int,
    val imagesURL: List<String>?,
    val writtenAt: String,
    val views: Int,
    val godScore: Int,
    val body: String,
    val isBoardOwner: Boolean,
    val tags: List<String>,
    val title: String,
    val commentCount: Int,
    val profileURL: String?,
    val nickname: String,
    val tier: String,
    val whoAmI: String,
    val memberLikedBoard: Boolean
)

data class TagItem(
    val tagName: String
)
