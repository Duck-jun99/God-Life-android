package com.godlife.createtodolist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTodoListScreen3(
    createViewModel: CreateViewModel = hiltViewModel()
){
    GodLifeTheme{

        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {

            Text(
                text = "오늘의 목표 설정이 완료되었어요.\n보람찬 하루를 시작해봐요!",
                style = GodLifeTypography.titleMedium
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CreateTodoListScreen3Preview(){
    GodLifeTheme{

        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {

            Text(
                text = "오늘의 목표 설정이 완료되었어요.\n보람찬 하루를 시작해봐요!",
                style = GodLifeTypography.titleMedium
            )
        }

    }
}