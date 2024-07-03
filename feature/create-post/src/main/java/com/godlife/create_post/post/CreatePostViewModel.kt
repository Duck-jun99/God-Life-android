package com.godlife.create_post.post

import android.net.Uri
import android.util.Log
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

    val title = mutableStateOf("")
    val text = mutableStateOf("")
    val tags = mutableListOf("tag1","tag2","tag3")


    fun saveImg(imgUri: Uri){
        _selectedImgUri.value += imgUri
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