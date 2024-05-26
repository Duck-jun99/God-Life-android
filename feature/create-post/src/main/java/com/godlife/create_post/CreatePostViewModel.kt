package com.godlife.create_post

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CreatePostViewModel @Inject constructor(

): ViewModel(){

    private val _selectedImgUri = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUri: StateFlow<List<Uri>> = _selectedImgUri

    val title = mutableStateOf("")
    val text = mutableStateOf("")


    fun saveImg(imgUri: Uri){
        _selectedImgUri.value += imgUri
    }

}