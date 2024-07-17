package com.godlife.create_post.stimulus

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.create_post.BuildConfig
import com.godlife.domain.CreateStimulusPostUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.CreatePostRequest
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

sealed class CreateStimulusUiState {
    object Loading: CreateStimulusUiState()
    data class Success(val data: String) : CreateStimulusUiState()
    data class Error(val message: String) : CreateStimulusUiState()
}

@HiltViewModel
class CreateStimulusPostViewModel @Inject constructor(
    private val createStimulusPostUseCase: CreateStimulusPostUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val reissueUseCase: ReissueUseCase
): ViewModel() {

    /**
     * State
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<CreateStimulusUiState>(CreateStimulusUiState.Loading)
    val uiState: StateFlow<CreateStimulusUiState> = _uiState

    /**
     * Data
     */

    //auth
    private val _auth = MutableStateFlow<String>("")
    val auth: StateFlow<String> = _auth

    //boardId
    private val _boardId = MutableStateFlow<Int>(0)
    val boardId: StateFlow<Int> = _boardId

    //boardId를 호출한 적이 있는지 플래그
    private var boardIdFlag = mutableIntStateOf(0)

    //커버 이미지
    private val _coverImg = MutableStateFlow<String>("")
    val coverImg: StateFlow<String> = _coverImg

    //제목
    private val _title = MutableStateFlow<String>("")
    val title: StateFlow<String> = _title

    //소개 내용
    private val _description = MutableStateFlow<String>("")
    val description: StateFlow<String> = _description

    //내용 (String형의 HTML)
    private val _content = MutableStateFlow<String>("")
    val content: StateFlow<String> = _content

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

    suspend fun setCoverImg(uri: Uri) {

        createStimulusPostUseCase.executeUploadStimulusPostImage(
            authorization = auth.value,
            tmpBoardId = boardId.value,
            image = uri
        )
            .onSuccess {

                _coverImg.value = data.body
                Log.e("CreateStimulusPostViewModel", "setCoverImg: ${coverImg.value}")
            }
            .onError {
                Log.e("onError", this.message())

                // 토큰 만료시 재발급 요청
                if(this.response.code() == 401){

                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")
                }

            }
            .onException {

                Log.e("onException", "${this.message}")

                // UI State Error로 변경
                _uiState.value = CreateStimulusUiState.Error("오류가 발생했습니다.")
            }

        //_coverImg.value = uri
        Log.e("CreateStimulusPostViewModel", "setCoverImg: ${coverImg.value}")
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setContent(content: String){
        _content.value = content
    }

    fun getTempBoardId(){

        if(boardIdFlag.value == 0){

            viewModelScope.launch {
                createStimulusPostUseCase.executeCreateStimulusPostTemp(
                    authorization = auth.value
                )

                    .onSuccess {
                        _boardId.value = data.body.toInt()

                        _uiState.value = CreateStimulusUiState.Success("임시 게시믈 번호 받아오기 성공")
                    }
                    .onError {
                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { getTempBoardId() })

                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CreateStimulusUiState.Error("오류가 발생했습니다.")
                    }
            }

            boardIdFlag.value += 1

        }

    }

    suspend fun uploadImage(image: Uri): String{

        var imgUrl = ""

        createStimulusPostUseCase.executeUploadStimulusPostImage(
            authorization = auth.value,
            tmpBoardId = boardId.value,
            image = image
        )
            .onSuccess {
                imgUrl = BuildConfig.SERVER_IMAGE_DOMAIN + data.body
            }
            .onError {
                Log.e("onError", this.message())

                // 토큰 만료시 재발급 요청
                if(this.response.code() == 401){

                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")
                }

            }
            .onException {

                Log.e("onException", "${this.message}")

                // UI State Error로 변경
                _uiState.value = CreateStimulusUiState.Error("오류가 발생했습니다.")
            }

        return imgUrl
    }

    suspend fun completeCreateStimulusPost(){
        createStimulusPostUseCase.executeCreateStimulusPost(
            authorization = auth.value,
            CreatePostRequest(
                boardId = boardId.value.toLong(),
                title = title.value,
                content = content.value,
                thumbnailUrl = coverImg.value,
                introduction = description.value
            )
        )
            .onSuccess {

                Log.e("CreateStimulusPostViewModel", "completeCreateStimulusPost: $data")

            }
            .onError {
                Log.e("onError", this.message())

                // 토큰 만료시 재발급 요청
                if(this.response.code() == 401){

                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")
                }
                else{
                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("${this.response.code()} Error")
                }

            }
            .onException {

                Log.e("onException", "${this.message}")

                // UI State Error로 변경
                _uiState.value = CreateStimulusUiState.Error(this.message())
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
                        _uiState.value = CreateStimulusUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = CreateStimulusUiState.Error("오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("오류가 발생했습니다.")

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