package com.godlife.community_page.stimulus.latest_post

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.godlife.community_page.stimulus.StimulusPostUiState
import com.godlife.domain.GetLatestStimulusPostUseCase
import com.godlife.domain.GetMostViewStimulusPostUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.StimulusPostList
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestStimulusPostViewModel @Inject constructor(
    private val getLatestStimulusPostUseCase: GetLatestStimulusPostUseCase
): ViewModel() {

    /**
     * State
     */

    private val _uiState = MutableStateFlow<StimulusPostUiState>(StimulusPostUiState.Loading)
    val uiState: StateFlow<StimulusPostUiState> = _uiState

    /**
     * Data
     */


    /*
    //게시물
    private val _postList = MutableStateFlow<List<StimulusPostList?>>(emptyList())
    val postList: StateFlow<List<StimulusPostList?>> = _postList

     */
    //조회된 최신 게시물, 페이징을 이용하기에 지연 초기화
    lateinit var latestPostList: Flow<PagingData<StimulusPostList>>

    //게시물을 호출했는지 플래그
    private var isGetPost = mutableStateOf(false)

    /**
     * Init
     */

    init {

        //최신 게시물 호출
        getLatestStimulusPost()

    }

    /**
     * Functions
     */

    private fun getLatestStimulusPost(){

        if(!isGetPost.value){

            latestPostList = getLatestStimulusPostUseCase.executeGetLatestStimulusPost().cachedIn(viewModelScope)
            _uiState.value = StimulusPostUiState.Success("성공")

            isGetPost.value = true
        }
    }


}