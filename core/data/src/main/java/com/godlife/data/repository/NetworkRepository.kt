package com.godlife.data.repository

import com.godlife.network.model.NetworkUserQuery

interface NetworkRepository {
    suspend fun getUserInfo(id : String) : NetworkUserQuery?


}