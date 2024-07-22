package com.godlife.create_post.stimulus

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
    data class Loading(val type: UiType): CreateStimulusUiState()
    data class Success(val type: UiType) : CreateStimulusUiState()
    object SendSuccess : CreateStimulusUiState()
    data class Error(val message: String) : CreateStimulusUiState()
}

enum class UiType {
    GET_BOARD_ID,
    SET_COVER_IMG,
    CREATE_POST
}

@HiltViewModel
class CreateStimulusPostViewModel @Inject constructor(
    private val createStimulusPostUseCase: CreateStimulusPostUseCase
): ViewModel() {

    /**
     * State
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<CreateStimulusUiState>(CreateStimulusUiState.Loading(UiType.GET_BOARD_ID))
    val uiState: StateFlow<CreateStimulusUiState> = _uiState

    /**
     * Data
     */

    //boardId
    private val _boardId = MutableStateFlow<Int>(0)
    val boardId: StateFlow<Int> = _boardId

    //임시 boardId를 호출한 적이 있는지 플래그
    private var isGetBoardId = mutableStateOf(false)

    //게시물 전송 플래그
    private var isCreatePost = mutableStateOf(false)

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
     * Functions
     */

    suspend fun setCoverImg(uri: Uri) {

        _uiState.value = CreateStimulusUiState.Loading(UiType.SET_COVER_IMG)

        createStimulusPostUseCase.executeUploadStimulusPostImage(
            tmpBoardId = boardId.value,
            image = uri
        )
            .onSuccess {
                _coverImg.value = data.body
                _uiState.value = CreateStimulusUiState.Success(UiType.SET_COVER_IMG)
                Log.e("CreateStimulusPostViewModel", "setCoverImg: ${coverImg.value}")
            }
            .onError {
                Log.e("setCoverImg", this.message())

                if(this.response.code() == 400){

                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("재로그인 해주세요.")
                }
                else{
                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("${this.response.code()} Error")
                }

            }
            .onException {

                Log.e("setCoverImg", "${this.message}")

                // UI State Error로 변경
                _uiState.value = CreateStimulusUiState.Error(this.message())
            }

        //_coverImg.value = uri
        Log.e("CreateStimulusPostViewModel", "setCoverImg: ${coverImg.value}")
    }

    fun setTitle(title: String) {
        _title.value = title.take(15)
    }

    fun setDescription(description: String) {
        _description.value = description.take(30)
    }

    fun setContent(content: String){
        _content.value = content
    }

    fun getTempBoardId(){

        if(!isGetBoardId.value){
            isGetBoardId.value = true
            _uiState.value = CreateStimulusUiState.Loading(UiType.GET_BOARD_ID)

            viewModelScope.launch {
                createStimulusPostUseCase.executeCreateStimulusPostTemp()
                    .onSuccess {
                        _boardId.value = data.body.toInt()

                        _uiState.value = CreateStimulusUiState.Success(UiType.GET_BOARD_ID)
                    }
                    .onError {
                        Log.e("getTempBoardId", this.message())

                        // 토큰 만료시
                        if(this.response.code() == 400){
                            _uiState.value = CreateStimulusUiState.Error("재로그인 해주세요.")
                        }
                        else{
                            // UI State Error로 변경
                            _uiState.value = CreateStimulusUiState.Error("${this.response.code()} Error")
                        }
                    }
                    .onException {

                        Log.e("getTempBoardId", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CreateStimulusUiState.Error(this.message())
                    }
            }

        }

    }

    suspend fun uploadImage(image: Uri): String{

        var imgUrl = ""

        createStimulusPostUseCase.executeUploadStimulusPostImage(
            tmpBoardId = boardId.value,
            image = image
        )
            .onSuccess {
                imgUrl = BuildConfig.SERVER_IMAGE_DOMAIN + data.body
            }
            .onError {
                Log.e("uploadImage", this.message())

                if(this.response.code() == 400){

                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")
                }
                else{
                    // UI State Error로 변경
                    _uiState.value = CreateStimulusUiState.Error("${this.response.code()} Error")
                }

            }
            .onException {

                Log.e("uploadImage", "${this.message}")

                // UI State Error로 변경
                _uiState.value = CreateStimulusUiState.Error(this.message())
            }

        return imgUrl
    }

    fun completeCreateStimulusPost(){

        if(!isCreatePost.value){
            isCreatePost.value = true
            _uiState.value = CreateStimulusUiState.Loading(UiType.CREATE_POST)

            viewModelScope.launch {

                createStimulusPostUseCase.executeCreateStimulusPost(
                    CreatePostRequest(
                        boardId = boardId.value.toLong(),
                        title = title.value,
                        content = content.value,
                        thumbnailUrl = coverImg.value,
                        introduction = description.value
                    )
                )
                    .onSuccess {

                        _uiState.value = CreateStimulusUiState.SendSuccess
                        Log.e("completeCreateStimulusPost", "completeCreateStimulusPost: $data")

                    }
                    .onError {
                        Log.e("completeCreateStimulusPost", this.message())

                        // 토큰 만료시
                        if(this.response.code() == 400){
                            _uiState.value = CreateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")

                        }
                        else{
                            // UI State Error로 변경
                            _uiState.value = CreateStimulusUiState.Error("${this.response.code()} Error")
                        }

                    }
                    .onException {

                        Log.e("completeCreateStimulusPost", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = CreateStimulusUiState.Error(this.message())
                    }

            }



        }

    }




}