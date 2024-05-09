package com.godlife.network

import com.godlife.network.model.NetworkUserQuery

interface NetworkDataSource {
    suspend fun getUserInfo(id: String): NetworkUserQuery?

}
