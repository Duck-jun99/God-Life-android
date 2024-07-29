package com.godlife.main_page.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.model.todo.TodoList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryPageViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel() {

    private val _todoList = MutableStateFlow<List<TodoEntity>?>(emptyList())
    val todoList: StateFlow<List<TodoEntity>?> = _todoList


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _todoList.value = localDatabaseUseCase.getAllTodoList().asReversed()
        }
    }
}