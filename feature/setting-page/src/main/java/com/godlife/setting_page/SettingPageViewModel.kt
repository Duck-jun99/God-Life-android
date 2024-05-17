package com.godlife.setting_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.LocalPreferenceUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingPageViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase
): ViewModel(){

    private val _logoutResult = MutableStateFlow<Boolean?>(null)
    val logoutResult: StateFlow<Boolean?> = _logoutResult

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