package com.godlife.main_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.database.model.TodoEntity
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoList
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
import java.time.LocalDateTime
import javax.inject.Inject

sealed class MainPageUiState {
    object Loading : MainPageUiState()
    data class Success(val data: String) : MainPageUiState()
    data class Error(val message: ErrorType) : MainPageUiState()
}

enum class ErrorType {
    REFRESH_TOKEN_EXPIRED{
        override fun toString(): String {
            return "세션이 만료되었습니다. 다시 로그인해주세요." }
    },
    UNKNOWN_ERROR{
        override fun toString(): String {
            return "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요." }
    },
    SERVER_ERROR{
        override fun toString(): String {
            return "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요." }
    }
}

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val reissueUseCase: ReissueUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase
): ViewModel(){

    /**
     * State 관련
     */

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<MainPageUiState>(MainPageUiState.Loading)
    val uiState: StateFlow<MainPageUiState> = _uiState

    // 오늘 투두리스트 설정 상태
    private val _todayTodoListExists = MutableStateFlow<Boolean>(false)
    val todayTodoListExists: StateFlow<Boolean> = _todayTodoListExists

    // 유저 정보 불러온 상태
    private val _userInfoExists = MutableStateFlow<Boolean>(false)
    val userInfoExists: StateFlow<Boolean> = _userInfoExists

    /**
     * 변수 관련
     */

    // 유저 정보 초기화
    private val _userInfo = MutableStateFlow<UserInfoBody>(UserInfoBody("", 0, "", 0, "", "", ""))
    val userInfo: StateFlow<UserInfoBody> = _userInfo

    // 오늘 투두리스트
    private val _todayTodoList = MutableStateFlow<TodoEntity>(TodoEntity(0, emptyList(), EndTimeData(0,0,0,0,0), NotificationTimeData(0,0,0,0,0)))
    val todayTodoList: StateFlow<TodoEntity> = _todayTodoList

    //오늘 투두리스트 진행 상황
    private val _completedCount = MutableStateFlow<Int>(0)
    val completedCount: StateFlow<Int> = _completedCount


    /**
     * 초기화
     */

    init {
        // 유저 정보 가져오기
        getUserInfo()

        // 로컬 데이터베이스에서 투두리스트 가져오기
        getTodayTodoList()


        /*
        Log.e("init", "userInfoExists.value: ${userInfoExists.value}")

        // getTodayTodoList: 오늘 투두리스트 가져오기 (오늘 투두 리스트가 존재하는지 아닌지 상관 없이 작업이 완료되면 true)
        //_userInfoExists, getTodayTodoList가 true이면 Success
        if(userInfoExists.value){

            _uiState.value = MainPageUiState.Success("Success")

        }

         */
    }


    /**
     * 로직
     */

    //오늘 설정한 투두 리스트를 로컬 데이터베이스에서 가져오기
    private fun getTodayTodoList(){

        viewModelScope.launch(Dispatchers.IO) {

            val allTodoList = localDatabaseUseCase.getAllTodoList()

            allTodoList.forEach {

                if(it.endTime.y == LocalDateTime.now().year
                    && it.endTime.m == LocalDateTime.now().monthValue
                    && it.endTime.d == LocalDateTime.now().dayOfMonth){

                    Log.e("allTodoList", "${it.endTime.y}, ${it.endTime.m}, ${it.endTime.d}," +
                            " ${LocalDateTime.now().year}, ${LocalDateTime.now().monthValue},${LocalDateTime.now().dayOfMonth}")

                    _todayTodoList.value = it
                    _todayTodoListExists.value = true

                }

            }

        }

    }

    // 서버로부터 유저 정보 가져오기
    private fun getUserInfo(){

        viewModelScope.launch {

            var auth = ""
            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            val response = getUserInfoUseCase.executeGetUserInfo(auth)

            response
                //성공적으로 넘어오면 유저 정보를 저장
                .onSuccess {

                    _userInfo.value = data.body

                    _userInfoExists.value = true

                    _uiState.value = MainPageUiState.Success("Success")

                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 재발급 요청
                    if(this.response.code() == 401){

                        reIssueRefreshToken()

                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = MainPageUiState.Error(ErrorType.UNKNOWN_ERROR)

                }

        }

    }

    // refresh token 갱신 후 getUserInfo 다시 실행
    private fun reIssueRefreshToken(){
        viewModelScope.launch(Dispatchers.IO) {

            var auth = ""
            launch { auth = "Bearer ${localPreferenceUserUseCase.getRefreshToken()}" }.join()

            val response = reissueUseCase.executeReissue(auth)

            response
                //성공적으로 넘어오면 유저 정보의 토큰을 갱신
                .onSuccess {

                    localPreferenceUserUseCase.saveAccessToken(data.body.accessToken)
                    localPreferenceUserUseCase.saveRefreshToken(data.body.refreshToken)

                    getUserInfo()

                }
                .onError {
                    Log.e("onError", this.message())

                    // 토큰 만료시 로컬에서 토큰 삭제하고 로그아웃 메시지
                    if(this.response.code() == 400){

                        deleteLocalToken()

                        // UI State Error로 변경
                        _uiState.value = MainPageUiState.Error(ErrorType.REFRESH_TOKEN_EXPIRED)

                    }

                    //기타 오류 시
                    else{

                        // UI State Error로 변경
                        _uiState.value = MainPageUiState.Error(ErrorType.SERVER_ERROR)
                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                    // UI State Error로 변경
                    _uiState.value = MainPageUiState.Error(ErrorType.UNKNOWN_ERROR)

                }


        }
    }

    // 로컬에서 토큰 및 사용자 정보 삭제
    private fun deleteLocalToken() {

        viewModelScope.launch(Dispatchers.IO) {

            // 로컬 데이터베이스에서 사용자 정보 삭제 후 완료되면 true 반환
            localPreferenceUserUseCase.removeAccessToken()
            localPreferenceUserUseCase.removeUserId()
            localPreferenceUserUseCase.removeRefreshToken()

        }

    }

    fun getTodoListCount():List<Int>{
        var completedCount = 0
        todayTodoList.value.todoList.forEach {
            if(it.iscompleted){
                completedCount+=1
            }
        }
        return listOf(todayTodoList.value.todoList.size, completedCount)
    }

    fun setTodoValueCompleted(todo: TodoList){
        viewModelScope.launch(Dispatchers.IO) {
            val newList = _todayTodoList.value
            newList.todoList = newList.todoList.map { if(it == todo) it.copy(iscompleted = true) else it }
            _todayTodoList.value = newList


        }
    }

    // 현재 시간대에 따른 인사말
    fun setTodayTimeText(nickname: String): List<Any> {
        val hour = LocalDateTime.now().hour

        return when (hour) {
            in 0..5 -> listOf("${nickname}님이 어두운 새벽을 빛내주고 있어요.", R.drawable.moon_icon8)
            in 6..8 -> listOf("아침부터 부지런하시군요!", R.drawable.sun_icons8)
            in 9 .. 11 -> listOf("활기찬 오전 보내세요!", R.drawable.sun_icons8)
            in 12..18 -> listOf("오후도 화이팅이에요!", R.drawable.sun_icons8)
            in 19..23 -> listOf("저녁에도 노력하시는 모습이 멋있어요!", R.drawable.moon_icon8)
            else -> listOf("화이팅이에요 항상!", R.drawable.sun_icons8) // 예외 상황 처리
        }
    }




}