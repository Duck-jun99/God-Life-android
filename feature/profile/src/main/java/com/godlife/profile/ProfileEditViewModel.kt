package com.godlife.profile

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.UpdateUserInfoUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class ProfileEditUiState {

    //사용자 변경 전 정보 로드 중 상태
    object Loading : ProfileEditUiState()

    //사용자 변경 전 정보 성공적으로 불러온 상태
    object Init : ProfileEditUiState()

    //변경 사항 전송 중 상태
    object SendLoading : ProfileEditUiState()

    // 변경 사항 전송 성공 상태
    data class Success(val data: String) : ProfileEditUiState()

    // 변경 사항 전송 실패 상태 or 사용자 변경 전 정보 불러오기 실패 상태
    data class Error(val message: String) : ProfileEditUiState()

}

enum class Work{
    PROFILE, BACKGROUND, INTRODUCE
}


@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val reissueUseCase: ReissueUseCase
): ViewModel(){

    /**
     * State
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<ProfileEditUiState>(ProfileEditUiState.Loading)
    val uiState: StateFlow<ProfileEditUiState> = _uiState

    //소개글 수정 뷰 Visiblity 상태 (false면 보여주지 않음, true이면 보여줌)
    private val _showIntroduceChangeViewState = MutableStateFlow(false)
    val showIntroduceChangeViewState: StateFlow<Boolean> = _showIntroduceChangeViewState

    /**
     * Data
     */

    //작업 리스트 변수
    private val _workList = MutableStateFlow<MutableList<Work>>(mutableListOf())
    val workList: StateFlow<MutableList<Work>> = _workList

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    //서버 통신 여부 플래그
    private val _isSend = MutableStateFlow(false)
    val isSend: StateFlow<Boolean> = _isSend

    //프로필 이미지 수정 상태 (false면 서버 반영 안함, true이면 서버 반영 완료)
    private val _profileChangeState = MutableStateFlow(false)
    val profileChangeState: StateFlow<Boolean> = _profileChangeState

    //배경 이미지 수정 상태 (false면 서버 반영 안함, true이면 서버 반영 완료)
    private val _backgroundChangeState = MutableStateFlow(false)
    val backgroundChangeState: StateFlow<Boolean> = _backgroundChangeState

    //소개글 수정 상태 (false면 서버 반영 안함, true이면 서버 반영 완료)
    private val _introduceChangeState = MutableStateFlow(false)
    val introduceChangeState: StateFlow<Boolean> = _introduceChangeState

    //선택한 이미지 타입 변수
    private val _selectedImageType = MutableStateFlow("")
    val selectedImageType: StateFlow<String> = _selectedImageType

    //프로필 이미지 Uri 저장 변수 (처음엔 userInfo에 담겨 있는 profileImage, 수정 시 변경)
    private val _profileImage = MutableStateFlow(Uri.EMPTY)
    val profileImage: StateFlow<Uri> = _profileImage

    //배경 이미지 Uri 저장 변수 (처음엔 userInfo에 담겨 있는 backgroundImage, 수정 시 변경)
    private val _backgroundImage = MutableStateFlow(Uri.EMPTY)
    val backgroundImage: StateFlow<Uri> = _backgroundImage

    //소개글 저장 변수 (처음엔 userInfo에 담겨 있는 introduce, 수정 시 변경)
    private val _introduce = MutableStateFlow("")
    val introduce: StateFlow<String> = _introduce

    //닉네임 저장 변수 (현재는 수정 지원 안함)
    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    //사용자 ID
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    /**
     * Init
     */

    init {
        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"

            //사용자 정보 초기화
            initUserInfo()


        }
    }

    /**
     * ProfileEditScreen에서 사용할 함수
     */

    //어떤 이미지 타입을 변경할지 알려주는 함수
    fun updateImageType(type: String){
        _selectedImageType.value = type
    }

    //소개글 수정 뷰 보여줄 여부 결정 함수 (ProfileEditScreen의 소개글 수정하기 버튼 클릭 시 호출)
    fun updateShowIntroduceChangeViewState(){
        _showIntroduceChangeViewState.value = !showIntroduceChangeViewState.value
    }


    //프로필 이미지 수정 버튼 이벤트
    fun updataProfileImage(uri: Uri){
        _profileImage.value = uri
        if(!workList.value.contains(Work.PROFILE)){
            _workList.value.add(Work.PROFILE)
        }
        Log.e("updateProfileImage", "${workList.value}")

    }

    //배경 이미지 수정 버튼 이벤트
    fun updateBackgroundImage(uri: Uri){
        _backgroundImage.value = uri
        if(!workList.value.contains(Work.BACKGROUND)){
            _workList.value.add(Work.BACKGROUND)
        }
        Log.e("updateBackgroundImage", "${workList.value}")

    }

    //소개글 수정 버튼 이벤트
    fun updateIntroduce(introduce: String){
        _introduce.value = introduce
        if(!workList.value.contains(Work.INTRODUCE)){
            _workList.value.add(Work.INTRODUCE)
        }
        Log.e("updateIntroduce", "${workList.value}")
    }

    //수정 정보 초기화 버튼 이벤트
    fun initUpdate(){

        initUserInfo()

        //작업 리스트 초기화
        _workList.value = mutableListOf()

    }

    //수정 완료 시 서버 통신 후 사용자 정보 반영
    fun completeUpdate() {
        Log.e("completeUpdate", "${workList.value}")
        if (!isSend.value && workList.value.isNotEmpty()) {
            _uiState.value = ProfileEditUiState.SendLoading
            viewModelScope.launch(Dispatchers.IO) {
                val initialWorks = workList.value.toSet() // 초기 작업 목록을 Set으로 저장
                val completedWorks = mutableSetOf<Work>() // 완료된 작업을 추적할 Set

                val jobs = initialWorks.map { work ->
                    async {
                        when (work) {
                            Work.PROFILE -> completeUpdateProfileImage()
                            Work.BACKGROUND -> completeUpdateBackgroundImage()
                            Work.INTRODUCE -> completeUpdateIntroduce()
                        }
                        completedWorks.add(work) // 작업 완료 후 추가
                    }
                }

                jobs.awaitAll() // 모든 작업이 완료될 때까지 대기

                withContext(Dispatchers.Main) {
                    if (completedWorks == initialWorks) {
                        _uiState.value = ProfileEditUiState.Success("프로필 수정이 완료되었습니다.")
                    } else {
                        val incompleteWorks = initialWorks - completedWorks
                        _uiState.value = ProfileEditUiState.Error("다음 작업이 완료되지 않았습니다: $incompleteWorks")
                    }
                    _workList.value.clear() // 모든 작업 처리 후 workList 초기화
                }

                _isSend.value = true
            }
        }
    }


    /**
     * Private 함수
     */

    //사용자 정보 초기화
    private fun initUserInfo(){
        if(auth.value != ""){

            _uiState.value = ProfileEditUiState.Loading

            _profileChangeState.value = false
            _backgroundChangeState.value = false
            _introduceChangeState.value = false

            viewModelScope.launch {

                val result = getUserInfoUseCase.executeGetUserInfo()

                result
                    .onSuccess {

                        if(data.body.profileImage != "") {
                            _profileImage.value = (BuildConfig.SERVER_IMAGE_DOMAIN + data.body.profileImage).toUri()
                        }
                        else{
                            _profileImage.value = data.body.profileImage.toUri()
                        }

                        if(data.body.backgroundImage != ""){
                            _backgroundImage.value = (BuildConfig.SERVER_IMAGE_DOMAIN + data.body.backgroundImage).toUri()
                        }
                        else{
                            _backgroundImage.value = data.body.backgroundImage.toUri()
                        }

                        _nickname.value = data.body.nickname
                        _introduce.value = data.body.whoAmI
                        _userId.value = data.body.memberId.toString()

                        _uiState.value = ProfileEditUiState.Init

                    }
                    .onError {
                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { initUserInfo() })

                        }
                        else{
                            _uiState.value = ProfileEditUiState.Error(this.message())
                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = ProfileEditUiState.Error(this.message())

                    }

            }

        }

    }

    //프로필 이미지에 수정이 있었을 때 서버에 요청
    private fun completeUpdateProfileImage(){

        if(!profileChangeState.value){

            viewModelScope.launch(Dispatchers.IO) {
                val result = updateUserInfoUseCase.executeProfileImageUpload(
                    authorization = auth.value,
                    imagePath = profileImage.value
                )

                result
                    .onSuccess {
                        _workList.value.remove(Work.PROFILE)
                        _profileChangeState.value = true
                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { completeUpdateProfileImage() })

                        }
                        else {
                            _uiState.value = ProfileEditUiState.Error(this.message())
                        }

                    }
                    .onException {
                        _uiState.value = ProfileEditUiState.Error(this.message())
                    }
            }

        }

    }


    //배경 이미지에 수정이 있었을 때 서버에 요청
    private fun completeUpdateBackgroundImage(){

        if(!backgroundChangeState.value){

            viewModelScope.launch(Dispatchers.IO) {

                val result = updateUserInfoUseCase.executeBackgroundImageUpload(
                    authorization = auth.value,
                    imagePath = backgroundImage.value
                )
                result
                    .onSuccess {
                        _workList.value.remove(Work.BACKGROUND)
                        _backgroundChangeState.value = true

                    }

                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { completeUpdateBackgroundImage() })

                        }

                        else{
                            _uiState.value = ProfileEditUiState.Error(this.message())
                        }

                    }
                    .onException {
                        _uiState.value = ProfileEditUiState.Error(this.message())
                    }

            }

        }
    }

    //소개글에 수정이 있었을 때 서버에 요청
    private fun completeUpdateIntroduce(){

        if(!introduceChangeState.value){

            viewModelScope.launch(Dispatchers.IO) {

                val result = updateUserInfoUseCase.executeUpdateIntroduce(
                    authorization = auth.value,
                    introduce = introduce.value
                )

                result
                    .onSuccess {
                        _workList.value.remove(Work.INTRODUCE)
                        _introduceChangeState.value = true
                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { completeUpdateIntroduce() })

                        }
                        else{
                            _uiState.value = ProfileEditUiState.Error(this.message())
                        }

                    }
                    .onException {
                        _uiState.value = ProfileEditUiState.Error(this.message())
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
                        _uiState.value = ProfileEditUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = ProfileEditUiState.Error("사용자 인증 오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = ProfileEditUiState.Error("사용자 인증 오류가 발생했습니다.")

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