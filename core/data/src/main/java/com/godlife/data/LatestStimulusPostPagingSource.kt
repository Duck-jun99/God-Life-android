package com.godlife.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.godlife.data.repository.LocalPreferenceUserRepository
import com.godlife.network.model.PostDetailBody
import com.godlife.network.api.RetrofitNetworkApi
import com.godlife.network.model.LatestPostQuery
import com.godlife.network.model.LatestStimulusPostQuery
import com.godlife.network.model.StimulusPost
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LatestStimulusPostPagingSource @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi
): PagingSource<Int, StimulusPost>() {

    override fun getRefreshKey(state: PagingState<Int, StimulusPost>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StimulusPost> {

        return try {
            // Authorization 토큰을 비동기적으로 가져옴
            val authorization = withContext(Dispatchers.IO) {
                "Bearer ${localPreferenceUserRepository.getAccessToken()}"
            }

            val page = params.key ?: 0
            var response = networkApi.getStimulusLatestPost(authorization = authorization, page = page)
            lateinit var data: LatestStimulusPostQuery

            response
                .onSuccess {
                   data = this.data
                }
                .onError {

                }
                .onException {

                }

            LoadResult.Page(
                data = data.body,
                prevKey = if (page == 0) null else page.minus(1),
                nextKey = if (data.body.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}