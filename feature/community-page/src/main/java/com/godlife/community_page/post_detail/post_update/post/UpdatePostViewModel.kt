package com.godlife.community_page.post_detail.post_update.post

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.UpdatePostUseCase
import com.godlife.network.BuildConfig
import com.godlife.network.model.PostDetailBody
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import javax.inject.Inject

sealed class UpdatePostUiState {
    object Loading : UpdatePostUiState()

    object Init : UpdatePostUiState()

    object SendLoading : UpdatePostUiState()
    data class Success(val message: String) : UpdatePostUiState()
    data class Error(val message: String) : UpdatePostUiState()
}

@HiltViewModel
class UpdatePostViewModel @Inject constructor(
    private val updatePostUseCase: UpdatePostUseCase,

): ViewModel(){

    private val _uiState = MutableStateFlow<UpdatePostUiState>(UpdatePostUiState.Loading)
    val uiState: StateFlow<UpdatePostUiState> = _uiState

    //기존 게시물 담을 변수
    private val _oldPost = MutableStateFlow<PostDetailBody?>(null)
    val oldPost: StateFlow<PostDetailBody?> = _oldPost

    // 초기화 플래그
    private val _isInit = mutableStateOf(false)

    //게시물 전송 플래그
    private val _isSending = mutableStateOf(false)

    private val _selectedImgUri = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUri: StateFlow<List<Uri>?> = _selectedImgUri

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _text = mutableStateOf("")
    val text: State<String> = _text

    val tags = mutableListOf("tag1","tag2","tag3")

    fun init(postDetail: PostDetailBody, context: Context) {
        if (!_isInit.value) {
            _oldPost.value = postDetail
            _title.value = postDetail.title
            _text.value = postDetail.body

            viewModelScope.launch {

                //기존 이미지를 다운로드하여 캐시에 저장
                if (!postDetail.imagesURL.isNullOrEmpty()) {
                    val cachedImageUris = postDetail.imagesURL!!.mapNotNull { imageUrl ->
                        downloadAndCacheImage(BuildConfig.SERVER_IMAGE_DOMAIN + imageUrl, context)
                    }
                    _selectedImgUri.value = cachedImageUris
                }
            }

            _uiState.value = UpdatePostUiState.Init
            _isInit.value = true
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
        Log.e("UpdatePostViewModel", selectedImgUri.value.toString())
    }

    fun removeImg(imgUri: Uri){
        _selectedImgUri.value -= imgUri
        Log.e("UpdatePostViewModel", selectedImgUri.value.toString())
    }

    fun updatePost(){
        if(!_isSending.value){
            _uiState.value = UpdatePostUiState.SendLoading
            viewModelScope.launch {
                val result = updatePostUseCase.executeUpdatePost(
                    postId = oldPost.value!!.board_id.toString(),
                    title = title.value,
                    content = text.value,
                    tags = tags,
                    categoryType = null,
                    imagePath = selectedImgUri.value
                )

                result
                    .onSuccess {
                        _uiState.value = UpdatePostUiState.Success(data.message)

                    }
                    .onError {
                        _uiState.value = UpdatePostUiState.Error("${this.response.code()} Error")
                    }
                    .onException {
                        _uiState.value = UpdatePostUiState.Error(this.message())
                    }
            }

            _isSending.value = true
        }
    }

    private suspend fun downloadAndCacheImage(
        imageUrl: String,
        context: Context
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()

            val fileName = imageUrl.substringAfterLast("/")
            val cacheDir = File(context.cacheDir, "images")
            cacheDir.mkdirs()
            val file = File.createTempFile("resized_image", ".jpg", cacheDir)
            //val file = File(cacheDir, fileName)

            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("UpdatePostViewModel", "Error caching image: ${e.message}")
            null
        }
    }

    fun onMove(from: Int, to: Int) {
        _selectedImgUri.value = _selectedImgUri.value.toMutableList().apply {
            add(to, removeAt(from))
        }
    }

    override fun onCleared() {
        Log.e("UpdatePostViewModel", "onCleared")
        super.onCleared()
    }

}