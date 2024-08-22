package com.godlife.model.community

import com.google.android.gms.ads.nativead.NativeAd

sealed class LatestContentUi {
    data class PostDetailBody(
        val board_id: Int,
        val imagesURL: List<String>?,
        val writtenAt: String,
        val views: Int,
        val godScore: Int,
        val body: String,
        val boardOwner: Boolean,
        val tags: List<String>,
        val title: String,
        val commentCount: Int,
        val profileURL: String?,
        val nickname: String,
        val tier: String,
        val whoAmI: String?,
        val memberLikedBoard: Boolean,
        val writerId: Int,
    ) : LatestContentUi()

    data class NativeAds(
        val ad: NativeAd
    ) : LatestContentUi()
}




/**
 *  Deprecated */
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

