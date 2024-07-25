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

