package com.godlife.setting_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.network.model.UserInfoBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingPageViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel(){

    // 유저 정보 초기화
    private val _userInfo = MutableStateFlow<UserInfoBody>(UserInfoBody("", 0, "", 0, "", "", ""))
    val userInfo: StateFlow<UserInfoBody> = _userInfo

    private val _logoutResult = MutableStateFlow<Boolean?>(null)
    val logoutResult: StateFlow<Boolean?> = _logoutResult

    init {

        viewModelScope.launch(Dispatchers.IO) {

            //Access Token을 통해 유저 정보 초기화
            var auth = ""
            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            //_userInfo.value = getUserInfoUseCase.executeGetUserInfo(auth).body

        }
    }



    fun logout() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                localPreferenceUserUseCase.removeAccessToken()
                localPreferenceUserUseCase.removeUserId()
                localPreferenceUserUseCase.removeRefreshToken()

                Log.e("SettingPageViewModel", "${localPreferenceUserUseCase.getAccessToken()}")
                Log.e("SettingPageViewModel", "${localPreferenceUserUseCase.getUserId()}")
                Log.e("SettingPageViewModel", "${localPreferenceUserUseCase.getRefreshToken()}")
                // 로그아웃이 완료되면 true를 반환
                true
            }
            _logoutResult.value = result
        }

    }
}