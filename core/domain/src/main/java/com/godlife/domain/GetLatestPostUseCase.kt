package com.godlife.domain

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.NetworkRepository
import com.google.android.gms.ads.AdLoader
import javax.inject.Inject

class GetLatestPostUseCase @Inject constructor(
    private val latestPostRepository: LatestPostRepository
) {
    fun executeGetLatestPost(adLoaderBuilder: AdLoader.Builder)
    = latestPostRepository.getLatestPost(adLoaderBuilder)
}