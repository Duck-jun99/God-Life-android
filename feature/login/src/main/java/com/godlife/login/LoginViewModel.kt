package com.godlife.login

import androidx.lifecycle.ViewModel
import com.godlife.domain.LocalPreferenceUserUseCase
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

    fun saveRefreshToken(refreshToken: String){
        localPreferenceUserUseCase.saveRefreshToken(refreshToken)
    }

    fun getRefreshToken(): String{
        return localPreferenceUserUseCase.getRefreshToken()
    }

    fun saveUserId(userId: String){
        localPreferenceUserUseCase.saveUserId(userId)
    }

    fun getUserId(): String {
        return localPreferenceUserUseCase.getUserId()
    }



    init {

    }
}