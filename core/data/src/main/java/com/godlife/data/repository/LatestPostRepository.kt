package com.godlife.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.godlife.data.LatestPostPagingSource
import com.godlife.network.api.RetrofitNetworkApi
import com.google.android.gms.ads.AdLoader
import javax.inject.Inject

/**
 * Paging을 위해 LatestPostRepository를 따로 생성
 */
class LatestPostRepository @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi,
) {
    fun getLatestPost(
        adLoaderBuilder: AdLoader.Builder
    ) = Pager(
        config = PagingConfig(
            pageSize = 1,
        ),
        pagingSourceFactory = {
            LatestPostPagingSource(localPreferenceUserRepository, networkApi, adLoaderBuilder)
        }
    ).flow
}