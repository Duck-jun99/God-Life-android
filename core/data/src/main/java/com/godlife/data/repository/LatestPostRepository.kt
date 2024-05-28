package com.godlife.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.godlife.data.LatestPostPagingSource
import com.godlife.data.LocalPreferenceUserDataSourceImpl
import com.godlife.network.retrofit.RetrofitNetworkApi
import javax.inject.Inject

/**
 * Paging을 위해 LatestPostRepository를 따로 생성
 */
class LatestPostRepository @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi
) {
    fun getLatestPost() = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            LatestPostPagingSource(localPreferenceUserRepository, networkApi)
        }
    ).flow
}