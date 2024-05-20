package com.godlife.login

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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

    //입력된 닉네임, 이메일, 나이, 성별
    val nickname = mutableStateOf("")
    val email = mutableStateOf("")
    val age = mutableStateOf("")
    val sex = mutableStateOf("")


    // 닉네임, 이메일, 나이, 성별 로직을 통과하는지에 대한 여부
    private val _checkedNickname = MutableStateFlow(false)
    val checkedNickname: StateFlow<Boolean> = _checkedNickname

    private val _checkedEmail = MutableStateFlow(false)
    val checkedEmail: StateFlow<Boolean> = _checkedEmail

    private val _checkedAge = MutableStateFlow(false)
    val checkedAge: StateFlow<Boolean> = _checkedAge

    private val _checkedSex = MutableStateFlow(false)
    val checkedSex: StateFlow<Boolean> = _checkedSex


    // 닉네임, 이메일, 나이, 성별에 대한 메시지
    private val _checkedNicknameMessage = MutableStateFlow("닉네임을 입력해주세요.")
    val checkedNicknameMessage: StateFlow<String> = _checkedNicknameMessage

    private val _checkedEmailMessage = MutableStateFlow("이메일을 입력해주세요.")
    val checkedEmailMessage: StateFlow<String> = _checkedEmailMessage

    private val _checkedAgeMessage = MutableStateFlow("나이를 입력해주세요.")
    val checkedAgeMessage: StateFlow<String> = _checkedAgeMessage

    private val _checkedSexMessage = MutableStateFlow("남 또는 여 를 입력해주세요.")
    val checkedSexMessage: StateFlow<String> = _checkedSexMessage

    // 서버에 닉네임과 이메일이 중복되는지 확인하기 위함
    private val _checkedServerNickname = MutableStateFlow(false)
    val checkedServerNickname: StateFlow<Boolean> = _checkedServerNickname

    private val _checkedServerEmail = MutableStateFlow(false)
    val checkedServerEmail: StateFlow<Boolean> = _checkedServerEmail

    suspend fun checkServerNickname(nickname: String) {
        _checkedServerNickname.value = signUpUseCase.executeCheckNickname(nickname)!!.check
        if(_checkedServerNickname.value){
            //낙네임 로직, 서버 중복 모두 통과
            _checkedNicknameMessage.value = "사용 가능한 닉네임 이에요."
            _checkedNickname.value = true
        }
        else{
            _checkedNicknameMessage.value = "이미 사용중인 닉네임 이에요."
        }
        //return signUpUseCase.executeCheckNickname(nickname)!!.check
    }

    suspend fun checkServerEmail(email: String) {
        _checkedServerEmail.value = signUpUseCase.executeCheckEmail(email)!!.check
        if(_checkedServerEmail.value){
            //이메일 로직, 서버 중복 모두 통과
            _checkedEmailMessage.value = "사용 가능한 이메일 이에요."
            _checkedEmail.value = true
        }
        else{
            _checkedEmailMessage.value = "이미 사용중인 이메일 이에요."
        }
        //return signUpUseCase.executeCheckEmail(email)!!.check
    }

    //닉네임 체크 로직, 모두 통과 시 서버에서 중복 확인 로직으로 넘어감.
    //닉네임에 공백 허용하지 않고 10자까지 제한
    fun checkNicknameLogic(nickname: String){

        _checkedNickname.value = false

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
            // 닉네임이 유효한 경우 서버에 중복된 닉네임이 있는지 확인
            _checkedNicknameMessage.value = "중복 확인중 이에요."
            viewModelScope.launch(Dispatchers.IO){
                checkServerNickname(nickname)
            }
        }
    }

    //이메일 체크 로직, 모두 통과 시 서버에서 중복 확인 로직으로 넘어감.
    fun checkEmailLogic(email: String){

        _checkedEmail.value = false

        val emailRegex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
        if(!emailRegex.matches(email)){
            _checkedEmailMessage.value = "올바른 이메일 형식을 입력해 주세요."
        }
        else{
            _checkedEmailMessage.value = "중복 확인중 이에요."
            viewModelScope.launch(Dispatchers.IO) {
                checkServerEmail(email)
            }
        }
    }

    fun checkAgeLogic(age: String){
        _checkedAge.value = false
        if(age!=null){
            _checkedAge.value = true
            _checkedAgeMessage.value = "입력이 확인되었어요."
        }
    }

    fun checkSexLogic(sex: String){
        _checkedSex.value = false

        if(sex.equals("남") || sex.equals("여")){
            _checkedSex.value = true
            _checkedSexMessage.value = "입력이 확인되었어요."
        }
        else{
            _checkedSexMessage.value = "남 또는 여 를 올바르게 입력해주세요."
        }
    }

}