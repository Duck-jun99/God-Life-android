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
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getSearchPostUseCase: SearchPostUseCase,
    private val reissueUseCase: ReissueUseCase
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

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    //사용자 정보
    private val _userInfo = MutableStateFlow<UserProfileBody>(UserProfileBody("", "", "", "", 0, 0, true))
    val userInfo: StateFlow<UserProfileBody> = _userInfo

    //조회된 사용자 굿생 인증 게시물
    private val _userPostList = MutableStateFlow<PagingData<PostDetailBody>>(PagingData.empty())
    val userPostList: StateFlow<PagingData<PostDetailBody>> = _userPostList

    //조회된 사용자 굿생 자극 게시물
    private val _userStimulusPostList = MutableStateFlow<List<StimulusPostList>>(emptyList())
    val userStimulusPostList: StateFlow<List<StimulusPostList>> = _userStimulusPostList

    /**
     * Init
     */
    init {
        viewModelScope.launch {
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }
    }


    /**
     * ProfileScreen에서 사용할 함수
     */

    // 사용자 정보 가져오기
    fun getUserProfile(memberId: String){

        if(auth.value != ""){

            val memberId = memberId

            viewModelScope.launch(Dispatchers.IO) {
                val result = getUserProfileUseCase.executeGetUserProfile(auth.value, memberId)

                result
                    .onSuccess {

                        _userInfo.value = data.body

                        //getUserPosts()

                        _uiState.value = ProfileUiState.Success("success")


                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken { getUserProfile(memberId) }

                        }
                        else {
                            _uiState.value = ProfileUiState.Error("오류가 발생했습니다.")
                        }

                    }
                    .onException {

                        _uiState.value = ProfileUiState.Error("오류가 발생했습니다.")

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
    fun getUserPosts(){
        if(userInfo.value.nickname != null){

            viewModelScope.launch {
                getSearchPostUseCase.executeSearchPost(keyword = "", tags = "", nickname = userInfo.value.nickname).cachedIn(viewModelScope).collect {
                    _userPostList.value = it
                }
            }

        }
    }

    // 조회한 사용자의 굿생 자극 게시물 불러오기
    fun getUserStimulusPosts(){
        if(userInfo.value.nickname != null){
            viewModelScope.launch {

                val result = getSearchPostUseCase.executeSearchStimulusPost(auth.value, nickname = userInfo.value.nickname, title = "", introduction = "")

                result
                    .onSuccess {
                        _userStimulusPostList.value = data.body
                    }

            }
        }
    }

    /**
     * Private 함수
     */

    // refresh token 갱신 후 Callback 실행
    private fun reIssueRefreshToken(callback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {

            val response = reissueUseCase.executeReissue(auth.value)

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
                        _uiState.value = ProfileUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = ProfileUiState.Error("오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = ProfileUiState.Error("오류가 발생했습니다.")

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