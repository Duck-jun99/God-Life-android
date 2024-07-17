package com.godlife.community_page.post_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.DeleteCommentUseCase
import com.godlife.domain.GetCommentsUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.GetUserProfileUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.PlusGodScoreUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.StimulusPost
import com.godlife.network.model.UserProfileBody
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

sealed class StimulusPostDetailUiState {
    object Loading : StimulusPostDetailUiState()
    data class Success(val data: String) : StimulusPostDetailUiState()
    data class Error(val message: String) : StimulusPostDetailUiState()
}

@HiltViewModel
class StimulusPostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val plusGodScoreUseCase: PlusGodScoreUseCase,
    private val reissueUseCase: ReissueUseCase

): ViewModel() {

    /**
     * State 관련
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<StimulusPostDetailUiState>(StimulusPostDetailUiState.Loading)
    val uiState: StateFlow<StimulusPostDetailUiState> = _uiState

    /**
     * Data 관련
     */

    //게시물 ID 저장 변수
    private val _postId = MutableStateFlow("")
    val postId: StateFlow<String> = _postId

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth


    //게시물 정보
    private val _postDetail = MutableStateFlow<StimulusPost?>(null)
    val postDetail: StateFlow<StimulusPost?> = _postDetail

    //작성자 정보
    private val _writerInfo = MutableStateFlow<UserProfileBody?>(null)
    val writerInfo: StateFlow<UserProfileBody?> = _writerInfo


    //게시물 정보 받아왔는지 플래그
    val isGetPostDetail = MutableStateFlow(false)

    //작성자 정보 받아왔는지 플래그
    val isGetWriterInfo = MutableStateFlow(false)


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

    //게시물 ID 초기화
    fun initPostId(postId: String){

        _postId.value = postId

    }

    //게시물 정보 불러오기
    fun getPostDetail(){

        if(!isGetPostDetail.value){
            isGetPostDetail.value = true
            viewModelScope.launch {
                val result = getPostDetailUseCase.executeGetStimulusPostDetail(auth.value, postId.value)

                result
                    .onSuccess {
                        _postDetail.value = data.body

                        //작성자 정보 불러오기
                        getWriterInfo(postDetail.value?.writerId.toString())
                    }
                    .onError {

                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            isGetPostDetail.value = false
                            reIssueRefreshToken(callback = { getPostDetail() })

                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = StimulusPostDetailUiState.Error("오류가 발생했습니다.")

                    }
            }

        }

    }

    //해당 게시물 작성자 프로필 정보, 이미지, 내용 초기화
    private fun getWriterInfo(writerId: String) {
        if(!isGetWriterInfo.value){
            isGetWriterInfo.value = true
            viewModelScope.launch {
                val result = getUserProfileUseCase.executeGetUserProfile(auth.value, writerId)

                result
                    .onSuccess {
                        _writerInfo.value = data.body
                        _uiState.value = StimulusPostDetailUiState.Success("성공")
                    }
                    .onError {
                        _uiState.value = StimulusPostDetailUiState.Error("${this.response.code()} Error")
                    }
                    .onException {
                        _uiState.value = StimulusPostDetailUiState.Error(this.message())
                    }
            }
        }

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
                        _uiState.value = StimulusPostDetailUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = StimulusPostDetailUiState.Error("오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = StimulusPostDetailUiState.Error("오류가 발생했습니다.")

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