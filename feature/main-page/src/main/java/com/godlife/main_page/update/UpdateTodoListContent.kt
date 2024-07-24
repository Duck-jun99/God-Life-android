package com.godlife.main_page.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.createtodolist.R
import com.godlife.createtodolist.model.TodoListForm
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.CheckColor
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.MainPageViewModel

@Composable
fun UpdateAlertDialog(
    viewModel: MainPageViewModel,
    onUpdateComplete: () -> Unit
){
    val updateCategory = viewModel.updateCategory.collectAsState().value

    if(updateCategory == "EDIT_TODOLIST"){
        EditTodoList(
            mainPageViewModel = viewModel,
            onUpdateComplete = onUpdateComplete
        )
    }
    else if(updateCategory == "EDIT_NOTIFICATION_TIME"){

    }
    else if(updateCategory == "DELETE"){

    }
}

@Composable
fun EditTodoList(
    modifier: Modifier = Modifier,
    mainPageViewModel: MainPageViewModel,
    onUpdateComplete: () -> Unit,
    viewModel: EditTodoListViewModel = hiltViewModel()
){
    val totalTodoList by viewModel.totalTodoList.collectAsState()

    AlertDialog(
        containerColor = GrayWhite3,
        onDismissRequest = { mainPageViewModel.setUpdateAlertDialogFlag() },
        title = {
            Text(text = "투두 리스트 수정", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier
            ) {

                items(totalTodoList.size){
                    TodoListItem(
                        item = totalTodoList[it],
                        viewModel = viewModel
                    )
                }

            }
        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {

                    viewModel.completeUpdate(
                        mainPageViewModel,
                        onUpdateComplete
                    )
                    //mainPageViewModel.setUpdateAlertDialogFlag()
                    //onUpdateComplete()
                          },
                text = { Text(text = "완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
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

@Composable
fun TodoListItem(
    modifier: Modifier = Modifier,
    item: TodoListForm,
    viewModel: EditTodoListViewModel
){

    Column(
        modifier = Modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    viewModel.updateSelected(todo = item)
                }
        ){
            Image(
                modifier = modifier
                    .padding(5.dp),
                painter = painterResource(id = item.imgId),
                contentDescription = item.description
            )

            if(item.isSelected){
                Box(
                    modifier = modifier
                        .matchParentSize()
                        .background(
                            color = CheckColor,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }


        }

        Text(text = item.name)
    }

}

@Preview(showBackground = true)
@Composable
fun EditTodoListPreview(
    modifier: Modifier = Modifier
){
    AlertDialog(
        containerColor = GrayWhite3,
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(text = "투두 리스트 수정", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier
            ) {

                items(15){
                    TodoListItemPreview()
                }

            }
        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = { /*TODO*/ },
                text = { Text(text = "완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
                        },

        dismissButton = {
            GodLifeButtonWhite(
                onClick = { /*TODO*/ },
                text = { Text(text = "취소", style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
        }
    )


}
@Preview(showBackground = true)
@Composable
fun TodoListItemPreview(
    modifier: Modifier = Modifier,
    selected: Boolean = false
){

    Column(
        modifier = Modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
        ){
            Image(
                modifier = modifier
                    .padding(5.dp),
                painter = painterResource(id = R.drawable.carbon_clean),
                contentDescription = ""
            )

            if(selected){
                Box(
                    modifier = modifier
                        .matchParentSize()
                        .background(
                            color = CheckColor,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }


        }

        Text(text = "dasdas")
    }

}