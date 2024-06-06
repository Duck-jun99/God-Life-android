package com.godlife.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.godlife.data.repository.LocalPreferenceUserRepository
import com.godlife.network.model.PostDetailBody
import com.godlife.network.retrofit.RetrofitNetworkApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchPostPagingSource @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi,

    private val keyword: String,
    private val tags: String,
    private val nickname: String

): PagingSource<Int, PostDetailBody>() {

    override fun getRefreshKey(state: PagingState<Int, PostDetailBody>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostDetailBody> {
        return try {
            // Authorization 토큰을 비동기적으로 가져옴
            val authorization = withContext(Dispatchers.IO) {
                "Bearer ${localPreferenceUserRepository.getAccessToken()}"
            }

            val page = params.key ?: 1
            val response = networkApi.searchPost(authorization = authorization, page = page, keyword = keyword, tag = tags, nickname = nickname)

            LoadResult.Page(
                data = response.body,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.body.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}