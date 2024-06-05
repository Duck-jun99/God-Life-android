package com.godlife.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.godlife.data.LatestPostPagingSource
import com.godlife.data.SearchPostPagingSource
import com.godlife.network.retrofit.RetrofitNetworkApi
import javax.inject.Inject


/**
 * Paging을 위해 SearchPostRepository를 따로 생성
 */
class SearchPostRepository @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi
) {
    fun getSearchedPost(
        keyword: String,
        tags: String,
        nickname: String
    ) = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
        pagingSourceFactory = {
            SearchPostPagingSource(localPreferenceUserRepository, networkApi, keyword, tags, nickname)
        }
    ).flow
}