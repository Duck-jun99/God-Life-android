package com.godlife.community_page.post_detail.post_update.stimulus

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.UpdateStimulusPostUseCase
import com.godlife.network.BuildConfig
import com.godlife.network.model.CreatePostRequest
import com.godlife.network.model.StimulusPost
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.inject.Inject

sealed class UpdateStimulusUiState {
    data class Loading(val type: UiType): UpdateStimulusUiState()
    data class Success(val type: UiType) : UpdateStimulusUiState()
    object SendSuccess : UpdateStimulusUiState()
    data class Error(val message: String) : UpdateStimulusUiState()
}

enum class UiType {
    GET_PREVIOUS_POST,
    SET_COVER_IMG,
    UPDATE_POST
}

@HiltViewModel
class UpdateStimulusPostViewModel @Inject constructor(
    private val updateStimulusPostUseCase: UpdateStimulusPostUseCase,
    private val getStimulusPostDetailUseCase: GetPostDetailUseCase
): ViewModel() {

    /**
     * State
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<UpdateStimulusUiState>(
        UpdateStimulusUiState.Loading(
            UiType.GET_PREVIOUS_POST
        )
    )
    val uiState: StateFlow<UpdateStimulusUiState> = _uiState

    /**
     * Data
     */



    //기존 게시물 정보
    private val _postDetail = MutableStateFlow<StimulusPost?>(null)
    val postDetail: StateFlow<StimulusPost?> = _postDetail

    //기존 게시물을 호출한 적이 있는지 플래그
    private var isGetBoardId = mutableStateOf(false)

    // 커버 이미지 불러오기 플래그
    private var isGetCoverImg = mutableStateOf(false)

    //게시물 내 이미지 전송 플래그
    private var isSetImg = mutableStateOf(false)

    // 커버 이미지 업데이트 플래그
    private var isUpdateCoverImg = mutableStateOf(false)

    //게시물 업데이트 플래그
    private var isUpdatePost = mutableStateOf(false)

    //boardId
    private val _boardId = MutableStateFlow<Int>(0)
    val boardId: StateFlow<Int> = _boardId

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

    /**
     * Functions
     */

    fun getStimulusPost(
        postId: String,
        context: Context
    ){
        if(!isGetBoardId.value){
            isGetBoardId.value = true

            viewModelScope.launch {
                getStimulusPostDetailUseCase.executeGetStimulusPostDetail(postId)

                    .onSuccess {
                        _postDetail.value = data.body
                        _boardId.value = data.body.boardId
                        //_coverImg.value = data.body.thumbnailUrl
                        _title.value = data.body.title
                        _description.value = data.body.introduction
                        _content.value = data.body.content

                        setCoverImg(
                            data.body.thumbnailUrl.toUri(),
                            context,
                            true
                        )

                        _uiState.value = UpdateStimulusUiState.Success(UiType.GET_PREVIOUS_POST)
                    }
                    .onError {
                        _uiState.value =
                            UpdateStimulusUiState.Error("${this.response.code()} Error")
                    }
                    .onException {
                        _uiState.value = UpdateStimulusUiState.Error(this.message())
                    }
            }
        }
    }

    fun setCoverImg(
        uri: Uri,
        context: Context,
        init : Boolean
    ) {

        if(init){

            if(!isGetCoverImg.value){
                isGetCoverImg.value = true

                viewModelScope.launch(Dispatchers.IO) {
                    val cachedImg = downloadAndCacheImage(BuildConfig.SERVER_IMAGE_DOMAIN + uri, context)
                    if (cachedImg != null) {
                        _coverImg.value = cachedImg
                    }
                }
            }

        }
        else{
            _coverImg.value = uri
        }


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
            Log.e("UpdateStimulusPostViewModel", "Error caching image: ${e.message}")
            null
        }
    }

    fun insertImageToWebView(
        webView: WebView,
        imageUri: Uri,
        flag: Boolean
    ){

        /*
        서버에 이미지를 업로드 후 이미지 URL을 받아오는 코드 부분
        */

        isSetImg.value = flag

        if(!isSetImg.value){
            isSetImg.value = true

            viewModelScope.launch {

                val imgUrl = uploadImage(imageUri)

                webView.evaluateJavascript("javascript:insertImage('$imgUrl');") { result ->
                    Log.e("WebView", "JavaScript execution result: $result")
                }
            }
        }

    }

    private suspend fun uploadImage(image: Uri): String{

        var imgUrl = ""

        updateStimulusPostUseCase.executeUploadStimulusPostImage(
            boardId = boardId.value,
            image = image
        )
            .onSuccess {
                imgUrl = BuildConfig.SERVER_IMAGE_DOMAIN + data.body
            }
            .onError {
                Log.e("uploadImage", this.message())

                if(this.response.code() == 401){

                    // UI State Error로 변경
                    _uiState.value = UpdateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")
                }
                else{
                    // UI State Error로 변경
                    _uiState.value = UpdateStimulusUiState.Error("${this.response.code()} Error")
                }

            }
            .onException {

                Log.e("uploadImage", "${this.message}")

                // UI State Error로 변경
                _uiState.value = UpdateStimulusUiState.Error(this.message())
            }

        return imgUrl
    }

    fun convertResizeImage(
        imageUri: Uri,
        context: Context
    ):Uri? {

        val bitmap: Bitmap

        if (Build.VERSION.SDK_INT >= 29) {

            val source: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, imageUri!!)

            try {
                bitmap = ImageDecoder.decodeBitmap(source)

                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

                val byteArrayOutputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

                val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)


                val fileOutputStream = FileOutputStream(tempFile)
                fileOutputStream.write(byteArrayOutputStream.toByteArray())
                fileOutputStream.close()


                return Uri.fromFile(tempFile)
                //return contentUri

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                //val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

                val byteArrayOutputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

                val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
                val fileOutputStream = FileOutputStream(tempFile)
                fileOutputStream.write(byteArrayOutputStream.toByteArray())
                fileOutputStream.close()

                return Uri.fromFile(tempFile)
                //return contentUri

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return null
    }

    fun getHtmlFromWebView(
        webView: WebView
    ) {
        webView.evaluateJavascript("javascript:getHtmlToAndroid();") { html ->
            val decodedHtml = decodeJavaScriptString(html)
            setContent(content = decodedHtml)
            Log.e("WebViewHTML", decodedHtml)
        }
    }

    fun setHtmlToWebView(
        webView: WebView
    ){
        val html = content.value
        webView.evaluateJavascript("javascript:setHtml('$html');"){result ->
            Log.e("setHtmlToWebView", result)
        }
    }

    // JavaScript 문자열 디코딩 함수
    private fun decodeJavaScriptString(
        encodedString: String)
    : String {
        return encodedString
            .replace("\\u003C", "<")
            .replace("\\u003E", ">")
            .replace("\\u0026", "&")
            .replace("\\\"", "\"")
            .replace("\\\'", "'")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
            .removeSurrounding("\"")  // 문자열 앞뒤의 따옴표 제거
    }

    fun updateCoverImg(){
        if(!isUpdateCoverImg.value){
            isUpdateCoverImg.value = true

            _uiState.value = UpdateStimulusUiState.Loading(UiType.SET_COVER_IMG)

            viewModelScope.launch {
                updateStimulusPostUseCase.executeUploadStimulusPostImage(
                    boardId = boardId.value,
                    image = coverImg.value
                )
                    .onSuccess {
                        _coverImg.value = data.body.toUri()

                        completeUpdateStimulusPost()
                    }
                    .onError {
                        // 토큰 만료시
                        if(this.response.code() == 401){
                            _uiState.value = UpdateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")

                        }
                        else{
                            // UI State Error로 변경
                            _uiState.value = UpdateStimulusUiState.Error("${this.response.code()} Error")
                        }
                    }
                    .onException {
                        Log.e("completeUpdateStimulusPost", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = UpdateStimulusUiState.Error(this.message())
                    }
            }
        }
    }

    private fun completeUpdateStimulusPost(){
        if(!isUpdatePost.value){
            isUpdatePost.value = true

            _uiState.value = UpdateStimulusUiState.Loading(UiType.UPDATE_POST)

            viewModelScope.launch {
                updateStimulusPostUseCase.executeUpdateStimulusPost(
                    CreatePostRequest(
                        boardId = boardId.value.toLong(),
                        title = title.value,
                        content = content.value,
                        thumbnailUrl = coverImg.value.toString(),
                        introduction = description.value
                    )
                )
                    .onSuccess {

                        _uiState.value = UpdateStimulusUiState.SendSuccess
                        Log.e("completeUpdateStimulusPost", "completeCreateStimulusPost: $data")

                    }
                    .onError {
                        Log.e("completeUpdateStimulusPost", this.message())

                        // 토큰 만료시
                        if(this.response.code() == 401){
                            _uiState.value = UpdateStimulusUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")

                        }
                        else{
                            // UI State Error로 변경
                            _uiState.value = UpdateStimulusUiState.Error("${this.response.code()} Error")
                        }

                    }
                    .onException {

                        Log.e("completeUpdateStimulusPost", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = UpdateStimulusUiState.Error(this.message())
                    }
            }
        }
    }

    override fun onCleared() {
        Log.e("UpdateStimulusPostViewModel", "onCleared()")
        super.onCleared()
    }

}