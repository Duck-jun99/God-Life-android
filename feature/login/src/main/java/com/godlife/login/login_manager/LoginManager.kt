package com.godlife.login.login_manager

import android.content.Context
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.login.LoginViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LoginManager @Inject constructor(
    @ActivityContext private val context: Context,
    private val loginViewModel: LoginViewModel,
    private val getUserInfoUseCase: GetUserInfoUseCase
){
    // 로그인 시 서버에 User ID가 저장되어 있는지 확인.
    suspend fun checkUserId(userId: String){

        val checked = getUserInfoUseCase.executeGetUserInfo(userId)
    }
}