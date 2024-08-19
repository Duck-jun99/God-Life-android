package com.godlife.community_page.post_detail

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.DeleteCommentUseCase
import com.godlife.domain.DeletePostUseCase
import com.godlife.domain.GetCommentsUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.PlusGodScoreUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.UpdatePostUseCase
import com.godlife.network.model.CommentBody
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.PostDetailQuery
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PostDetailUiState {

    //게시물 정보 불러오는 중 or API 통신 결과 기다리는 중
    data class Loading(val type: LoadingType) : PostDetailUiState()

    //게시물 정보 불러오기 성공 or API 통신 성공 (Delete 제외)
    data class Success(val data: String) : PostDetailUiState()

    //게시물 수정 상태
    object Update : PostDetailUiState()

    // 게시물 삭제 성공
    object DeleteSuccess: PostDetailUiState()

    //게시물 정보 불러오기 실패 or API 통신 실패
    data class Error(val message: String) : PostDetailUiState()
}

enum class LoadingType {
    POST, DELETE
}

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val plusGodScoreUseCase: PlusGodScoreUseCase,
): ViewModel() {

    /**
     * State 관련
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading(type = LoadingType.POST))
    val uiState: StateFlow<PostDetailUiState> = _uiState

    /**
     * Data 관련
     */

    private val _postDetail = MutableStateFlow<PostDetailQuery>(PostDetailQuery("", null, ""))
    val postDetail: StateFlow<PostDetailQuery> = _postDetail

    private val _comments = MutableStateFlow<List<CommentBody>>(emptyList())
    val comments: StateFlow<List<CommentBody>> = _comments

    //작성 중인 댓글
    private val _writeComment = MutableStateFlow("")
    val writeComment: StateFlow<String> = _writeComment

    //게시물 ID 저장 변수
    private val _postId = MutableStateFlow("")
    val postId: StateFlow<String> = _postId

    //게시물 정보 가져왔는지 플래그
    private var isGetPostDetail = mutableStateOf(false)

    //게시물 댓글 가져왔는지 플래그
    private var isGetComments = mutableStateOf(false)

    //게시물 삭제 성공 플래그
    private var isDeletePost = mutableStateOf(false)


    //게시물 ID 초기화 및 게시물 정보 불러오기
    fun initPostDetailInfo(postId: String){

        viewModelScope.launch {

            _postId.value = postId

            Log.e("PostDetailViewModel", this.toString())

            initPostDetail()

        }

    }

    fun deletePost(){
        if(!isDeletePost.value){
            viewModelScope.launch {
                val result = deletePostUseCase.executeDeletePost(postId.value)
                result
                    .onSuccess {
                        _uiState.value = PostDetailUiState.DeleteSuccess
                        isDeletePost.value = true
                    }
                    .onError {
                        _uiState.value = PostDetailUiState.Error(this.message())
                    }
                    .onException {
                        _uiState.value = PostDetailUiState.Error(this.message())
                    }
            }
        }
    }

    //해당 게시물 작성자 프로필 정보, 이미지, 내용 초기화 (성공 시 initComments 호출 -> initComments까지 성공적으로 되면 Ui State Success로 변경)
    private fun initPostDetail() {

        if(!isGetPostDetail.value){

            viewModelScope.launch{

                val result = getPostDetailUseCase.executeGetPostDetail(postId.value)

                result
                    .onSuccess {
                        _postDetail.value = this.data

                        initComments()

                    }
                    .onError {

                        Log.e("onError", this.message())

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error("${this.response.code()} Error")
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())

                    }


            }


            isGetPostDetail.value = true
        }


    }

    //댓글 정보 초기화
    private fun initComments(){

        if(!isGetComments.value){

            viewModelScope.launch{

                val result = getCommentsUseCase.executeGetComments(postId.value)

                result
                    .onSuccess {
                        _comments.value = this.data.body

                        _uiState.value = PostDetailUiState.Success("성공")
                    }
                    .onError {
                        Log.e("onError", this.message())

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error("${this.response.code()} Error")
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())

                    }
            }

            isGetComments.value = true
        }

    }

    //작성 중인 댓글 변화
    fun onWriteCommentChange(text: String) {
        _writeComment.value = text
    }

    //댓글 작성 -> 성공 시 댓글 정보 다시 불러오기
    fun createComment(){

        viewModelScope.launch {
            val result = createCommentUseCase.executeCreateComment(postId.value, writeComment.value)

            result
                .onSuccess {
                    //댓글 작성이 성공했다는 메시지를 받으면 댓글 정보 다시 불러오기 수행
                    if(data.body == true) {

                        //작성 중인 댓글 초기화
                        _writeComment.value = ""

                        //해당 게시물 댓글 초기화
                        isGetComments.value = false
                        initComments()
                    }
                }
                .onError {
                    Log.e("onError", this.message())

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error("${this.response.code()} Error")
                }
                .onException {

                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error(this.message())
                }

        }
    }

    //댓글 삭제
    fun deleteComment(commentId: String){

        viewModelScope.launch {

            val result = deleteCommentUseCase.executeDeleteComment(commentId)

            result
                .onSuccess {
                    //댓글 삭제가 성공했다는 메시지를 받으면 댓글 정보 다시 불러오기
                    if(data.body == true){

                        //해당 게시물 댓글 초기화
                        isGetComments.value = false
                        initComments()
                    }
                }
                .onError {
                    Log.e("onError", this.message())

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error("${this.response.code()} Error")
                }
                .onException {

                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error(this.message())
                }

        }
    }

    //굿생 인정 버튼 클릭
    fun agreeGodLife(){

        val postId = postId.value.toInt()

        viewModelScope.launch {

            val result = plusGodScoreUseCase.executePlusGodScore(postId)

            result
                .onSuccess {
                    //굿생 인정이 성공했다는 메시지를 받으면 게시물 정보 다시 불러오기

                    if(data.body == "true"){
                        isGetPostDetail.value = false
                        initPostDetail()
                    }
                }
                .onError {
                    Log.e("onError", this.message())

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error("${this.response.code()} Error")
                }
                .onException {

                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error(this.message())
                }

        }

    }

    fun updateUIState(uiState: PostDetailUiState){
        _uiState.value = uiState
    }

    //게시판 수정 후 다시 게시물 불러오기
    fun refreshPostDetail(){
        _uiState.value = PostDetailUiState.Loading(type = LoadingType.POST)
        isGetPostDetail.value = false
        isGetComments.value = false
        initPostDetail()
    }

    override fun onCleared() {
        Log.e("PostDetailViewModel", "onCleared")
        super.onCleared()
    }



}