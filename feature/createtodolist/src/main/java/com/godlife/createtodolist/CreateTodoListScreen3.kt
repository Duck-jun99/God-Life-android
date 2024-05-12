package com.godlife.createtodolist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.CleanArchitectureTheme
import com.godlife.designsystem.GreyWhite
import com.godlife.designsystem.PurpleMain

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTodoListScreen3(
    createViewModel: CreateViewModel = hiltViewModel()
){
    CleanArchitectureTheme{

        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {

            Text(
                text = "오늘의 목표 설정이 완료되었어요.\n보람찬 하루를 시작해봐요!",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreyWhite
                )
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CreateTodoListScreen3Preview(){
    CleanArchitectureTheme{

        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {

            Text(
                text = "오늘의 목표 설정이 완료되었어요.\n보람찬 하루를 시작해봐요!",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreyWhite
                )
            )
        }

    }
}