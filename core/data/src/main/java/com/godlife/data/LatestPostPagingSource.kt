package com.godlife.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.godlife.data.repository.LocalPreferenceUserRepository
import com.godlife.model.community.LatestContentUi
import com.godlife.network.model.PostDetailBody
import com.godlife.network.api.RetrofitNetworkApi
import com.godlife.network.model.LatestPostQuery
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

class LatestPostPagingSource @Inject constructor(
    private val localPreferenceUserRepository: LocalPreferenceUserRepository,
    private val networkApi: RetrofitNetworkApi,
    private val adLoaderBuilder: AdLoader.Builder
): PagingSource<Int, LatestContentUi>() {

    companion object {
        private const val DEFAULT_AD_INDEX = 3
    }

    override fun getRefreshKey(state: PagingState<Int, LatestContentUi>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LatestContentUi> {
        return try {
            val page = params.key ?: 1
            var response = networkApi.getLatestPost(page = page, keyword = "", tag = "")
            //lateinit var data: LatestContentUi
            lateinit var data: LatestPostQuery

            //val latestContents = mutableListOf<LatestContentUi>()

            response
                .onSuccess {
                    //Log.e("LatestPostPagingSource", "data: ${this.data}")

                    data = this.data

                    //Log.e("LatestPostPagingSource", "data.body: ${data.body}")

                }
                .onError {
                    // handle error
                    Log.e("LatestPostPagingSource", "error: ${this.errorBody}")
                }
                .onException {
                    // handle exception
                    Log.e("LatestPostPagingSource", "exception: ${this.message}")
                }

            val list = data.body.map{
                LatestContentUi.PostDetailBody(
                    board_id = it.board_id,
                    imagesURL = it.imagesURL,
                    writtenAt = it.writtenAt,
                    views = it.views,
                    godScore = it.godScore,
                    body = it.body,
                    boardOwner = it.boardOwner,
                    tags = it.tags,
                    title = it.title,
                    commentCount = it.commentCount,
                    profileURL = it.profileURL,
                    nickname = it.nickname,
                    tier = it.tier,
                    whoAmI = it.whoAmI,
                    memberLikedBoard = it.memberLikedBoard,
                    writerId = it.writerId
                )
            }

            val latestContents = mutableListOf<LatestContentUi>().apply {
                addAll(list)

                if (list.isNotEmpty()) {
                    val adIndex = min(DEFAULT_AD_INDEX, size)
                    add(adIndex, LatestContentUi.NativeAds(getNativeAds(adLoaderBuilder)))
                }
            }


            LoadResult.Page(
                data = latestContents,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (latestContents.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            Log.e("LatestPostPagingSource", "exception: ${e.message}")
            LoadResult.Error(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getNativeAds(adLoaderBuilder: AdLoader.Builder) =
        suspendCancellableCoroutine<NativeAd> { emit ->

            adLoaderBuilder
                .forNativeAd { nativeAd ->
                    emit.resume(nativeAd) {}
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        emit.cancel()
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build()
                )
                .build()
                .loadAd(AdManagerAdRequest.Builder().build())
        }
}