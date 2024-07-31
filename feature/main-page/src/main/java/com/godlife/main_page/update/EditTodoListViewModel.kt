package com.godlife.main_page.update

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.model.todo.TodoList
import com.godlife.createtodolist.model.TodoListForm
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.main_page.MainPageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTodoListViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel() {

    // 오늘 투두리스트 불러온 플래그
    val getTodayTodoListFlag = mutableStateOf<Boolean>(false)

    // 수정 완료 플래그
    val updateCompleteFlag = mutableStateOf<Boolean>(false)

    private val _totalTodoList = MutableStateFlow(com.godlife.createtodolist.model.TodoList().getTodoList())
    val totalTodoList: StateFlow<List<TodoListForm>> = _totalTodoList

    //오늘 투두 정보
    private val _todayTodoInfo = MutableStateFlow<TodoEntity?>(null)
    val todayTodoInfo: StateFlow<TodoEntity?> = _todayTodoInfo

    // 오늘 투두리스트
    private val _todayTodoList = MutableStateFlow<List<TodoList>>(emptyList())
    val todayTodoList: StateFlow<List<TodoList>> = _todayTodoList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getTodayTodoList()

            setTotalTodoList()

        }
    }

    //오늘 설정한 투두 리스트를 로컬 데이터베이스에서 가져오기
    private suspend fun getTodayTodoList(){

        if(!getTodayTodoListFlag.value){
            getTodayTodoListFlag.value = true

            _todayTodoInfo.value = localDatabaseUseCase.getTodayTodoList()
            _todayTodoList.value = todayTodoInfo.value!!.todoList

        }

    }

    //오늘 설정한 투두 리스트와 전체 투두 리스트 동기화 (오늘 설정한 투두 리스트는 selected로 변경)
    private suspend fun setTotalTodoList() {
        val updatedList = _totalTodoList.value.map { todoListForm ->
            if (_todayTodoList.value.any { it.name == todoListForm.name }) {
                todoListForm.copy(isSelected = true)
            } else {
                todoListForm
            }
        }
        _totalTodoList.value = updatedList

        _todayTodoList.value = emptyList()
    }

    //투두 리스트 선택 상태 변경
    fun updateSelected(todo: TodoListForm){
        _totalTodoList.value = _totalTodoList.value.map { if (it == todo) it.copy(isSelected = !it.isSelected) else it }
    }

    // 수정 완료
    fun completeUpdate(
        mainPageViewModel: MainPageViewModel,
        onUpdateComplete: () -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {

            launch {
                updateTodo()
            }.join()

            launch {
                mainPageViewModel.setUpdateAlertDialogFlag()
                onUpdateComplete()
                onCleared()
            }

        }

    }

    //오늘 투두 업데이트
    private suspend fun updateTodo() {
        val updatedTodayTodoList = _todayTodoList.value.toMutableList()

        totalTodoList.value.forEach { todoListForm ->
            // TODO
            if (todoListForm.isSelected && !updatedTodayTodoList.any { it.name == todoListForm.name }) {
                updatedTodayTodoList.add(TodoList(name = todoListForm.name, iscompleted = false))
            }
        }

        _todayTodoList.value = updatedTodayTodoList

        todayTodoInfo.value?.let { currentInfo ->
            val updatedInfo = currentInfo.copy(
                todoList = updatedTodayTodoList,
                isCompleted = false
            )
            _todayTodoInfo.value = updatedInfo
            localDatabaseUseCase.updateTodoList(updatedInfo)
        }
    }

    fun setCleared(){
        onCleared()
    }


    override fun onCleared() {
        Log.e("EditTodoListViewModel", "onCleared")
        super.onCleared()
    }


}