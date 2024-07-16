package com.godlife.community_page.stimulus.recommended_post

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.community_page.stimulus.StimulusPostUiState
import com.godlife.domain.GetRecommendedStimulusPostUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.StimulusPostList
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendedStimulusPostViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getRecommendedStimulusPostUseCase: GetRecommendedStimulusPostUseCase,
    private val reissueUseCase: ReissueUseCase
): ViewModel() {

    /**
     * State
     */

    private val _uiState = MutableStateFlow<StimulusPostUiState>(StimulusPostUiState.Loading)
    val uiState: StateFlow<StimulusPostUiState> = _uiState

    /**
     * Data
     */

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    //게시물
    private val _postList = MutableStateFlow<List<StimulusPostList?>>(emptyList())
    val postList: StateFlow<List<StimulusPostList?>> = _postList

    //게시물을 호출했는지 플래그
    private var isGetPost = mutableStateOf(false)

    /**
     * Init
     */

    init {

        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }

        //추천 게시물 호출
        getRecommendedStimulusPost()

    }

    /**
     * Functions
     */

    private fun getRecommendedStimulusPost(){

        if(!isGetPost.value){

            viewModelScope.launch {
                val result = getRecommendedStimulusPostUseCase.executeGetRecommendedStimulusPost(auth.value)

                result
                    .onSuccess {
                        _postList.value = data.body
                        _uiState.value = StimulusPostUiState.Success("성공")
                    }
                    .onError {
                        _uiState.value = StimulusPostUiState.Error("${this.response.code()} Error")
                    }
                    .onException {
                        _uiState.value = StimulusPostUiState.Error(this.message())
                    }
            }
            isGetPost.value = true
        }
    }


}