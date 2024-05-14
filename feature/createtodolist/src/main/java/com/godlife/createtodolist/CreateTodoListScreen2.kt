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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.godlife.designsystem.component.GodLifeButton
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
    createViewModel: CreateViewModel = hiltViewModel()
){
    GodLifeTheme{

        Column(
            modifier = Modifier
                .padding(20.dp),
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
                    .align(Alignment.Center))
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
                    .align(Alignment.Center))
            }

            Spacer(modifier = Modifier.height(100.dp))

        }

    }
}

@Preview(showBackground = true)
@Composable
fun CreateTodoListScreen2Preview(){
    GodLifeTheme{

        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {

            Text(
                text = "오늘 목표를 마무리할 시간을 정해주세요.\n잊지 않게 알림을 보내드릴게요.",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreyWhite
                )
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
                    .align(Alignment.Center))
            }


/*
            Text(text = "PM 10:00",
                style = TextStyle(
                    color = GreyWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center
            )

 */

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
                    .align(Alignment.Center))
            }

            /*
            Text(text = "PM 09:00",
                style = TextStyle(
                    color = GreyWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center
            )

             */

            Spacer(modifier = Modifier.height(100.dp))


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndTimeInput(
    modifier: Modifier,
    createViewModel: CreateViewModel = hiltViewModel()
){
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = false
    )

    CustomTimeInput(timePickerState = timePickerState)


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
    createViewModel: CreateViewModel = hiltViewModel()
){
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = false
    )

    CustomTimeInput(timePickerState = timePickerState)

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimeInput(
    timePickerState: TimePickerState
){
    TimeInput(
        state = timePickerState,
        modifier = Modifier.padding(top = 10.dp),
        colors = TimePickerDefaults.colors(
            timeSelectorSelectedContainerColor = Color.White,
            timeSelectorSelectedContentColor = PurpleMain,
            timeSelectorUnselectedContainerColor = Color.White,
            timeSelectorUnselectedContentColor = GreyWhite,

            periodSelectorBorderColor = PurpleMain,
            periodSelectorSelectedContainerColor = PurpleMain,
            periodSelectorSelectedContentColor = Color.White,
            periodSelectorUnselectedContainerColor = Color.White,
            periodSelectorUnselectedContentColor = GreyWhite,

            containerColor = PurpleMain,
            selectorColor = PurpleMain,
            clockDialColor = PurpleMain,
            clockDialSelectedContentColor = PurpleMain,
            clockDialUnselectedContentColor = Color.White,

        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CustomTimeInputPreview(){
    val timePickerState = rememberTimePickerState(
        initialHour = 17,
        initialMinute = 30,
        is24Hour = false
    )

    TimeInput(
        state = timePickerState,
        modifier = Modifier.padding(top = 10.dp),
        colors = TimePickerDefaults.colors(
            timeSelectorSelectedContainerColor = Color.White,
            timeSelectorSelectedContentColor = PurpleMain,
            timeSelectorUnselectedContainerColor = Color.White,
            timeSelectorUnselectedContentColor = GreyWhite,

            periodSelectorBorderColor = PurpleMain,
            periodSelectorSelectedContainerColor = PurpleMain,
            periodSelectorSelectedContentColor = Color.White,
            periodSelectorUnselectedContainerColor = Color.White,
            periodSelectorUnselectedContentColor = GreyWhite,

            containerColor = PurpleMain,
            selectorColor = PurpleMain,
            clockDialColor = PurpleMain,
            clockDialSelectedContentColor = PurpleMain,
            clockDialUnselectedContentColor = Color.White,


        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TimeInputPreview(){
    val timePickerState = rememberTimePickerState(
        initialHour = 17,
        initialMinute = 30,
        is24Hour = false
    )

    TimeInput(
        state = timePickerState,
        modifier = Modifier.padding(top = 10.dp),
        colors = TimePickerDefaults.colors(Color.Black)
    )
}