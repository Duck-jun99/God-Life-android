package com.godlife.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.navigator.MainNavigator
import com.godlife.network.model.BodyQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _userExistenceResult = MutableStateFlow<Boolean?>(null)
    val userExistenceResult: StateFlow<Boolean?> = _userExistenceResult

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

    /*
    fun checkUserExistence(userId :String) {

        viewModelScope.launch(Dispatchers.IO) {
            _userExistenceResult.value = getUserInfoUseCase.executeGetUserInfo(userId)?.check
        }
    }

     */

    suspend fun checkUserExistence(userId :String): BodyQuery {

        return getUserInfoUseCase.executeGetUserInfo(userId)!!.body
    }

}