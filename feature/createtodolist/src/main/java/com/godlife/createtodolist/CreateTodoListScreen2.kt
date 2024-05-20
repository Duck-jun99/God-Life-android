package com.godlife.createtodolist

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeTimeInput
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData
import java.time.LocalDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTodoListScreen2(
    navController: NavController,
    createViewModel: CreateViewModel
){

    Log.e("CreateViewModel2", createViewModel.toString())

    val selectedList by createViewModel.selectedList.collectAsState()

    //createViewModel.updateSelectedList(selectedList)

    Log.e("selectedList",selectedList.toString())

    GodLifeTheme {


        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(0.8f)
            ) {

                Text(
                    text = "오늘 목표를 마무리할 시간을 정해주세요.\n잊지 않게 알림을 보내드릴게요.",
                    style = GodLifeTypography.titleMedium
                )


                Spacer(modifier = Modifier.height(100.dp))

                Row(modifier = Modifier.padding(5.dp)){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(text = "목표 종료 시간",
                        style = TextStyle(
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()){
                    EndTimeInput(modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center),
                        createViewModel)
                }

                Spacer(modifier = Modifier.height(100.dp))

                Row(modifier = Modifier.padding(5.dp)){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(text = "알림 시간",
                        style = TextStyle(
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()){
                    NotificationTimeInput(modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center),
                        createViewModel)
                }

                Spacer(modifier = Modifier.height(100.dp))

            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)){

                GodLifeButton(onClick = {
                    navController.navigate(CreateTodoListScreen3Route.route)
                },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "완료",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndTimeInput(
    modifier: Modifier,
    createViewModel: CreateViewModel
){
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = false
    )

    GodLifeTimeInput(timePickerState = timePickerState)


    createViewModel.updateEndTime(
        EndTimeData(LocalDateTime.now().year,
            LocalDateTime.now().monthValue,
            LocalDateTime.now().dayOfMonth,
            timePickerState.hour,
            timePickerState.minute))

    Log.e("EndTimeInput", EndTimeData(LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth,
        timePickerState.hour,
        timePickerState.minute).toString())

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimeInput(
    modifier: Modifier,
    createViewModel: CreateViewModel
){
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = false
    )

    GodLifeTimeInput(timePickerState = timePickerState)

    Log.e("NotificationTimeInput", NotificationTimeData(LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth,
        timePickerState.hour,
        timePickerState.minute).toString())

    createViewModel.updateNotificationTime(
        NotificationTimeData(LocalDateTime.now().year,
            LocalDateTime.now().monthValue,
            LocalDateTime.now().dayOfMonth,
            timePickerState.hour,
            timePickerState.minute))
}



@Preview(showBackground = true)
@Composable
fun CreateTodoListScreen2Preview(){
    GodLifeTheme {


        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(0.8f)
            ) {

                Text(
                    text = "오늘 목표를 마무리할 시간을 정해주세요.\n잊지 않게 알림을 보내드릴게요.",
                    style = GodLifeTypography.titleMedium
                )


                Spacer(modifier = Modifier.height(100.dp))

                Row(modifier = Modifier.padding(5.dp)){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(text = "목표 종료 시간",
                        style = TextStyle(
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()){

                }

                Spacer(modifier = Modifier.height(100.dp))

                Row(modifier = Modifier.padding(5.dp)){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(text = "알림 시간",
                        style = TextStyle(
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()){

                }

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
                    Text(text = "완료",
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