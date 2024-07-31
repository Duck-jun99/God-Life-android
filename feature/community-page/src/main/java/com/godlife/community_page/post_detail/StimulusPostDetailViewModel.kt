package com.godlife.community_page.post_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.DeleteCommentUseCase
import com.godlife.domain.DeleteStimulusPostUseCase
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
    data class Loading(val type: UiType) : StimulusPostDetailUiState()
    data class Success(val type: UiType) : StimulusPostDetailUiState()
    object Delete: StimulusPostDetailUiState()
    data class Error(val message: String) : StimulusPostDetailUiState()
}

enum class UiType{
    LOAD_POST,
    DELETE
}

@HiltViewModel
class StimulusPostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val deleteStimulusPostUseCase: DeleteStimulusPostUseCase

): ViewModel() {

    /**
     * State 관련
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<StimulusPostDetailUiState>(StimulusPostDetailUiState.Loading(UiType.LOAD_POST))
    val uiState: StateFlow<StimulusPostDetailUiState> = _uiState

    /**
     * Data 관련
     */

    //게시물 ID 저장 변수
    private val _postId = MutableStateFlow("")
    val postId: StateFlow<String> = _postId

    //게시물 정보
    private val _postDetail = MutableStateFlow<StimulusPost?>(null)
    val postDetail: StateFlow<StimulusPost?> = _postDetail

    //작성자 정보
    private val _writerInfo = MutableStateFlow<UserProfileBody?>(null)
    val writerInfo: StateFlow<UserProfileBody?> = _writerInfo

    //삭제 Dialog 보여줄 플래그
    val isDialogVisble = MutableStateFlow(false)

    //게시물 정보 받아왔는지 플래그
    private val isGetPostDetail = MutableStateFlow(false)

    //작성자 정보 받아왔는지 플래그
    private val isGetWriterInfo = MutableStateFlow(false)

    //삭제 완료 플래그
    private val isDeleted = MutableStateFlow(false)


    /**
     * Init
     */


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
                val result = getPostDetailUseCase.executeGetStimulusPostDetail(postId.value)

                result
                    .onSuccess {
                        _postDetail.value = data.body

                        //작성자 정보 불러오기
                        getWriterInfo(postDetail.value?.writerId.toString())
                    }
                    .onError {

                        Log.e("onError", this.message())

                        // UI State Error로 변경
                        _uiState.value = StimulusPostDetailUiState.Error("${this.response.code()} Error")

                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = StimulusPostDetailUiState.Error(this.message())

                    }
            }

        }

    }

    //해당 게시물 작성자 프로필 정보, 이미지, 내용 초기화
    private fun getWriterInfo(writerId: String) {
        if(!isGetWriterInfo.value){
            isGetWriterInfo.value = true
            viewModelScope.launch {
                val result = getUserProfileUseCase.executeGetUserProfile(writerId)

                result
                    .onSuccess {
                        _writerInfo.value = data.body
                        _uiState.value = StimulusPostDetailUiState.Success(UiType.LOAD_POST)
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

    //삭제 버튼 Dialog flag 변경 함수
    fun setDialogVisble(){
        isDialogVisble.value = !isDialogVisble.value
    }

    //삭제 함수 (작성자인 경우)
    fun deletePost(){
        if(!isDeleted.value){
            isDeleted.value = true

            _uiState.value = StimulusPostDetailUiState.Loading(UiType.DELETE)

            viewModelScope.launch {
                deleteStimulusPostUseCase.executeDeleteStimulusPost(boardId  = postId.value)
                    .onSuccess {
                        _uiState.value = StimulusPostDetailUiState.Delete
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


}