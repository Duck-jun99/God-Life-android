package com.godlife.createtodolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.createtodolist.model.TodoList
import com.godlife.createtodolist.model.TodoListForm
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoTimeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateTodoListViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
) :ViewModel(){


    private val _todoList = MutableStateFlow(TodoList().getTodoList())
    val todoList: StateFlow<List<TodoListForm>> = _todoList

    private val _selectedList = MutableStateFlow<List<String>>(emptyList())
    val selectedList: StateFlow<List<String>> = _selectedList

    private val _notificationTime = MutableStateFlow<NotificationTimeData>(NotificationTimeData(0,0,0,0,0))
    private val notificationTime: StateFlow<NotificationTimeData> = _notificationTime

    private val _notificationSwitchState = MutableStateFlow(false)
    val notificationSwitchState: StateFlow<Boolean> = _notificationSwitchState

    //추가 플래그
    private val _flag = MutableStateFlow(0)
    val flag: StateFlow<Int> = _flag

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

        //알림 설정 안하면 알림 시간도 초기화
        if(!_notificationSwitchState.value){
            _notificationTime.value = NotificationTimeData(0,0,0,0,0)
        }
    }

    suspend fun getDatabase(){
        val allTodoList = localDatabaseUseCase.getAllTodoList()
        Log.e("CreateViewModel", allTodoList.toString())

    }


    fun addDatabase(){


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

            if(flag.value == 0){
                localDatabaseUseCase.insertTodo(data)
                Log.e("CreateViewModel", localDatabaseUseCase.getAllTodoList().toString())
                _flag.value = 1
            }


        }

    }

}