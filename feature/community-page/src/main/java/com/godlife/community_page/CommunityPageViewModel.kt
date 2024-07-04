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
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.network.model.PostDetailBody
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

@HiltViewModel
class CommunityPageViewModel @Inject constructor(
    private val getLatestPostUseCase: GetLatestPostUseCase,
    private val searchPostUseCase: SearchPostUseCase,
    private val getWeeklyFamousPostUseCase: GetFamousPostUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val reissueUseCase: ReissueUseCase
): ViewModel(){


    /**
     * State
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<CommunityPageUiState>(CommunityPageUiState.Loading)
    val uiState: StateFlow<CommunityPageUiState> = _uiState

    // 새로고침 상태
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    /**
     * Data
     */

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    //현재 선택되어 있는 라우트 이름
    var selectedRoute = mutableStateOf("")

    //최신 게시물을 호출한 적이 있는지 플래그
    private var latestFlag = mutableIntStateOf(0)

    //조회된 최신 게시물, 페이징을 이용하기에 지연 초기화
    lateinit var latestPostList: Flow<PagingData<PostDetailBody>>

    //주간 인기 게시물을 호출한 적이 있는지 플래그
    private var weeklyFamousFlag = mutableIntStateOf(0)

    //전체 인기 게시물을 호출한 적이 있는지 플래그
    private var allFamousFlag = mutableIntStateOf(0)

    //조회된 일주일 인기 게시물
    private val _weeklyFamousPostList = MutableStateFlow<List<PostDetailBody>>(emptyList())
    val weeklyFamousPostList: StateFlow<List<PostDetailBody>> = _weeklyFamousPostList

    //조회된 전체 인기 게시물
    private val _allFamousPostList = MutableStateFlow<List<PostDetailBody>>(emptyList())
    val allFamousPostList: StateFlow<List<PostDetailBody>> = _allFamousPostList

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

    init {
        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }
    }

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
        if(weeklyFamousFlag.value == 0){

            _uiState.value = CommunityPageUiState.Loading

            viewModelScope.launch {
                val result = getWeeklyFamousPostUseCase.executeGetWeeklyFamousPost(authorId = auth.value)

                result
                    .onSuccess {
                        _weeklyFamousPostList.value = data.body

                        _uiState.value = CommunityPageUiState.Success("일주일 인기 게시물 조회 완료")


                        weeklyFamousFlag.value += 1

                    }
                    .onError {
                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { getWeeklyFamousPost() })

                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CommunityPageUiState.Error("오류가 발생했습니다.")
                    }

            }

        }

    }

    //전체 인기 게시물 불러오기
    fun getAllFamousPost(){

        // 인기 게시물 API를 호출한 적이 없을 때에만 실행
        if(allFamousFlag.value == 0){

            _uiState.value = CommunityPageUiState.Loading

            viewModelScope.launch {
                val result = getWeeklyFamousPostUseCase.executeGetAllFamousPost(authorId = auth.value)
                result
                    .onSuccess {
                        _allFamousPostList.value = data.body
                        _uiState.value = CommunityPageUiState.Success("전체 인기 게시물 조회 완료")
                        allFamousFlag.value += 1
                    }
                    .onError {
                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { getAllFamousPost() })

                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CommunityPageUiState.Error("오류가 발생했습니다.")
                    }

            }

        }

    }

    // 뷰 새로 고침 함수
    fun refresh() {

        _uiState.value = CommunityPageUiState.Loading

        when(uiState.value){
            is CommunityPageUiState.Success -> {
                _isRefreshing.value = false
            }
            is CommunityPageUiState.Error -> {
                _isRefreshing.value = false
            }

            is CommunityPageUiState.Loading -> {
                _isRefreshing.value = true
            }
        }

        if(selectedRoute.value == "LatestPostScreen"){
            // 최신 게시물 조회 플래그 초기화
            latestFlag.value = 0
            getLatestPost()
        }
        else if(selectedRoute.value == "FamousPostScreen"){
            // 인기 게시물 조회 플래그 초기화
            weeklyFamousFlag.value = 0
            getWeeklyFamousPost()
        }

        // 나머지도 구현해야됨
    }


    // refresh token 갱신 후 Callback 실행
    private fun reIssueRefreshToken(callback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {

            var auth = ""
            launch { auth = "Bearer ${localPreferenceUserUseCase.getRefreshToken()}" }.join()

            val response = reissueUseCase.executeReissue(auth)

            response
                //성공적으로 넘어오면 유저 정보의 토큰을 갱신
                .onSuccess {

                    localPreferenceUserUseCase.saveAccessToken(data.body.accessToken)
                    localPreferenceUserUseCase.saveRefreshToken(data.body.refreshToken)

                    //callback 실행
                    callback()

                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 로컬에서 토큰 삭제하고 로그아웃 메시지
                    if(this.response.code() == 400){

                        deleteLocalToken()

                        // UI State Error로 변경 및 로그아웃 메시지
                        _uiState.value = CommunityPageUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = CommunityPageUiState.Error("오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = CommunityPageUiState.Error("오류가 발생했습니다.")

                }


        }
    }

    // 로컬에서 토큰 및 사용자 정보 삭제
    private fun deleteLocalToken() {

        viewModelScope.launch(Dispatchers.IO) {

            // 로컬 데이터베이스에서 사용자 정보 삭제 후 완료되면 true 반환
            localPreferenceUserUseCase.removeAccessToken()
            localPreferenceUserUseCase.removeUserId()
            localPreferenceUserUseCase.removeRefreshToken()

        }

    }


}