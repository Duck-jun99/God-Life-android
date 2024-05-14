package com.godlife.createtodolist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.createtodolist.util.AnimatedPreLoader
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.navigator.MainNavigator
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTodoListScreen3(
    navController: NavController,
    createActivity: CreateActivity,
    mainNavigator: MainNavigator,
    createViewModel: CreateViewModel
){

    val showDoneUI = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(5000) // 5초 지연
        showDoneUI.value = true
    }

    if (showDoneUI.value) {
        DoneLodingUI(
            navController = navController,
            createActivity = createActivity,
            mainNavigator = mainNavigator,
            createViewModel = createViewModel
        )
    } else {
        LoadingUI()
    }

}

@Preview(showBackground = true)
@Composable
fun LoadingUI(){
    GodLifeTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                AnimatedPreLoader(modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(10.dp))

                Text(text = "투두 리스트를 만들고 있어요.", style = GodLifeTypography.bodyLarge)
            }

        }
    }
}

@Composable
fun DoneLodingUI(
    navController: NavController,
    createActivity: CreateActivity,
    mainNavigator: MainNavigator,
    createViewModel: CreateViewModel
){
    GodLifeTheme {

        val selectedList by createViewModel.selectedList.collectAsState()
        val endTime by createViewModel.endTime.collectAsState()
        val notificationTime by createViewModel.notificationTime.collectAsState()

        Log.e("CreateTodoListScreen3", createViewModel.toString())
        Log.e("CreateTodoListScreen3", selectedList.toString())
        Log.e("CreateTodoListScreen3", endTime.toString())
        Log.e("CreateTodoListScreen3", notificationTime.toString())


        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(0.8f)
            ) {

                Text(
                    text = "오늘의 목표 설정이 완료되었어요.\n보람찬 하루를 시작해봐요!",
                    style = GodLifeTypography.titleMedium
                )

                Spacer(modifier = Modifier.height(100.dp))

            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)){

                GodLifeButton(onClick = {
                    moveMainActivity(mainNavigator, createActivity)
                },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "갓생 시작",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTodoListScreen3Preview(){
    GodLifeTheme {

        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(0.8f)
            ) {

                Text(
                    text = "오늘의 목표 설정이 완료되었어요.\n보람찬 하루를 시작해봐요!",
                    style = GodLifeTypography.titleMedium
                )

                Spacer(modifier = Modifier.height(100.dp))

            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)){

                GodLifeButton(onClick = {},
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "갓생 시작",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

private fun moveMainActivity(mainNavigator: MainNavigator, createActivity: CreateActivity){
    //val intent = Intent(context, MainActivity::class.java)
    //ContextCompat.startActivity(context, intent, null)
    mainNavigator.navigateFrom(
        activity = createActivity,
        withFinish = true
    )
}
