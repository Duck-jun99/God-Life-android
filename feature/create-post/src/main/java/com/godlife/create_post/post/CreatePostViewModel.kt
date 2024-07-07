package com.godlife.create_post.post

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase
): ViewModel(){

    private val _selectedImgUri = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUri: StateFlow<List<Uri>?> = _selectedImgUri

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _text = mutableStateOf("")
    val text: State<String> = _text

    val tags = mutableListOf("tag1","tag2","tag3")

    fun updateTitle(newTitle: String) {
        _title.value = newTitle.take(20)
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
        lateinit var authorizationHeader:String

        viewModelScope.launch(Dispatchers.IO) {

            launch {
                authorizationHeader = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
            }.join()

            launch {
                createPostUseCase.executeCreatePost(authorizationHeader, title.value, text.value, tags, selectedImgUri.value)
            }

        }

    }


}