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
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val plusGodScoreUseCase: PlusGodScoreUseCase,
    private val reissueUseCase: ReissueUseCase

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

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth


    //게시물 정보 가져왔는지 플래그
    private var isGetPostDetail = mutableIntStateOf(0)

    //게시물 댓글 가져왔는지 플래그
    private var isGetComments = mutableIntStateOf(0)

    //게시물 삭제 성공 플래그
    private var isDeletePost = mutableStateOf(false)


    //게시물 ID 초기화 및 게시물 정보 불러오기
    fun initPostDetailInfo(postId: String){

        viewModelScope.launch {

            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"

            _postId.value = postId

            Log.e("PostDetailViewModel", this.toString())

            initPostDetail()

        }


    }

    fun deletePost(){
        if(!isDeletePost.value){
            viewModelScope.launch {
                val result = deletePostUseCase.executeDeletePost(auth.value, postId.value)
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

        if(isGetPostDetail.value == 0){

            viewModelScope.launch{

                val result = getPostDetailUseCase.executeGetPostDetail(auth.value, postId.value)

                result
                    .onSuccess {
                        _postDetail.value = this.data

                        initComments()

                    }
                    .onError {

                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { initPostDetail() })

                        }
                        else {
                            // UI State Error로 변경
                            _uiState.value = PostDetailUiState.Error(this.message())
                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())

                    }


            }


            isGetPostDetail.value += 1
        }


    }

    //댓글 정보 초기화
    private fun initComments(){

        if(isGetComments.value == 0){

            viewModelScope.launch{

                val result = getCommentsUseCase.executeGetComments(auth.value, postId.value)

                result
                    .onSuccess {
                        _comments.value = this.data.body

                        _uiState.value = PostDetailUiState.Success("성공")
                    }
                    .onError {
                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { initComments() })

                        }
                        else{
                            // UI State Error로 변경
                            _uiState.value = PostDetailUiState.Error(this.message())
                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())

                    }
            }

            isGetComments.value +=1
        }

    }

    //작성 중인 댓글 변화
    fun onWriteCommentChange(text: String) {
        _writeComment.value = text
    }

    //댓글 작성 -> 성공 시 댓글 정보 다시 불러오기
    fun createComment(){

        viewModelScope.launch {
            val result = createCommentUseCase.executeCreateComment(auth.value, postId.value, writeComment.value)

            result
                .onSuccess {
                    //댓글 작성이 성공했다는 메시지를 받으면 댓글 정보 다시 불러오기 수행
                    if(data.body == true) {

                        //작성 중인 댓글 초기화
                        _writeComment.value = ""

                        //해당 게시물 댓글 초기화
                        isGetComments.value = 0
                        initComments()
                    }
                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 재발급 요청
                    if(this.response.code() == 401){

                        reIssueRefreshToken(callback = { createComment() })

                    }
                    else{
                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())
                    }
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

            val result = deleteCommentUseCase.executeDeleteComment(auth.value, commentId)

            result
                .onSuccess {
                    //댓글 삭제가 성공했다는 메시지를 받으면 댓글 정보 다시 불러오기
                    if(data.body == true){

                        //해당 게시물 댓글 초기화
                        isGetComments.value = 0
                        initComments()
                    }
                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 재발급 요청
                    if(this.response.code() == 401){

                        reIssueRefreshToken(callback = { deleteComment(commentId) })

                    }
                    else{
                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())
                    }
                }
                .onException {

                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error(this.message())
                }

        }
    }

    //갓생 인정 버튼 클릭
    fun agreeGodLife(){

        val postId = postId.value.toInt()

        viewModelScope.launch {

            val result = plusGodScoreUseCase.executePlusGodScore(auth.value, postId)

            result
                .onSuccess {
                    //갓생 인정이 성공했다는 메시지를 받으면 게시물 정보 다시 불러오기

                    if(data.body == "true"){
                        isGetPostDetail.value = 0
                        initPostDetail()
                    }
                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 재발급 요청
                    if(this.response.code() == 401){

                        reIssueRefreshToken(callback = { agreeGodLife() })

                    }
                    else{
                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())
                    }
                }
                .onException {

                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error(this.message())
                }

        }

    }


    // refresh token 갱신 후 Callback 실행
    private fun reIssueRefreshToken(callback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {

            var auth = ""
            launch { auth = "Bearer ${localPreferenceUserUseCase.getRefreshToken()}" }.join()

            val response = reissueUseCase.executeReissue(auth)

            response
                //성공적으로 넘어오면 유저 정보의 토큰을 갱신
                .onSuccess {

                    localPreferenceUserUseCase.saveAccessToken(data.body.accessToken)
                    localPreferenceUserUseCase.saveRefreshToken(data.body.refreshToken)

                    //callback 실행
                    callback()

                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 로컬에서 토큰 삭제하고 로그아웃 메시지
                    if(this.response.code() == 400){

                        deleteLocalToken()

                        // UI State Error로 변경 및 로그아웃 메시지
                        _uiState.value = PostDetailUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = PostDetailUiState.Error(this.message())
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = PostDetailUiState.Error(this.message())

                }


        }
    }

    // 로컬에서 토큰 및 사용자 정보 삭제
    private fun deleteLocalToken() {

        viewModelScope.launch(Dispatchers.IO) {

            // 로컬 데이터베이스에서 사용자 정보 삭제 후 완료되면 true 반환
            localPreferenceUserUseCase.removeAccessToken()
            localPreferenceUserUseCase.removeUserId()
            localPreferenceUserUseCase.removeRefreshToken()

        }

    }



}