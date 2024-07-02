package com.godlife.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.godlife.data.LatestPostPagingSource
import com.godlife.data.LatestStimulusPostPagingSource
import com.godlife.network.api.RetrofitNetworkApi
import javax.inject.Inject

/**
 * Paging을 위해 LatestPostRepository를 따로 생성
 */
class LatestStimulusPostRepository @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi
) {
    fun getLatestStimulusPost() = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
        pagingSourceFactory = {
            LatestStimulusPostPagingSource(localPreferenceUserRepository, networkApi)
        }
    ).flow
}