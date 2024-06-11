package com.godlife.network.model

data class GetCommentsQuery(
    val status: String,
    val body: List<CommentBody>,
    val message: String,
)

data class CommentBody(
    val comment_id: String,
    val writer_id: String,
    val nickname: String,
    val comment: String,
    val aboutMe: String,
    val writtenAt: String,
    val profileURL: String,
    val commentOwner: Boolean
)

data class CommentQuery(
    val status: String,
    val body: Boolean,
    val message: String,
)
