package com.godlife.data

import com.godlife.network.NetworkDataSource
import com.godlife.network.model.NetworkUserQuery
import com.godlife.network.retrofit.RetrofitNetworkApi
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val notionApi: RetrofitNetworkApi
) : NetworkDataSource {

    override suspend fun getUserInfo( id : String): NetworkUserQuery? {
        return notionApi.getUserInfo(id = id)

    }


}