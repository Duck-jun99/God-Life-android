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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
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

    // 로그아웃 플래그
    private val logoutFlag = mutableStateOf(false)

    init {

        getUserInfo()
    }

    private fun getUserInfo() {
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
                    if(this.response.code() == 400){

                        _uiState.value = SettingPageUiState.Error("세션이 만료되었습니다. 다시 로그인 해주세요")

                    }
                    else {
                        _uiState.value = SettingPageUiState.Error("${this.response.code()} Error")
                    }

                }
                .onException {

                    _uiState.value = SettingPageUiState.Error(this.message())

                }

        }

    }

    fun logout(){
        if(!logoutFlag.value){
            logoutFlag.value = true
            viewModelScope.launch(Dispatchers.IO) {

                val result = withContext(Dispatchers.IO) {

                    Log.e("로그아웃 전 Access", localPreferenceUserUseCase.getAccessToken())
                    Log.e("로그아웃 전 UserId", localPreferenceUserUseCase.getUserId())
                    Log.e("로그아웃 전 Refresh", localPreferenceUserUseCase.getRefreshToken())

                    logoutUseCase.executeLogout()

                    localPreferenceUserUseCase.removeAccessToken()
                    localPreferenceUserUseCase.removeUserId()
                    localPreferenceUserUseCase.removeRefreshToken()

                    //로그아웃 시 FCM 토큰 삭제 (빈 문자열로 처리)
                    //registerFCMTokenUseCase.executeRegisterFCMToken(auth.value, "")

                    Log.e("로그아웃 후 Access", localPreferenceUserUseCase.getAccessToken())
                    Log.e("로그아웃 후 UserId", localPreferenceUserUseCase.getUserId())
                    Log.e("로그아웃 후 Refresh", localPreferenceUserUseCase.getRefreshToken())
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

}