package com.godlife.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.GetUserProfileUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.StimulusPostList
import com.godlife.network.model.UserProfileBody
import com.godlife.network.model.UserProfileQuery
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val data: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getSearchPostUseCase: SearchPostUseCase,
): ViewModel(){

    /**
     * State
     */

    // 전체 UI 상태
    // /*TODO*/ 수정 필요
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Success("TEST"))
    val uiState: StateFlow<ProfileUiState> = _uiState

    // 이미지 확대 뷰 Visiblity 상태
    private val _fullImageVisibility = MutableStateFlow(false)
    val fullImageVisibility: StateFlow<Boolean> = _fullImageVisibility


    /**
     * Data
     */

    // 확대할 이미지 비트맵 변수
    private val _fullImageBitmap = MutableStateFlow<Bitmap?>(null)
    val fullImageBitmap: StateFlow<Bitmap?> = _fullImageBitmap

    //사용자 정보
    private val _userInfo = MutableStateFlow<UserProfileBody>(UserProfileBody("", "", "", "", 0, 0, true))
    val userInfo: StateFlow<UserProfileBody> = _userInfo

    //굿생 티어
    private val _tier = MutableStateFlow<String>("로딩중")
    val tier: StateFlow<String> = _tier

    //조회된 사용자 굿생 인증 게시물
    private val _userPostList = MutableStateFlow<PagingData<PostDetailBody>>(PagingData.empty())
    val userPostList: StateFlow<PagingData<PostDetailBody>> = _userPostList

    //조회된 사용자 굿생 자극 게시물
    private val _userStimulusPostList = MutableStateFlow<List<StimulusPostList>>(emptyList())
    val userStimulusPostList: StateFlow<List<StimulusPostList>> = _userStimulusPostList

    //사용자 정보 불러왔는지 플래그
    private var _userInfoLoaded = MutableStateFlow(false)

    //사용자 굿생 인증 게시물 불러왔는지 플래그
    private var _userPostLoaded = MutableStateFlow(false)

    //사용자 굿생 자극 게시물 불러왔는지 플래그
    private var _userStimulusPostLoaded = MutableStateFlow(false)


    /**
     * ProfileScreen에서 사용할 함수
     */

    // 사용자 정보 가져오기
    fun getUserProfile(memberId: String){
        if(!_userInfoLoaded.value){
            _userInfoLoaded.value = true
            _uiState.value = ProfileUiState.Loading
            val memberId = memberId

            viewModelScope.launch(Dispatchers.IO) {
                val result = getUserProfileUseCase.executeGetUserProfile(memberId)

                result
                    .onSuccess {

                        _userInfo.value = data.body

                        setTier()

                        _uiState.value = ProfileUiState.Success("success")

                        getUserPosts()


                    }
                    .onError {

                        // 토큰 만료시
                        if(this.response.code() == 401){

                            _uiState.value = ProfileUiState.Error("세션이 만료되었습니다. 다시 로그인해주세요.")

                        }
                        else {
                            _uiState.value = ProfileUiState.Error("${this.response.code()} Error")
                        }

                    }
                    .onException {

                        _uiState.value = ProfileUiState.Error(this.message())

                    }

            }
        }


    }

    // 이미지 확대 뷰 Visiblity 상태 변경
    fun setFullImageVisibility(){
        _fullImageVisibility.value = !fullImageVisibility.value
    }

    // 확대할 이미지 비트맵 변경
    fun setFullImageBitmap(bitmap: Bitmap){
        _fullImageBitmap.value = bitmap
    }

    // 조회한 사용자의 굿생 인증 게시물 불러오기
    private fun getUserPosts(){
        if(!_userPostLoaded.value){
            _userPostLoaded.value = true
            _uiState.value = ProfileUiState.Loading
            viewModelScope.launch {
                getSearchPostUseCase.executeSearchPost(keyword = "", tags = "", nickname = userInfo.value.nickname).cachedIn(viewModelScope).collect {
                    _userPostList.value = it
                    _uiState.value = ProfileUiState.Success("success")
                }
            }

        }
    }

    // 조회한 사용자의 굿생 자극 게시물 불러오기
    fun getUserStimulusPosts(){
        if(!_userStimulusPostLoaded.value){
            _userStimulusPostLoaded.value = true
            _uiState.value = ProfileUiState.Loading
            viewModelScope.launch {

                val result = getSearchPostUseCase.executeSearchStimulusPost(nickname = userInfo.value.nickname, title = "", introduction = "")

                result
                    .onSuccess {
                        _userStimulusPostList.value = data.body
                        _uiState.value = ProfileUiState.Success("success")
                    }
                    .onError {

                        // 토큰 만료시
                        if(this.response.code() == 401){

                            _uiState.value = ProfileUiState.Error("세션이 만료되었습니다. 다시 로그인해주세요.")

                        }
                        else {
                            _uiState.value = ProfileUiState.Error("${this.response.code()} Error")
                        }

                    }
                    .onException {

                        _uiState.value = ProfileUiState.Error(this.message())

                    }

            }
        }
    }

    private fun setTier(){
        viewModelScope.launch(Dispatchers.Default) {
            when(userInfo.value.godLifeScore){

                in 0..29 -> {
                    _tier.value = "새싹"
                }

                in 30..99 -> {
                    _tier.value = "브론즈"
                }

                in 100..199 -> {
                    _tier.value = "실버"
                }

                in 200..499 -> {
                    _tier.value = "골드"
                }

                in 500..699 -> {
                    _tier.value = "다이아"
                }

                in 700..999 -> {
                    _tier.value = "마스터"
                }

                in 1000..Int.MAX_VALUE -> {
                    _tier.value = "레전드"
                }

            }
        }
    }
}
