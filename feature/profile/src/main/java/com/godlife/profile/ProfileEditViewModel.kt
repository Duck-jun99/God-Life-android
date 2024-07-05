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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileEditUiState {
    object Loading : ProfileEditUiState()
    data class Success(val data: String) : ProfileEditUiState()
    data class Error(val message: String) : ProfileEditUiState()

    data class Result(val success: Boolean, val message: String) : ProfileEditUiState()
}

sealed class UploadState {
    object Loading : UploadState()
    data class Success(val data: String) : UploadState()
    data class Error(val message: String) : UploadState()
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

    //프로필 이미지 수정 상태 (false면 수정 안함, true이면 수정함)
    private val _profileChangeState = MutableStateFlow(false)
    val profileChangeState: StateFlow<Boolean> = _profileChangeState

    //배경 이미지 수정 상태 (false면 수정 안함, true이면 수정함)
    private val _backgroundChangeState = MutableStateFlow(false)
    val backgroundChangeState: StateFlow<Boolean> = _backgroundChangeState

    //소개글 수정 상태 (false면 수정 안함, true이면 수정함)
    private val _introduceChangeState = MutableStateFlow(false)
    val introduceChangeState: StateFlow<Boolean> = _introduceChangeState

    //소개글 수정 뷰 Visiblity 상태 (false면 보여주지 않음, true이면 보여줌)
    private val _showIntroduceChangeViewState = MutableStateFlow(false)
    val showIntroduceChangeViewState: StateFlow<Boolean> = _showIntroduceChangeViewState

    //프로필 이미지 서버 통신 성공 여부
    private val _profileUploadState = MutableStateFlow<UploadState>(UploadState.Loading)
    val profileUploadState = _profileUploadState.asStateFlow()

    //배경 이미지 서버 통신 성공 여부
    private val _backgroundUploadState = MutableStateFlow<UploadState>(UploadState.Loading)
    val backgroundUploadState = _backgroundUploadState.asStateFlow()

    //소개글 서버 통신 성공 여부
    private val _introduceUploadState = MutableStateFlow<UploadState>(UploadState.Loading)
    val introduceUploadState = _introduceUploadState.asStateFlow()

    /**
     * Data
     */

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

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

    /**
     * Init
     */

    init {
        viewModelScope.launch {
            //엑세스 토큰 저장
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"

            //사용자 정보 초기화
            initUserInfo()

            combine(
                profileUploadState,
                backgroundUploadState,
                introduceUploadState
            ) { profileUpload, backgroundUpload, introduceUpload ->
                Triple(profileUpload, backgroundUpload, introduceUpload)
            }.collectLatest { (profileUpload, backgroundUpload, introduceUpload) ->
                when {
                    profileUpload is UploadState.Loading ||
                            backgroundUpload is UploadState.Loading ||
                            introduceUpload is UploadState.Loading -> {
                        _uiState.value = ProfileEditUiState.Loading
                    }
                    profileUpload is UploadState.Error ||
                            backgroundUpload is UploadState.Error ||
                            introduceUpload is UploadState.Error -> {
                        _uiState.value = ProfileEditUiState.Result(
                            success = false,
                            message = (profileUpload as? UploadState.Error)?.message ?:
                            (backgroundUpload as? UploadState.Error)?.message ?:
                            (introduceUpload as? UploadState.Error)?.message ?:
                            "오류가 발생했습니다."
                        )
                    }
                    profileUpload is UploadState.Success &&
                            backgroundUpload is UploadState.Success &&
                            introduceUpload is UploadState.Success -> {
                        _uiState.value = ProfileEditUiState.Result(
                            success = true,
                            message = "프로필 수정이 완료되었습니다."
                        )
                    }
                }
            }
        }
    }

    /**
     * ProfileEditScreen에서 사용할 함수
     */

    //어떤 이미지 타입을 변경할지 알려주는 함수
    fun updateImageType(type: String){
        _selectedImageType.value = type
    }

    //프로필 이미지 수정
    fun updataProfileImage(uri: Uri){

        _profileChangeState.value = true
        _profileImage.value = uri
    }

