package com.godlife.main_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel(){

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
    fun todayTimeText(nickname: String): String {
        val hour = LocalDateTime.now().hour

        return when (hour) {
            in 6..8 -> "아침부터 부지런하시군요!"
            in 9 .. 11 -> "활기찬 오전 보내세요!"
            in 12..18 -> "오후도 화이팅이에요!"
            in 19..23 -> "저녁에도 노력하시는 모습이 멋있어요!"
            in 0..5 -> "${nickname}님이 어두운 새벽을 빚내주고 있어요."
            else -> "화이팅이에요 항상!" // 예외 상황 처리
        }
    }
}