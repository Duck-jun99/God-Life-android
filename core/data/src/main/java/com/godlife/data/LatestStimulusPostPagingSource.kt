package com.godlife.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.godlife.data.repository.LocalPreferenceUserRepository
import com.godlife.network.api.RetrofitNetworkApi
import com.godlife.network.model.StimulusPostListQuery
import com.godlife.network.model.StimulusPostList
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LatestStimulusPostPagingSource @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi
): PagingSource<Int, StimulusPostList>() {

    override fun getRefreshKey(state: PagingState<Int, StimulusPostList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StimulusPostList> {

        return try {

            val page = params.key ?: 0
            var response = networkApi.getStimulusLatestPost(page = page)
            lateinit var data: StimulusPostListQuery

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