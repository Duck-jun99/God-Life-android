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

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val reissueUseCase: ReissueUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase
): ViewModel(){

    // 유저 정보 초기화
    private val _userInfo = MutableStateFlow<UserInfoBody>(UserInfoBody("", 0, "", 0, "", ""))
    val userInfo: StateFlow<UserInfoBody> = _userInfo

    private val _todoList = MutableStateFlow<TodoEntity>(TodoEntity(0, emptyList(), EndTimeData(0,0,0,0,0), NotificationTimeData(0,0,0,0,0)))
    val todoList: StateFlow<TodoEntity> = _todoList

    // 오늘 투두리스트 설정 유무
    private val _todayBoolean = MutableStateFlow<Boolean>(false)
    val todayBoolean: StateFlow<Boolean> = _todayBoolean

    //오늘 투두리스트 진행 상황
    private val _completedCount = MutableStateFlow<Int>(0)
    val completedCount: StateFlow<Int> = _completedCount

    init{
        viewModelScope.launch(Dispatchers.IO) {

            //오늘 설정한 투두 리스트를 로컬 데이터베이스에서 가져오기
            val allTodoList = localDatabaseUseCase.getAllTodoList()
            allTodoList.forEach {
                if(it.endTime.y == LocalDateTime.now().year
                    && it.endTime.m == LocalDateTime.now().monthValue
                    && it.endTime.d == LocalDateTime.now().dayOfMonth){
                    Log.e("allTodoList", "${it.endTime.y}, ${it.endTime.m}, ${it.endTime.d}," +
                            " ${LocalDateTime.now().year}, ${LocalDateTime.now().monthValue},${LocalDateTime.now().dayOfMonth}")
                    _todoList.value = it
                    _todayBoolean.value = true
                }

            }

            // 유저 정보 가져오기
            setUserInfoState()

        }
    }

    fun getTodoListCount():List<Int>{
        var completedCount = 0
        todoList.value.todoList.forEach {
            if(it.iscompleted){
                completedCount+=1
            }
        }
        return listOf(todoList.value.todoList.size, completedCount)
    }

    fun completeTodo(todo: TodoList){
        viewModelScope.launch(Dispatchers.IO) {
            val newList = _todoList.value
            newList.todoList = newList.todoList.map { if(it == todo) it.copy(iscompleted = true) else it }
            _todoList.value = newList

            /*
            val newList = _todoList.value.map { if (it == todo) it.copy(isSelected = !it.isSelected) else it }
            _todoList.value = newList

             */
        }
    }


    // 현재 시간대에 따른 인사말
    fun todayTimeText(nickname: String): List<Any> {
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

    // 서버로부터 유저 정보 가져오기
    private fun setUserInfoState(){

        viewModelScope.launch(Dispatchers.IO) {

            var auth = ""
            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            val response = getUserInfoUseCase.executeGetUserInfo(auth)


            response
                //성공적으로 넘어오면 유저 정보를 저장
                .onSuccess {
                    _userInfo.value = data.body
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
                }

        }

    }

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

                    setUserInfoState()

                }
                .onError {
                    Log.e("onError", this.message())


                    // 토큰 만료시 로그아웃
                    if(this.response.code() == 400){

                        logout()

                    }

                }
                .onException {
                    Log.e("onException", "${this.message}")

                }


        }
    }

    private fun logout() {
        viewModelScope.launch {

            // 로컬 데이터베이스에서 사용자 정보 삭제 후 로그아웃

            val result = withContext(Dispatchers.IO) {

                localPreferenceUserUseCase.removeAccessToken()
                localPreferenceUserUseCase.removeUserId()
                localPreferenceUserUseCase.removeRefreshToken()

                // 로그아웃이 완료되면 true를 반환
                true
            }
        }

    }
}