    //배경 이미지 수정
    fun updateBackgroundImage(uri: Uri){
        _backgroundChangeState.value = true
        _backgroundImage.value = uri
    }

    //소개글 수정
    fun updateIntroduce(introduce: String){
        _introduceChangeState.value = true
        _introduce.value = introduce
    }

    //소개글 수정 뷰 보여줄 여부 결정 함수 (ProfileEditScreen의 소개글 수정하기 버튼 클릭 시 호출)
    fun updateShowIntroduceChangeViewState(){
        _showIntroduceChangeViewState.value = !showIntroduceChangeViewState.value
    }

    //수정 정보 초기화
    fun initUpdate(){

        initUserInfo()

    }

    //수정 완료 시 서버 통신 후 사용자 정보 반영
    fun completeUpdate(){

        completeUpdateProfileImage()

        completeUpdateBackgroundImage()

        completeUpdateIntroduce()

        //checkAllUpdatesSuccess()

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

                val result = getUserInfoUseCase.executeGetUserInfo(authorization = auth.value)

                result
                    .onSuccess {

                        _uiState.value = ProfileEditUiState.Success("success")

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

                    }
                    .onError {
                        Log.e("onError", this.message())

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { initUserInfo() })

                        }
                    }
                    .onException {

                        Log.e("onException", "${this.message}")

                        // UI State Error로 변경
                        _uiState.value = ProfileEditUiState.Error("오류가 발생했습니다.")

                    }

            }

        }

    }

    //프로필 이미지에 수정이 있었을 때 서버에 요청
    private fun completeUpdateProfileImage(){

        if(profileChangeState.value){

            viewModelScope.launch(Dispatchers.IO) {
                val result = updateUserInfoUseCase.executeProfileImageUpload(
                    authorization = auth.value,
                    imagePath = profileImage.value
                )

                result
                    .onSuccess {
                        _profileUploadState.value = UploadState.Success("success")
                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { completeUpdateProfileImage() })

                        }
                        else {
                            _profileUploadState.value = UploadState.Error("오류가 발생했습니다.")
                        }

                    }
                    .onException {

                        _profileUploadState.value = UploadState.Error("오류가 발생했습니다.")

                    }
            }

        }

        else {

            _profileUploadState.value = UploadState.Success("success")
        }

    }

    //배경 이미지에 수정이 있었을 때 서버에 요청
    private fun completeUpdateBackgroundImage(){

        if(backgroundChangeState.value){

            viewModelScope.launch(Dispatchers.IO) {

                val result = updateUserInfoUseCase.executeBackgroundImageUpload(
                    authorization = auth.value,
                    imagePath = backgroundImage.value
                )
                result
                    .onSuccess {
                        _backgroundUploadState.value = UploadState.Success("success")

                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { completeUpdateBackgroundImage() })

                        }

                        else{
                            _backgroundUploadState.value = UploadState.Error("오류가 발생했습니다.")

                        }

                    }
                    .onException {
                        _backgroundUploadState.value = UploadState.Error("오류가 발생했습니다.")

                    }

            }

        }
        else{
            _backgroundUploadState.value = UploadState.Success("success")
        }
    }

    //소개글에 수정이 있었을 때 서버에 요청
    private fun completeUpdateIntroduce(){
        if(introduceChangeState.value){

            viewModelScope.launch(Dispatchers.IO) {

                val result = updateUserInfoUseCase.executeUpdateIntroduce(
                    authorization = auth.value,
                    introduce = introduce.value
                )

                result
                    .onSuccess {
                        _introduceUploadState.value = UploadState.Success("success")
                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken(callback = { completeUpdateIntroduce() })

                        }
                        else{
                            _introduceUploadState.value = UploadState.Error("오류가 발생했습니다.")

                        }

                    }
                    .onException {
                        _introduceUploadState.value = UploadState.Error("오류가 발생했습니다.")
                    }

            }


        }

        else{
            _introduceUploadState.value = UploadState.Success("success")
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
                        _uiState.value = ProfileEditUiState.Error("오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = ProfileEditUiState.Error("오류가 발생했습니다.")

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