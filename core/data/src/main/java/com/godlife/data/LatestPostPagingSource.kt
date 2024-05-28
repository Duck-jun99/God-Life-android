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

class LatestPostPagingSource @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi
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

            Log.e("LatestPostPagingSource", "authorization: ${authorization}")

            val page = params.key ?: 1
            val response = networkApi.getLatestPost(authorization = authorization, page = page, keyword = "", tag = "")

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