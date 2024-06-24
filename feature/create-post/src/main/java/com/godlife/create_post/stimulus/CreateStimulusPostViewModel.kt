package com.godlife.create_post.stimulus

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateStimulusPostViewModel @Inject constructor(

): ViewModel() {


    //커버 이미지
    private val _coverImg = MutableStateFlow<Uri>(Uri.EMPTY)
    val coverImg: StateFlow<Uri> = _coverImg

    //제목
    private val _title = MutableStateFlow<String>("")
    val title: StateFlow<String> = _title

    //소개 내용
    private val _description = MutableStateFlow<String>("")
    val description: StateFlow<String> = _description

    //내용 (String형의 HTML)
    private val _content = MutableStateFlow<String>("")
    val content: StateFlow<String> = _content


    fun setCoverImg(uri: Uri) {
        _coverImg.value = uri
        Log.e("CreateStimulusPostViewModel", "setCoverImg: ${coverImg.value}")
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setDescription(description: String) {
        _description.value = description
    }


}