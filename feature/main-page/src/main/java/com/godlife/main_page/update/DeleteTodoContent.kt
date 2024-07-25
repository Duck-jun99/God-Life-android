package com.godlife.main_page.update

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.MainPageViewModel

@Composable
fun DeleteTodoContent(
    modifier: Modifier = Modifier,
    mainPageViewModel: MainPageViewModel,
    onUpdateComplete: () -> Unit,
    todoId: Int,
    viewModel: DeleteTodoViewModel = hiltViewModel()
){
    AlertDialog(
        containerColor = GrayWhite3,
        onDismissRequest = {
            mainPageViewModel.setUpdateAlertDialogFlag()
            viewModel.setCleared()
                           },
        title = {
            Text(text = "오늘 투두 삭제하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            Text(text = "오늘의 투두를 삭제할까요?", style = TextStyle(color = GrayWhite, fontSize = 16.sp, fontWeight = FontWeight.Normal))
        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {
                          viewModel.completeDeleteTodo(
                              todoId, mainPageViewModel, onUpdateComplete
                          )
                },
                text = { Text(text = "삭제", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
        },

        dismissButton = {
            GodLifeButtonWhite(
                onClick = {
                    mainPageViewModel.setUpdateAlertDialogFlag()
                    viewModel.setCleared()
                },
                text = { Text(text = "취소", style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
        }
    )
}