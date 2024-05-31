package com.godlife.community_page.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.network.model.PostDetailBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LatestPostViewModel @Inject constructor(
    private val latestPostUseCase: GetLatestPostUseCase
): ViewModel() {

    fun getLatestPost(): Flow<PagingData<PostDetailBody>>
    = latestPostUseCase.executeGetLatestPost().cachedIn(viewModelScope)
}