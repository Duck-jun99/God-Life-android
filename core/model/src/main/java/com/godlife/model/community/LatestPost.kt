package com.godlife.model.community

data class LatestPostItem(
    val name: String,
    val title: String,
    val tagItem: List<TagItem>,

    //수정 필요
    val rank: String,
    val image: String = "",

    //조회수
    val view: Int = 10,
    //댓글
    val comments: Int = 1

    //게시 시간 필요
)

