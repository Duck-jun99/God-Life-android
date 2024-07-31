package com.godlife.main_page.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.main_page.MainPageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteTodoViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel() {

    val dbCompleteFlag = mutableStateOf<Boolean>(false)

    private suspend fun deleteTodo(todoId: Int){
        localDatabaseUseCase.deleteTodoList(todoId)
    }

    fun completeDeleteTodo(
        todoId: Int,
        mainPageViewModel: MainPageViewModel,
        onUpdateComplete: () -> Unit
    ){
        if(!dbCompleteFlag.value){
            dbCompleteFlag.value = true
            viewModelScope.launch(Dispatchers.IO) {
                deleteTodo(todoId)

                mainPageViewModel.setUpdateAlertDialogFlag()
                onUpdateComplete()
                onCleared()

            }
        }

    }

    fun setCleared(){
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
    }
}