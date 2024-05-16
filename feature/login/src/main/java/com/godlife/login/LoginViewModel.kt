package com.godlife.login

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.login.social_login_manager.KakaoLoginManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase): ViewModel() {

        fun saveAccessToken(accessToken: String){
            localPreferenceUserUseCase.saveAccessToken(accessToken)
        }
        fun getAccessToken() :String {
            return localPreferenceUserUseCase.getAccessToken()
        }


    init {

    }
}