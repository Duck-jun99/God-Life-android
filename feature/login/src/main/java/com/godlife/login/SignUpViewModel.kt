package com.godlife.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    //각각 로직을 모두 통과하면 다음 뷰를 보여줄지 여부
    private val _showEditEmail = MutableStateFlow(false)
    val showEditEmail: StateFlow<Boolean> = _showEditEmail

    private val _showEditAge = MutableStateFlow(false)
    val showEditAge: StateFlow<Boolean> = _showEditAge

    private val _showEditSex = MutableStateFlow(false)
    val showEditSex: StateFlow<Boolean> = _showEditSex

    // 닉네임, 이메일 로직을 통과하는지에 대한 여부
    private val _checkedNickname = MutableStateFlow(false)
    val checkedNickname: StateFlow<Boolean> = _checkedNickname

    private val _checkedEmail = MutableStateFlow(false)
    val checkedEmail: StateFlow<Boolean> = _checkedEmail


    // 닉네임, 이메일 로직을 통과하는지에 대한 메시지
    private val _checkedNicknameMessage = MutableStateFlow("닉네임을 입력해주세요.")
    val checkedNicknameMessage: StateFlow<String> = _checkedNicknameMessage

    private val _checkedEmailMessage = MutableStateFlow("이메일을 입력해주세요.")
    val checkedEmailMessage: StateFlow<String> = _checkedEmailMessage

    // 서버에 닉네임과 이메일이 중복되는지 확인하기 위함
    private val _checkedServerNickname = MutableStateFlow(false)
    val checkedServerNickname: StateFlow<Boolean> = _checkedServerNickname

    private val _checkedServerEmail = MutableStateFlow(false)
    val checkedServerEmail: StateFlow<Boolean> = _checkedServerEmail

    suspend fun checkServerNickname(nickname: String) {
        _checkedServerNickname.value = signUpUseCase.executeCheckNickname(nickname)!!.check
        if(_checkedServerNickname.value){
            _checkedNicknameMessage.value = "사용 가능한 닉네임이에요."
            _showEditEmail.value = true
        }
        else{
            _checkedNicknameMessage.value = "이미 사용중인 닉네임이에요."
        }
        //return signUpUseCase.executeCheckNickname(nickname)!!.check
    }

    suspend fun checkServerEmail(email: String) {
        _checkedServerEmail.value = signUpUseCase.executeCheckEmail(email)!!.check
        if(_checkedServerEmail.value){
            _checkedEmailMessage.value = "사용 가능한 이메일이에요."
        }
        else{
            _checkedEmailMessage.value = "이미 사용중인 이메일이에요."
        }
        //return signUpUseCase.executeCheckEmail(email)!!.check
    }

    //닉네임에 공백 허용하지 않고 10자까지 제한
    fun checkNicknameLogic(nickname: String){

        val containsEmptyspace = nickname.contains(" ")

        if (containsEmptyspace) {
            // 닉네임에 공백이 포함된 경우
            _checkedNicknameMessage.value = "공백을 제거해주세요."
        }
        else if (nickname.trim().isEmpty()) {
            // 닉네임이 공백만 포함하는 경우
            _checkedNicknameMessage.value = "닉네임을 입력해주세요."
        }
        else if (nickname.trim().length > 10) {
            // 닉네임이 10자를 초과하는 경우
            _checkedNicknameMessage.value = "10자까지만 가능해요."

        }
        else {
            // 닉네임이 유효한 경우
            _checkedNickname.value = true
            _checkedNicknameMessage.value = "중복 확인중 이에요."
            viewModelScope.launch(Dispatchers.IO){
                checkServerNickname(nickname)
            }
        }
    }

    fun checkEmailLogic(email: String){
        val emailRegex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
        if(!emailRegex.matches(email)){
            _checkedEmailMessage.value = "올바른 이메일 형식을 입력해주세요."
        }
        else{
            _checkedEmail.value = true
            _checkedEmailMessage.value = "중복 확인중 이에요."
            viewModelScope.launch(Dispatchers.IO) {
                checkServerEmail(email)
            }
        }
    }

}