package com.godlife.community_page.stimulus.recommended_author

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.community_page.stimulus.StimulusPostUiState
import com.godlife.domain.GetFamousAuthorStimulusPostUseCase
import com.godlife.network.model.RecommendPostBody
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
class RecommendedAuthorViewModel @Inject constructor(
    private val getRecommendedAuthorStimulusPostUseCase: GetFamousAuthorStimulusPostUseCase
): ViewModel() {
    /**
     * State
     */

    private val _uiState = MutableStateFlow<StimulusPostUiState>(StimulusPostUiState.Loading)
    val uiState: StateFlow<StimulusPostUiState> = _uiState

    /**
     * Data
     */

    //추천 작가 정보
    private val _authorInfo = MutableStateFlow<RecommendPostBody?>(null)
    val authorInfo: StateFlow<RecommendPostBody?> = _authorInfo

    //게시물을 호출했는지 플래그
    private var isGetPost = mutableStateOf(false)

    /**
     * Init
     */

    init {

        //조회수 많은 게시물 호출
        getRecommendedAuthor()

    }

    /**
     * Functions
     */

    private fun getRecommendedAuthor(){

        if(!isGetPost.value){

            viewModelScope.launch {
                val result = getRecommendedAuthorStimulusPostUseCase.executeGetFamousAuthorStimulusPost()

                result
                    .onSuccess {
                        _authorInfo.value = data.body
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