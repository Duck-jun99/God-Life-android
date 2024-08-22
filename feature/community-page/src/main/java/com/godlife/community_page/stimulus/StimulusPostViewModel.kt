package com.godlife.community_page.stimulus

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.godlife.domain.GetFamousAuthorStimulusPostUseCase
import com.godlife.domain.GetFamousPostUseCase
import com.godlife.domain.GetLatestStimulusPostUseCase
import com.godlife.domain.GetMostViewStimulusPostUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.GetRecommendedStimulusPostUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.StimulusPostList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StimulusPostUiState {
    object Loading : StimulusPostUiState()
    data class Success(val data: String) : StimulusPostUiState()
    data class Error(val message: String) : StimulusPostUiState()
}

@HiltViewModel
class StimulusPostViewModel @Inject constructor(
): ViewModel() {

    /**
     * State
     */

    private val _uiState = MutableStateFlow<StimulusPostUiState>(StimulusPostUiState.Loading)
    val uiState: StateFlow<StimulusPostUiState> = _uiState

    //FAB 버튼 Expaned
    val fabExpanded = mutableStateOf(false)

    //도움말 Dialog Visible
    val helpDialogVisible = mutableStateOf(false)

    /**
     * Event
     */

    fun setFabExpanded() {
        fabExpanded.value = !fabExpanded.value
    }

    fun setHelpDialogVisible() {
        helpDialogVisible.value = !helpDialogVisible.value
    }





}