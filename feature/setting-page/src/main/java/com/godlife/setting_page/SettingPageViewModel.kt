package com.godlife.setting_page

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.LogoutUseCase
import com.godlife.domain.RegisterFCMTokenUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.network.model.UserInfoBody
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
import javax.inject.Inject

sealed class SettingPageUiState {
    object Loading : SettingPageUiState()
    data class Success(val data: String) : SettingPageUiState()
    data class Error(val message: String) : SettingPageUiState()
}

@HiltViewModel
class SettingPageViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val reissueUseCase: ReissueUseCase,
    private val registerFCMTokenUseCase: RegisterFCMTokenUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow<SettingPageUiState>(SettingPageUiState.Loading)
    val uiState: StateFlow<SettingPageUiState> = _uiState

    // 유저 정보 초기화
    private val _userInfo = MutableStateFlow<UserInfoBody>(UserInfoBody("", 0, "", 0, "", "", "", 0, ""))
    val userInfo: StateFlow<UserInfoBody> = _userInfo

    private val _logoutResult = MutableStateFlow<Boolean?>(null)
    val logoutResult: StateFlow<Boolean?> = _logoutResult

    //엑세스 토큰 저장 변수
    private val _auth = MutableStateFlow("")
    val auth: StateFlow<String> = _auth

    // 로그아웃 플래그
    private val logoutFlag = mutableStateOf(false)

    init {

        viewModelScope.launch {
            _auth.value = "Bearer ${localPreferenceUserUseCase.getAccessToken()}"

        }

        getUserInfo()
    }

    private fun getUserInfo() {
        if(auth.value != ""){

            viewModelScope.launch {

                val result = withContext(Dispatchers.IO) {
                    getUserInfoUseCase.executeGetUserInfo()
                }

                result
                    .onSuccess {

                        _userInfo.value = data.body
                        _uiState.value = SettingPageUiState.Success("success")

                    }
                    .onError {

                        // 토큰 만료시 재발급 요청
                        if(this.response.code() == 401){

                            reIssueRefreshToken { getUserInfo() }

                        }
                        else {
                            _uiState.value = SettingPageUiState.Error("오류가 발생했습니다.")
                        }

                    }
                    .onException {

                        _uiState.value = SettingPageUiState.Error("오류가 발생했습니다.")

                    }

            }

        }

    }

    fun logout(){
        if(!logoutFlag.value){
            logoutFlag.value = true
            viewModelScope.launch(Dispatchers.IO) {

                val result = withContext(Dispatchers.IO) {

                    Log.e("로그아웃 전", "${localPreferenceUserUseCase.getAccessToken()}")
                    Log.e("로그아웃 전", "${localPreferenceUserUseCase.getUserId()}")
                    Log.e("로그아웃 전", "${localPreferenceUserUseCase.getRefreshToken()}")

                    logoutUseCase.executeLogout(auth.value)

                    localPreferenceUserUseCase.removeAccessToken()
                    localPreferenceUserUseCase.removeUserId()
                    localPreferenceUserUseCase.removeRefreshToken()

                    //로그아웃 시 FCM 토큰 삭제 (빈 문자열로 처리)
                    //registerFCMTokenUseCase.executeRegisterFCMToken(auth.value, "")

                    Log.e("로그아웃 후", "${localPreferenceUserUseCase.getAccessToken()}")
                    Log.e("로그아웃 후", "${localPreferenceUserUseCase.getUserId()}")
                    Log.e("로그아웃 후", "${localPreferenceUserUseCase.getRefreshToken()}")
                    // 로그아웃이 완료되면 true를 반환
                    true
                }
                _logoutResult.value = result
            }
        }
    }


/*
    fun logout() {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                localPreferenceUserUseCase.removeAccessToken()
                localPreferenceUserUseCase.removeUserId()
                localPreferenceUserUseCase.removeRefreshToken()

                //로그아웃 시 FCM 토큰 삭제 (빈 문자열로 처리)
                registerFCMTokenUseCase.executeRegisterFCMToken(auth.value, "")

                Log.e("SettingPageViewModel", "${localPreferenceUserUseCase.getAccessToken()}")
                Log.e("SettingPageViewModel", "${localPreferenceUserUseCase.getUserId()}")
                Log.e("SettingPageViewModel", "${localPreferenceUserUseCase.getRefreshToken()}")
                // 로그아웃이 완료되면 true를 반환
                true
            }
            _logoutResult.value = result
        }

    }


 */


    // refresh token 갱신 후 Callback 실행
    private fun reIssueRefreshToken(callback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {

            val response = reissueUseCase.executeReissue(auth.value)

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
                        _uiState.value = SettingPageUiState.Error("재로그인 해주세요.")

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = SettingPageUiState.Error("오류가 발생했습니다.")
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = SettingPageUiState.Error("오류가 발생했습니다.")

                }


        }
    }

    // 로컬에서 토큰 및 사용자 정보 삭제
    private fun deleteLocalToken() {

        viewModelScope.launch(Dispatchers.IO) {

            // 로컬 데이터베이스에서 사용자 정보 삭제
            localPreferenceUserUseCase.removeAccessToken()
            localPreferenceUserUseCase.removeUserId()
            localPreferenceUserUseCase.removeRefreshToken()

        }

    }
}