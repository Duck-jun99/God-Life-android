package com.godlife.main_page.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.godlife.main_page.MainPageViewModel

@Composable
fun UpdateAlertDialog(
    viewModel: MainPageViewModel,
    onUpdateComplete: () -> Unit,
    todoId: Int
){
    val updateCategory = viewModel.updateCategory.collectAsState().value

    if(updateCategory == "EDIT_TODOLIST"){
        EditTodoListContent(
            mainPageViewModel = viewModel,
            onUpdateComplete = onUpdateComplete
        )
    }
    else if(updateCategory == "EDIT_NOTIFICATION_TIME"){
        EditAlarmContent(
            mainPageViewModel = viewModel,
            onUpdateComplete = onUpdateComplete
        )
    }
    else if(updateCategory == "DELETE"){
        DeleteTodoContent(
            mainPageViewModel = viewModel,
            onUpdateComplete = onUpdateComplete,
            todoId = todoId
        )
    }
}

