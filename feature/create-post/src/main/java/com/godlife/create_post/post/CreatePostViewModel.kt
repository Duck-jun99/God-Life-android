package com.godlife.create_post.post

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.database.model.TodoEntity
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalDatabaseUseCase
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow<CreatePostUiState>(CreatePostUiState.Loading)
    val uiState: StateFlow<CreatePostUiState> = _uiState

    private val _userInfo = MutableStateFlow<UserInfoBody?>(null)
    val userInfo: StateFlow<UserInfoBody?> = _userInfo

    //유저 정보 플래그
    private val _isGetUserInfo = mutableStateOf(false)

    //게시물 전송 플래그
    private val _isSending = mutableStateOf(false)

    private val _selectedImgUri = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUri: StateFlow<List<Uri>?> = _selectedImgUri

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _text = mutableStateOf("")
    val text: State<String> = _text

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> = _tags

    private val _todayTodo = MutableStateFlow<TodoEntity?>(null)
    val todayTodo: StateFlow<TodoEntity?> = _todayTodo

    //val tags = mutableListOf("tag1","tag2","tag3")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            //_tags.value = localDatabaseUseCase.getTodayTodoList().todoList.map{ it.name }
            launch {
                _todayTodo.value = localDatabaseUseCase.getTodayTodoList()
            }.join()

            launch {
                _tags.value = todayTodo.value!!.todoList.map{ it.name }
            }

        }
    }

    fun getUserInfo(){
        if(!_isGetUserInfo.value){
            Log.e("CreatePostViewModel", "유저 정보 불러오기")
            viewModelScope.launch(Dispatchers.IO) {
                val result = getUserInfoUseCase.executeGetUserInfo()
                result
                    .onSuccess {
                        _userInfo.value = data.body
                        _isGetUserInfo.value = true
                        _uiState.value = CreatePostUiState.Init
                    }
                    .onError {
                        _uiState.value = CreatePostUiState.Error("${this.response.code()} Error")
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

                    val result = createPostUseCase.executeCreatePost(title.value, text.value, tags.value, selectedImgUri.value)

                    result
                        .onSuccess {
                            _isSending.value = true
                            updateTodoCompleted()

                            //localDatabaseUseCase.updateTodoList(todayTodo.value!!.copy(isCompleted = true))
                        }

                        .onError {
                            _uiState.value = CreatePostUiState.Error("${this.response.code()} Error}")
                        }

                        .onException {
                            _uiState.value = CreatePostUiState.Error(this.message())
                        }

                }

            }

        }


    }

    private fun updateTodoCompleted(){
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                localDatabaseUseCase.updateTodoList(todayTodo.value!!.copy(isCompleted = true))
            }.join()

            launch {
                _uiState.value = CreatePostUiState.Success("오늘의 투두리스트 완료")
            }
        }
    }


}