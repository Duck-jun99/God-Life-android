package com.godlife.createtodolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.createtodolist.model.TodoList
import com.godlife.createtodolist.model.TodoListForm
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
) :ViewModel(){


    private val _todoList = MutableStateFlow(TodoList().getTodoList())
    val todoList: StateFlow<List<TodoListForm>> = _todoList

    private val _selectedList = MutableStateFlow<List<String>>(emptyList())
    val selectedList: StateFlow<List<String>> = _selectedList

    private val _endTime = MutableStateFlow<EndTimeData?>(null)
    val endTime: StateFlow<EndTimeData?> = _endTime

    private val _notificationTime = MutableStateFlow<NotificationTimeData?>(null)
    val notificationTime: StateFlow<NotificationTimeData?> = _notificationTime

    fun toggleSelect(todo: TodoListForm){
        viewModelScope.launch(Dispatchers.IO) {
            //_todoList.value[_todoList.value.indexOf(todo)].isSelected = !_todoList.value[_todoList.value.indexOf(todo)].isSelected
            val newList = _todoList.value.map { if (it == todo) it.copy(isSelected = !it.isSelected) else it }
            _todoList.value = newList
        }

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


    fun updateEndTime(time: EndTimeData) {
        viewModelScope.launch(Dispatchers.IO) {
            _endTime.value = time
            Log.e("updateEndTime", _endTime.value.toString())
            Log.e("updateEndTime", time.toString())
        }

    }

    fun updateNotificationTime(time: NotificationTimeData) {
        viewModelScope.launch(Dispatchers.IO) {
            _notificationTime.value = time
        }

    }

    suspend fun getDatabase(){
        val allTodoList = localDatabaseUseCase.getAllTodoList()
        Log.e("CreateViewModel", allTodoList.toString())

    }


    fun addDatabase(){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("CreateViewModel","addDatabase()")
            val formattedTodoList:List<com.godlife.model.todo.TodoList>
            = selectedList.value.map { com.godlife.model.todo.TodoList(it, false) }

           val data = TodoEntity(
                todoList = formattedTodoList,
                endTime = endTime.value!!,
                notificationTime = notificationTime.value!!
            )

            Log.e("CreateViewModel", data.toString())

            localDatabaseUseCase.insertTodo(data)

            Log.e("CreateViewModel", localDatabaseUseCase.getAllTodoList().toString())
        }

    }

}