package com.godlife.create_post.post

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.network.model.UserInfoBody
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

sealed class CreatePostUiState {
    object Loading : CreatePostUiState()

    object Init : CreatePostUiState()

    object SendLoading : CreatePostUiState()
    data class Success(val message: String) : CreatePostUiState()
    data class Error(val message: String) : CreatePostUiState()
}

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow<CreatePostUiState>(CreatePostUiState.Loading)
    val uiState: StateFlow<CreatePostUiState> = _uiState

    private val _userInfo = MutableStateFlow<UserInfoBody?>(null)
    val userInfo: StateFlow<UserInfoBody?> = _userInfo

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    //유저 정보 플래그
    private val _isGetUserInfo = mutableStateOf(false)
    //val isGetUserInfo: StateFlow<Boolean> = _isGetUserInfo

    //게시물 전송 플래그
    private val _isSending = mutableStateOf(false)

    private val _selectedImgUri = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUri: StateFlow<List<Uri>?> = _selectedImgUri

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _text = mutableStateOf("")
    val text: State<String> = _text

    val tags = mutableListOf("tag1","tag2","tag3")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }
    }

    fun getUserInfo(){
        if(!_isGetUserInfo.value){
            Log.e("CreatePostViewModel", "유저 정보 불러오기")
            viewModelScope.launch(Dispatchers.IO) {
                val result = getUserInfoUseCase.executeGetUserInfo(auth.value)
                result
                    .onSuccess {
                        _userInfo.value = data.body
                        _isGetUserInfo.value = true
                        _uiState.value = CreatePostUiState.Init
                    }
                    .onError {
                        _uiState.value = CreatePostUiState.Error(this.message())
                    }
                    .onException {
                        _uiState.value = CreatePostUiState.Error(this.message())
                    }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle.take(30)
    }

    fun updateText(newText: String) {
        _text.value = newText.take(1000)
    }


    fun saveImg(imgUri: Uri){
        _selectedImgUri.value += imgUri
        Log.e("CreatePostViewModel", selectedImgUri.value.toString())
    }

    fun removeImg(imgUri: Uri){
        _selectedImgUri.value -= imgUri
        Log.e("CreatePostViewModel", selectedImgUri.value.toString())
    }


    fun createPost(){

        if(title.value != "" && text.value != ""){

            if(!_isSending.value){

                _uiState.value = CreatePostUiState.SendLoading

                viewModelScope.launch(Dispatchers.IO) {

                    val result = createPostUseCase.executeCreatePost(auth.value, title.value, text.value, tags, selectedImgUri.value)

                    result
                        .onSuccess {
                            _uiState.value = CreatePostUiState.Success(data.message)
                            _isSending.value = true
                        }

                        .onError {
                            _uiState.value = CreatePostUiState.Error(this.message())
                        }

                        .onException {
                            _uiState.value = CreatePostUiState.Error(this.message())
                        }

                }

            }

        }


    }


}