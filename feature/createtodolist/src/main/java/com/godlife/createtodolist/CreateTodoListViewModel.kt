package com.godlife.createtodolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.createtodolist.model.TodoList
import com.godlife.createtodolist.model.TodoListForm
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.domain.PostNotificationTimeUseCase
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoTimeData
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

sealed class CreateTodoListUiState {
    object Loading : CreateTodoListUiState()
    data class NotiSuccess(val data: String) : CreateTodoListUiState()
    data class Success(val data: String) : CreateTodoListUiState()
    data class Error(val message: String) : CreateTodoListUiState()
}

@HiltViewModel
class CreateTodoListViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase,
    private val postNotificationTimeUseCase: PostNotificationTimeUseCase
) :ViewModel(){

    /**
     * State
     */

    // UI 상태 (알림 시간 서버에 전송 중 -> Loading, 성공 -> Success, 실패 -> Error)
    private val _uiState = MutableStateFlow<CreateTodoListUiState>(CreateTodoListUiState.Loading)
    val uiState: StateFlow<CreateTodoListUiState> = _uiState

    //알림 설정 유무
    private val _notificationSwitchState = MutableStateFlow(false)
    val notificationSwitchState: StateFlow<Boolean> = _notificationSwitchState

    /**
     * Data
     */


    private val _todoList = MutableStateFlow(TodoList().getTodoList())
    val todoList: StateFlow<List<TodoListForm>> = _todoList

    private val _selectedList = MutableStateFlow<List<String>>(emptyList())
    val selectedList: StateFlow<List<String>> = _selectedList

    private val _notificationTime = MutableStateFlow<NotificationTimeData>(NotificationTimeData(0,0,0,0,0))
    private val notificationTime: StateFlow<NotificationTimeData> = _notificationTime


    //데이터베이스 추가 플래그
    private val _dbFlag = MutableStateFlow(0)
    val dbFlag: StateFlow<Int> = _dbFlag

    //서버 알림 시간 전송 플래그
    private val _serverFlag = MutableStateFlow(0)
    val serverFlag: StateFlow<Int> = _serverFlag

    fun toggleSelect(todo: TodoListForm){

        val newList = _todoList.value.map { if (it == todo) it.copy(isSelected = !it.isSelected) else it }
        _todoList.value = newList

    }


    fun addToSelectedList(todo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedList.value += todo
        }
    }

    fun deleteToSelectedList(todo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedList.value = _selectedList.value.toMutableList().apply { remove(todo) }
        }
    }

    fun updateSelectedList(selectedList: List<String>){
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("updateSelectedList", _selectedList.value.toString())
            _selectedList.value = selectedList
        }
    }

    fun updateNotificationTime(time: NotificationTimeData) {

        _notificationTime.value = time

    }

    fun updateNotificationSwitchState() {
        _notificationSwitchState.value = !notificationSwitchState.value

        //알림 설정 안하면
        if(!_notificationSwitchState.value){
            //알림 시간 초기화
            _notificationTime.value = NotificationTimeData(0,0,0,0,0)

            //Ui State NotiSuccess로 변경
            _uiState.value = CreateTodoListUiState.NotiSuccess("알림 시간 설정 안함")
        }

        //알림 설정하면
        else{
            //Ui State Loading으로 변경
            _uiState.value = CreateTodoListUiState.Loading
        }
    }

    // 전체 과정 작업 (내부 DB 저장, 서버에 알림 시간 전송)
    fun saveTodoList(){

        when(notificationSwitchState.value){

            //알림 설정하면 서버에 알림 시간 전송 후 성공하면 내부 DB에 저장
            true -> {

                viewModelScope.launch {

                    if(serverFlag.value == 0){

                        _serverFlag.value = 1

                        delay(2000L)

                        //서버에 알림 시간 전송 후 성공하면 내부 DB에 저장
                        val result =
                            postNotificationTimeUseCase.executePostNotificationTime(
                                notificationTime = notificationTime.value
                            )

                        result
                            .onSuccess {

                                _uiState.value = CreateTodoListUiState.NotiSuccess("알림 시간 전송 성공")

                                addDatabase()

                            }
                            .onError {

                                // 토큰 만료시
                                if(this.response.code() == 400){
                                    _uiState.value = CreateTodoListUiState.Error("세션이 만료되었어요. 재로그인 해주세요.")

                                }
                                else{
                                    // UI State Error로 변경
                                    _uiState.value = CreateTodoListUiState.Error("${this.response.code()} Error")

                                }

                            }
                            .onException {

                                _uiState.value = CreateTodoListUiState.Error(this.message())

                            }

                    }



                }

            }
            //알림 설정 안하면 그냥 내부 DB에 저장
            false -> {

                addDatabase()

            }
        }




    }

    private fun addDatabase(){

        //Format Data
        val formattedTodoList: List<com.godlife.model.todo.TodoList>
                = selectedList.value.map { com.godlife.model.todo.TodoList(it, false) }

        val data = TodoEntity(
            date = TodoTimeData(
                LocalDateTime.now().year,
                LocalDateTime.now().monthValue,
                LocalDateTime.now().dayOfMonth
            ),
            todoList = formattedTodoList,
            notificationBoolean = notificationSwitchState.value,
            notificationTime = notificationTime.value,
            isCompleted = false
        )

        //Log.e("CreateViewModel", "data: ${data.toString()}")


        viewModelScope.launch(Dispatchers.IO){

            if(dbFlag.value == 0){
                _dbFlag.value = 1

                localDatabaseUseCase.insertTodo(data)
                Log.e("CreateViewModel", localDatabaseUseCase.getAllTodoList().toString())

                delay(3000L)

                _uiState.value = CreateTodoListUiState.Success("성공")


            }

        }

    }

    suspend fun getDatabase(){
        val allTodoList = localDatabaseUseCase.getAllTodoList()
        Log.e("CreateViewModel", allTodoList.toString())

    }

}