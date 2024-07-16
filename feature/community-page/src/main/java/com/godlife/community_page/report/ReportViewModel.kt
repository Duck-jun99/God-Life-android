package com.godlife.community_page.report

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.ReportUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

sealed class ReportUiState{

    // 신고 정보 불러오는 중, API 전송 중
    object Loading: ReportUiState()

    // 신고 정보 불러온 상태
    data class Success(val data: String): ReportUiState()

    // 신고 접수 완료
    object SendSuccess: ReportUiState()

    // 신고 정보 불러오기 또는 신고 접수 실패
    data class Error(val error: String): ReportUiState()
}

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val reportUseCase: ReportUseCase,
    private val reissueUseCase: ReissueUseCase
): ViewModel(){

    /**
     * State 관련
     */

    //ui state
    private val _uiState = MutableStateFlow<ReportUiState>(ReportUiState.Loading)
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    /**
     * Data 관련
     */
    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    // 신고자 닉네임
    private val _reporterNickname = MutableStateFlow("")
    val reporterNickname: StateFlow<String> = _reporterNickname

    // 신고자 아이디
    private val _reporterId = MutableStateFlow(0)
    val reporterId: StateFlow<Int> = _reporterId

    // 신고받은 사람 닉네임
    private val _reportedNickname = MutableStateFlow("")
    val reportedNickname: StateFlow<String> = _reportedNickname

    // 신고받은 사람 아이디
    private val _reportedId = MutableStateFlow(0)
    val reportedId: StateFlow<Int> = _reportedId

    //신고 사유
    private val _reportReason = MutableStateFlow("신고 사유를 선택해주세요.")
    val reportReason: StateFlow<String> = _reportReason

    // 신고내용
    private val _reportContent = mutableStateOf("")
    val reportContent: State<String> = _reportContent

    // 게시물인지 댓글인지
    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category

    // 게시물 제목 or 댓글 내용
    private val _articleTitle = MutableStateFlow("")
    val articleTitle: StateFlow<String> = _articleTitle

    // 게시물(댓글)아이디
    private val _articleId = MutableStateFlow(0)
    val articleId: StateFlow<Int> = _articleId

    // 신고자(해당 신고를 이용하는 유저)의 정보를 가져왔는지 플래그
    private val isGetUserInfo = mutableStateOf(false)

    // 게시물(또는 댓글)을 가져왔는지 플래그
    private val isGetArticle = mutableStateOf(false)

    // 신고 접수 플래그
    private val isSendReport = mutableStateOf(false)

    // 초기화 플래그
    private val isInit = mutableStateOf(false)

    /*
    * Init
     */

    init {
        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"
        }
    }

    /*
    * Function
     */

    // 신고 관련 데이터 초기화
    fun init(
        postId:String,
        writerNickname: String,
        writerId:String,
        categoryType:String,
    ){

        if(!isInit.value){

            _category.value = categoryType
            _articleId.value = postId.toInt()
            _reportedNickname.value = writerNickname
            _reportedId.value = writerId.toInt()

            Log.e("ReportViewModel", "category : ${category.value}, postId : ${articleId.value}, writerNickname : ${reportedNickname.value}, writerId : ${reportedId.value}")

            if(category.value == "board"){
                getPost()

            }
            else if (category.value == "comment"){
                getUserInfo()
            }

            isInit.value = true

        }



    }

    private fun getPost(){

        Log.e("ReportViewModel", "getPost")

        if(!isGetArticle.value){
            viewModelScope.launch {

                val result = getPostDetailUseCase.executeGetPostDetail(auth.value,
                    articleId.value.toString()
                )
                result
                    .onSuccess {
                        _articleTitle.value = data.body!!.title
                        isGetArticle.value = true

                        getUserInfo()
                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { getPost()})

                        }
                        else{
                            _uiState.value = ReportUiState.Error(this.response.message())
                        }

                    }
                    .onException {
                        _uiState.value = ReportUiState.Error(this.message())
                    }
            }
        }
    }

    // 신고자(해당 신고를 이용하는 유저)의 정보 가져오기
    private fun getUserInfo(){
        if(!isGetUserInfo.value){
            viewModelScope.launch {
                val result = getUserInfoUseCase.executeGetUserInfo(auth.value)

                result
                    .onSuccess {
                        _reporterNickname.value = data.body.nickname
                        _reporterId.value = data.body.memberId

                        isGetUserInfo.value = true
                        _uiState.value = ReportUiState.Success("성공")
                    }
                    .onError {
                        _uiState.value = ReportUiState.Error(this.response.message())
                    }
                    .onException {
                        _uiState.value = ReportUiState.Error(this.message())
                    }
            }
        }
    }


    // 신고 내용 Text 변경
    fun updateText(newText: String) {
        _reportContent.value = newText.take(500)
    }

    // 신고 사유 변경
    fun updateReason(newReason: String) {
        _reportReason.value = newReason
    }

    // 신고 접수
    fun sendReport(){
        if(!isSendReport.value){
            _uiState.value = ReportUiState.Loading
            viewModelScope.launch {
                val result = reportUseCase.executeReport(
                    authorization= auth.value,
                    reporterNickname= reporterNickname.value,
                    reporterId= reporterId.value.toLong(),
                    receivedNickname= reportedNickname.value,
                    receivedId= reportedId.value.toLong(),
                    reason= reportReason.value,
                    reportContent= reportContent.value,
                    reportId= articleId.value.toLong(),
                    //reportTime= LocalDateTime.now(),
                    reportType= category.value
                    )

                result
                    .onSuccess {
                        _uiState.value = ReportUiState.SendSuccess
                        isSendReport.value = true
                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { sendReport()})

                        }
                        else{
                            Log.e("onError", this.message())
                            _uiState.value = ReportUiState.Error("${this.response.code()} Error")
                        }
                    }
                    .onException {
                        _uiState.value = ReportUiState.Error(this.message())
                    }

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
                        _uiState.value = ReportUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = ReportUiState.Error(this.message())
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = ReportUiState.Error(this.message())

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