package com.godlife.login

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.SignUpUseCase
import com.godlife.login.social_login_manager.KakaoLoginManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _checkedNickname = MutableStateFlow(false)
    val checkedNickname: StateFlow<Boolean> = _checkedNickname

    private val _checkedEmail = MutableStateFlow(false)
    val checkedEmail: StateFlow<Boolean> = _checkedEmail

    suspend fun checkNickname(nickname: String):Boolean {
        _checkedNickname.value = signUpUseCase.executeCheckNickname(nickname)!!.check
        return signUpUseCase.executeCheckNickname(nickname)!!.check
    }

    suspend fun checkEmail(email: String):Boolean {
        _checkedEmail.value = signUpUseCase.executeCheckEmail(email)!!.check
        return signUpUseCase.executeCheckEmail(email)!!.check
    }

}