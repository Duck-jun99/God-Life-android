package com.godlife.community_page.search.stimulus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.community_page.CommunityPageUiState
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.network.model.StimulusPostList
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchStimulusUiState {

    object Initial : SearchStimulusUiState()
    object Loading : SearchStimulusUiState()
    data class Success(val data: String) : SearchStimulusUiState()
    data class Error(val message: String) : SearchStimulusUiState()
}

@HiltViewModel
class StimulusSearchViewModel @Inject constructor(
    private val searchPostUseCase: SearchPostUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val reissueUseCase: ReissueUseCase
): ViewModel() {

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<SearchStimulusUiState>(SearchStimulusUiState.Initial)
    val uiState: StateFlow<SearchStimulusUiState> = _uiState

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    //검색 카테고리
    private val _searchCategory = MutableStateFlow<String>("post")
    val searchCategory: StateFlow<String> = _searchCategory

    //검색 카테고리 체크 플래그
    private val _checkPostCategory = MutableStateFlow<Boolean>(true)
    val checkPostCategory: StateFlow<Boolean> = _checkPostCategory
    private val _checkWriterCategory = MutableStateFlow<Boolean>(false)
    val checkWriterCategory: StateFlow<Boolean> = _checkWriterCategory

    //검색결과
    private val _searchResult = MutableStateFlow<List<StimulusPostList>>(emptyList())
    val searchResult: StateFlow<List<StimulusPostList>> = _searchResult


    init {
        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }
    }

    //체크박스 변경
    fun checkCategory(category: String){

        if(category == "post"){
            _checkPostCategory.value = true
            _checkWriterCategory.value = false
            _searchCategory.value = "post"
        }

        else{
            _checkPostCategory.value = false
            _checkWriterCategory.value = true
            _searchCategory.value = "writer"
        }
    }

    //검색
    suspend fun search(text: String){

        _searchResult.value = emptyList()

        if(text.length > 1){
            _uiState.value = SearchStimulusUiState.Loading

            // 게시물 검색일 경우
            if(searchCategory.value == "post"){
                val response = searchPostUseCase.executeSearchStimulusPost(auth.value, title = text, nickname = "", introduction = "")

                response
                    .onSuccess {

                        if(data.body.isNotEmpty()){
                            _searchResult.value = data.body
                            _uiState.value = SearchStimulusUiState.Success(data.toString())
                        }
                        else{
                            _uiState.value = SearchStimulusUiState.Error("검색 결과가 없습니다.")
                        }

                    }
                    .onError {
                        Log.e("onError", this.message())

                        // UI State Error로 변경
                        _uiState.value = SearchStimulusUiState.Error("오류가 발생했어요.\n잠시 뒤 다시 시도해주세요.")

                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = SearchStimulusUiState.Error("오류가 발생했어요.\n잠시 뒤 다시 시도해주세요.")
                    }

            }

            // 작가 검색일 경우
            else {

                val response = searchPostUseCase.executeSearchStimulusPost(auth.value, title = "", nickname = text, introduction = "")

                response
                    .onSuccess {
                        _uiState.value = SearchStimulusUiState.Success(data.toString())
                    }
                    .onError {
                        Log.e("onError", this.message())

                        // UI State Error로 변경
                        _uiState.value = SearchStimulusUiState.Error("오류가 발생했어요.\n잠시 뒤 다시 시도해주세요.")

                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = SearchStimulusUiState.Error("오류가 발생했어요.\n잠시 뒤 다시 시도해주세요.")
                    }

            }

        }

    }


}