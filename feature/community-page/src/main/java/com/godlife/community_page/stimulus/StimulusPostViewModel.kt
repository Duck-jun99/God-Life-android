package com.godlife.community_page.stimulus

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.godlife.domain.GetLatestStimulusPostUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.StimulusPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StimulusPostUiState {
    object Loading : StimulusPostUiState()
    data class Success(val data: String) : StimulusPostUiState()
    data class Error(val message: String) : StimulusPostUiState()
}

@HiltViewModel
class StimulusPostViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val latestStimulusPostUseCase: GetLatestStimulusPostUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
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

    //최신 게시물을 호출한 적이 있는지 플래그
    private var latestFlag = mutableIntStateOf(0)

    //조회된 최신 게시물, 페이징을 이용하기에 지연 초기화
    lateinit var latestPostList: Flow<PagingData<StimulusPost>>


    /**
     * Init
     */

    init {

        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }

        getLatestStimulusPost()

    }

    /**
     * Functions
     */

    private fun getLatestStimulusPost() {

        if(latestFlag.value == 0) {
            viewModelScope.launch {
                latestPostList = latestStimulusPostUseCase.executeGetLatestStimulusPost()
                latestFlag.value += 1
            }
        }

    }

}