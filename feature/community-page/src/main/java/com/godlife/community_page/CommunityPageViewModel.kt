package com.godlife.community_page

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.domain.GetFamousPostUseCase
import com.godlife.domain.GetRankingUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.RankingBody
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CommunityPageUiState {
    object Loading : CommunityPageUiState()
    data class Success(val data: String) : CommunityPageUiState()
    data class Error(val message: String) : CommunityPageUiState()
}

sealed class RankingPageUiState {
    object Loading : RankingPageUiState()
    data class Success(val data: String) : RankingPageUiState()
    data class Error(val message: String) : RankingPageUiState()
}

@HiltViewModel
class CommunityPageViewModel @Inject constructor(
    private val getLatestPostUseCase: GetLatestPostUseCase,
    private val searchPostUseCase: SearchPostUseCase,
    private val getWeeklyFamousPostUseCase: GetFamousPostUseCase,
    private val getRankingUseCase: GetRankingUseCase,
): ViewModel(){


    /**
     * State
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<CommunityPageUiState>(CommunityPageUiState.Loading)
    val uiState: StateFlow<CommunityPageUiState> = _uiState

    // 명예의 전당 UI 상태
    private val _rankingUiState = MutableStateFlow<RankingPageUiState>(RankingPageUiState.Loading)
    val rankingUiState: StateFlow<RankingPageUiState> = _rankingUiState

    /**
     * Data
     */

    //현재 선택되어 있는 라우트 이름
    var selectedRoute = mutableStateOf("")

    //최신 게시물을 호출한 적이 있는지 플래그
    private var latestFlag = mutableIntStateOf(0)

    //조회된 최신 게시물, 페이징을 이용하기에 지연 초기화
    lateinit var latestPostList: Flow<PagingData<PostDetailBody>>

    //주간 인기 게시물을 호출한 적이 있는지 플래그
    private var weeklyFamousFlag = mutableStateOf(false)

    //전체 인기 게시물을 호출한 적이 있는지 플래그
    private var allFamousFlag = mutableStateOf(false)

    //주간 명예의 전당을 호출한 적이 있는지 플래그
    private var weeklyRankingFlag = mutableStateOf(false)

    //전체 명예의 전당을 호출한 적이 있는지 플래그
    private var allRankingFlag = mutableStateOf(false)

    //전체 명예의 전당에서 해당하는 유저의 게시물 호출한 적이 있는지 플래그
    private var rankingUserPostFlag = mutableStateOf(false)

    //조회된 일주일 인기 게시물
    private val _weeklyFamousPostList = MutableStateFlow<List<PostDetailBody>>(emptyList())
    val weeklyFamousPostList: StateFlow<List<PostDetailBody>> = _weeklyFamousPostList

    //조회된 전체 인기 게시물
    private val _allFamousPostList = MutableStateFlow<List<PostDetailBody>>(emptyList())
    val allFamousPostList: StateFlow<List<PostDetailBody>> = _allFamousPostList

    //조회된 주간 명예의 전당
    private val _weeklyRankingList = MutableStateFlow<List<RankingBody>>(emptyList())
    val weeklyRankingList: StateFlow<List<RankingBody>> = _weeklyRankingList

    //조회된 전체 명예의 전당
    private val _allRankingList = MutableStateFlow<List<RankingBody>>(emptyList())
    val allRankingList: StateFlow<List<RankingBody>> = _allRankingList

    //전체 명예의 전당에서 해당 유저의 게시물
    private val _rankingUserPostList = MutableStateFlow<PagingData<PostDetailBody>>(PagingData.empty())
    val rankingUserPostList: StateFlow<PagingData<PostDetailBody>> = _rankingUserPostList
    //lateinit var rankingUserPostList: Flow<PagingData<PostDetailBody>>

    //검색어
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    //검색된 게시물
    private val _searchedPosts = MutableStateFlow<PagingData<PostDetailBody>>(PagingData.empty())
    val searchedPosts: StateFlow<PagingData<PostDetailBody>> = _searchedPosts

    //최상단 타이틀
    var topTitle = mutableStateOf("굿생 커뮤니티")


    /**
     * Init
     */


    /**
     * Functions
     */

    //검색어 변경
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


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

    fun changeCurrentRoute(route: String){
        selectedRoute.value = route
    }

    //최신 게시물 불러오기
    fun getLatestPost(){

        // 최신 게시물 API를 호출한 적이 없을 때에만 실행
        if(latestFlag.value == 0){

            // Loading으로 초기화
            _uiState.value = CommunityPageUiState.Loading

            latestPostList = getLatestPostUseCase.executeGetLatestPost().cachedIn(viewModelScope)

            _uiState.value = CommunityPageUiState.Success("최신 게시물 조회 완료")

            latestFlag.value += 1

        }
    }

    //일주일 인기 게시물 불러오기
    fun getWeeklyFamousPost(){

        // 인기 게시물 API를 호출한 적이 없을 때에만 실행
        if(!weeklyFamousFlag.value){
            weeklyFamousFlag.value = true
            _uiState.value = CommunityPageUiState.Loading

            viewModelScope.launch {
                val result = getWeeklyFamousPostUseCase.executeGetWeeklyFamousPost()

                result
                    .onSuccess {
                        _weeklyFamousPostList.value = data.body

                        _uiState.value = CommunityPageUiState.Success("일주일 인기 게시물 조회 완료")

                    }
                    .onError {
                        Log.e("getWeeklyFamousPost", this.message())

                        _rankingUiState.value = RankingPageUiState.Error("${this.response.code()} Error")
                    }
                    .onException {

                        Log.e("getWeeklyFamousPost", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CommunityPageUiState.Error(this.message())
                    }

            }

        }

    }

    //전체 인기 게시물 불러오기
    fun getAllFamousPost(){

        // 인기 게시물 API를 호출한 적이 없을 때에만 실행
        if(!allFamousFlag.value){
            allFamousFlag.value = true

            _uiState.value = CommunityPageUiState.Loading

            viewModelScope.launch {
                val result = getWeeklyFamousPostUseCase.executeGetAllFamousPost()
                result
                    .onSuccess {
                        _allFamousPostList.value = data.body
                        _uiState.value = CommunityPageUiState.Success("전체 인기 게시물 조회 완료")
                    }
                    .onError {
                        Log.e("getAllFamousPost", this.message())

                        _rankingUiState.value = RankingPageUiState.Error("${this.response.code()} Error")
                    }
                    .onException {

                        Log.e("getAllFamousPost", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CommunityPageUiState.Error(this.message())
                    }

            }

        }

    }

    //주간 명예의 전당 불러오기
    fun getWeeklyRanking(){
        if(!weeklyRankingFlag.value){
            weeklyRankingFlag.value = true
            _rankingUiState.value = RankingPageUiState.Loading

            viewModelScope.launch {
                val result = getRankingUseCase.executeGetWeeklyRanking()
                result
                    .onSuccess {
                        _weeklyRankingList.value = data.body
                        //_rankingUiState.value = RankingPageUiState.Success("주간 명예의 전당 조회 완료")

                        getAllRanking()

                    }
                    .onError {
                        Log.e("getWeeklyRanking", this.message())

                        _rankingUiState.value = RankingPageUiState.Error("${this.response.code()} Error")
                    }
                    .onException {

                        Log.e("getWeeklyRanking", "${this.message}")

                        // UI State Error로 변경
                        _rankingUiState.value = RankingPageUiState.Error(this.message())
                    }
            }


        }
    }

    //전체 명예의 전당 불러오기
    fun getAllRanking(){

        if(!allRankingFlag.value){
            allRankingFlag.value = true
            _rankingUiState.value = RankingPageUiState.Loading

            viewModelScope.launch {
                val result = getRankingUseCase.executeGetAllRanking()
                result
                    .onSuccess {
                        _allRankingList.value = data.body
                        _rankingUiState.value = RankingPageUiState.Success("명예의 전당 조회 완료")

                    }
                    .onError {
                        Log.e("getAllRanking", this.message())

                        _rankingUiState.value = RankingPageUiState.Error("${this.response.code()} Error")

                    }
                    .onException {

                        Log.e("getAllRanking", "${this.message}")

                        // UI State Error로 변경
                        _rankingUiState.value = RankingPageUiState.Error(this.message())
                    }
            }


        }

    }

    //rankingUserPostFlag 초기화
    fun updateRankingUserPostFlag(){
        rankingUserPostFlag.value = false
    }

    //명예의 전당 유저의 게시물 불러오기
    fun getRankingUserPost(
        keyword: String = "",
        tags: String = "",
        nickname: String
    ) {
        if(!rankingUserPostFlag.value){
            rankingUserPostFlag.value = true

            viewModelScope.launch {

                searchPostUseCase.executeSearchPost(keyword, tags, nickname)
                    .collectLatest {
                        _rankingUserPostList.value = it
                    }
                Log.e("getRankingUserPost", "nickname : $nickname")

            }
        }


    }


    override fun onCleared() {
        Log.e("CommunityPageViewModel", "onCleared")
        super.onCleared()
    }


}