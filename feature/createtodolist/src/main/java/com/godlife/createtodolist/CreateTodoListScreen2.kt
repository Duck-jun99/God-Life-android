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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeTimeInput
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.todo.NotificationTimeData
import java.time.LocalDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTodoListScreen2(
    navController: NavController,
    createViewModel: CreateTodoListViewModel
){

    Log.e("CreateViewModel2", createViewModel.toString())

    val selectedList by createViewModel.selectedList.collectAsState()

    val notificationSwitchState by createViewModel.notificationSwitchState.collectAsState()

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
                    text = "알림을 설정하시면\n잊지 않게 알림을 보내드릴게요.",
                    style = GodLifeTypography.titleMedium
                )


                Spacer(modifier = Modifier.height(100.dp))

                Row(modifier = Modifier.padding(5.dp)){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(text = "알림 설정",
                        style = TextStyle(
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()){
                    Switch(
                        checked = notificationSwitchState, onCheckedChange = { createViewModel.updateNotificationSwitchState() }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))

                if(notificationSwitchState){

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
fun NotificationTimeInput(
    modifier: Modifier,
    createViewModel: CreateTodoListViewModel
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
                    text = "알림을 설정하시면\n잊지 않게 알림을 보내드릴게요.",
                    style = GodLifeTypography.titleMedium
                )


                Spacer(modifier = Modifier.height(100.dp))

                Row(modifier = Modifier.padding(5.dp)){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(
                        text = "알림 설정",
                        style = TextStyle(
                            color = PurpleMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                val switchState = remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ){
                    Switch(
                        checked = switchState.value, onCheckedChange = {switchState.value = !switchState.value}
                    )
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

                NotificationTimeInputPreview()

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NotificationTimeInputPreview(
    modifier: Modifier = Modifier,
){

    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = false
    )

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){

        GodLifeTimeInput(timePickerState = timePickerState)
    }





}
