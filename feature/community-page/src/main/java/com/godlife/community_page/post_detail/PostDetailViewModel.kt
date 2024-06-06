package com.godlife.community_page.post_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.DeleteCommentUseCase
import com.godlife.domain.GetCommentsUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.PlusGodScoreUseCase
import com.godlife.network.model.CommentBody
import com.godlife.network.model.PostDetailQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val plusGodScoreUseCase: PlusGodScoreUseCase

): ViewModel() {

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

    //게시물 상세 정보 초기화
    fun initPostDetail(postId: String) {

        _postId.value = postId

        var auth = ""

        viewModelScope.launch {
            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            //해당 게시물 작성자 프로필 정보, 이미지, 내용 초기화
            _postDetail.value = getPostDetailUseCase.executeGetPostDetail(auth, postId)

            //해당 게시물 댓글 초기화
            _comments.value = getCommentsUseCase.executeGetComments(auth, postId).body


        }
    }

    //작성 중인 댓글 변화
    fun onWriteCommentChange(text: String) {
        _writeComment.value = text
    }

    //댓글 작성 및 게시물 정보 다시 불러오기
    fun createComment(postId: String){

        var auth = ""

        viewModelScope.launch {

            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            //댓글 작성이 성공했다는 메시지를 받으면 댓글 정보 다시 불러오기
            if(createCommentUseCase.executeCreateComment(auth, postId, writeComment.value).body){

                //작성 중인 댓글 초기화
                _writeComment.value = ""

                //해당 게시물 댓글 초기화
                _comments.value = getCommentsUseCase.executeGetComments(auth, postId).body

            }
        }
    }

    //댓글 삭제
    fun deleteComment(commentId: String){
        var auth = ""
        viewModelScope.launch {

            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            //댓글 삭제가 성공했다는 메시지를 받으면 댓글 정보 다시 불러오기
            if(deleteCommentUseCase.executeDeleteComment(auth, commentId).body){

                //해당 게시물 댓글 초기화
                _comments.value = getCommentsUseCase.executeGetComments(auth, postId.value).body

            }
        }
    }

    //갓생 인정 버튼 클릭
    fun agreeGodLife(postId: String){

        val postId = postId.toInt()
        var auth = ""

        viewModelScope.launch {

            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            //갓생 인정이 성공했다는 메시지를 받으면 게시물 정보 다시 불러오기
            if(plusGodScoreUseCase.executePlusGodScore(auth, postId).body == "true"){

                initPostDetail(postId.toString())

            }

        }

    }



}