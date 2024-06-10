package com.godlife.community_page

import android.util.Log
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.network.model.PostDetailBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CommunityPageUiState {
    object Loading : CommunityPageUiState()
    data class Success(val data: String) : CommunityPageUiState()
    data class Error(val message: String) : CommunityPageUiState()
}

@HiltViewModel
class CommunityPageViewModel @Inject constructor(
    private val latestPostUseCase: GetLatestPostUseCase,
    private val searchPostUseCase: SearchPostUseCase
): ViewModel(){


    /**
     * State 관련
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<CommunityPageUiState>(CommunityPageUiState.Loading)
    val uiState: StateFlow<CommunityPageUiState> = _uiState

    //현재 선택되어 있는 라우트 이름
    var selectedRoute = mutableStateOf("")

    //검색어
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    //검색 뷰가 보일지 여부
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    //검색어 변경
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    private val _searchedPosts = MutableStateFlow<PagingData<PostDetailBody>>(PagingData.empty())
    val searchedPosts: StateFlow<PagingData<PostDetailBody>> = _searchedPosts

    fun getSearchedPost(
        keyword: String,
        tags: String = "",
        nickname: String = ""
    ) {
        viewModelScope.launch {

            searchPostUseCase.executeSearchPost(keyword, tags, nickname)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _searchedPosts.value = it
                }

        }
    }

    //최상단 타이틀
    var topTitle = mutableStateOf("굿생 커뮤니티")

    //최상단 타이틀 변경 (scaffoldState.bottomSheetState.currentValue에 따라 변경)
    fun changeTopTitle(scaffoldState: String){
        if(scaffoldState == "Expanded"){
            topTitle.value = mapingRouteToTitle(selectedRoute.value)
        }else{
            topTitle.value = "굿생 커뮤니티"
        }
    }

    fun mapingRouteToTitle(route: String): String{
        var title = ""
        if(route == "FamousPostScreen"){
            title = "인기 게시물"
        }
        else if(route == "LatestPostScreen"){
            title = "최신 게시물"
        }
        else if(route == "StimulusPostScreen"){
            title = "갓생 자극"
        }
        else if(route == "RankingScreen"){
            title = "명예의 전당"
        }
        else if(route == "SearchResultScreen"){
            title = "검색 결과"
        }
        return title
    }

    //최신 게시물에 대한 플래그
    private var latestFlag = mutableIntStateOf(0)

    lateinit var latestPostList: Flow<PagingData<PostDetailBody>>

    fun changeCurrentRoute(route: String){
        selectedRoute.value = route
    }


    /*

    // 곧 지워야 함. 밑에 getLatestPost()로 변경
    //최신 게시물 불러오기
    init {

        if(latestFlag.value == 0){

            latestPostList = latestPostUseCase.executeGetLatestPost().cachedIn(viewModelScope)
            latestFlag.value += 1
        }

    }

     */




    //최신 게시물 불러오기
    fun getLatestPost(){

        // 최신 게시물 API를 호출한 적이 없을 때에만 실행
        if(latestFlag.value == 0){

            // Loading으로 초기화
            _uiState.value = CommunityPageUiState.Loading

            latestPostList = latestPostUseCase.executeGetLatestPost().cachedIn(viewModelScope)

            _uiState.value = CommunityPageUiState.Success("최신 게시물 조회 완료")

            latestFlag.value += 1

        }
    }


}