package com.godlife.main_page.update

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.createtodolist.NotificationTimeInputPreview
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTimeInput
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.MainPageViewModel
import com.godlife.model.todo.NotificationTimeData
import java.time.LocalDateTime

@Composable
fun EditAlarmContent(
    modifier: Modifier = Modifier,
    mainPageViewModel: MainPageViewModel,
    onUpdateComplete: () -> Unit,
    viewModel: EditAlarmViewModel = hiltViewModel()
){
    val notificationFlag = viewModel.notificationFlag.collectAsState()

    val context = LocalContext.current

    AlertDialog(
        containerColor = GrayWhite3,
        onDismissRequest = {
            mainPageViewModel.setUpdateAlertDialogFlag()
            viewModel.setCleared()
        },
        title = {
            Text(text = "알림 시간 수정", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            Column {
                Row(
                    modifier = modifier
                ){

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        tint = PurpleMain
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


                Switch(
                    checked = notificationFlag.value,
                    onCheckedChange = {viewModel.setnotificationFlag() }
                )

                Spacer(modifier = Modifier.height(10.dp))

                if(notificationFlag.value){
                    Row(
                        modifier = Modifier
                    ){

                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            tint = PurpleMain
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

                    NotificationTimeInput(
                        viewModel = viewModel
                    )
                }


            }
        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {
                    if(notificationFlag.value){
                        if(viewModel.checkFunc()){
                            viewModel.completeNotification(
                                mainPageViewModel, onUpdateComplete
                            )
                        }
                        else{
                            Toast.makeText(context, "현재 시간보다 이전 시간은 불가능해요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        viewModel.completeNotification(
                            mainPageViewModel, onUpdateComplete
                        )
                    }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimeInput(
    viewModel: EditAlarmViewModel
){
    val hour = viewModel.notificationTime.collectAsState().value.hour
    val minute = viewModel.notificationTime.collectAsState().value.minute

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = false
    )

    GodLifeTimeInput(timePickerState = timePickerState)

    Log.e("NotificationTimeInput", NotificationTimeData(
        LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth,
        timePickerState.hour,
        timePickerState.minute).toString())


    viewModel.updateNotificationTime(
        NotificationTimeData(
            LocalDateTime.now().year,
            LocalDateTime.now().monthValue,
            LocalDateTime.now().dayOfMonth,
            timePickerState.hour,
            timePickerState.minute)
    )


}


@Preview
@Composable
fun EditAlarmContentPreview(
    modifier: Modifier = Modifier
){
    val switchState = remember { mutableStateOf(true) }

    AlertDialog(
        containerColor = GrayWhite3,
        onDismissRequest = {  },
        title = {
            Text(text = "알림 시간 수정", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
               Column {
                   Row(
                       modifier = modifier
                   ){

                       Icon(
                           imageVector = Icons.Outlined.Notifications,
                           contentDescription = null,
                           modifier = Modifier.align(Alignment.CenterVertically),
                           tint = PurpleMain
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


                   Switch(
                       checked = switchState.value, onCheckedChange = {switchState.value = !switchState.value}
                   )

                   Spacer(modifier = Modifier.height(10.dp))

                   if(switchState.value){
                       Row(
                           modifier = Modifier
                       ){

                           Icon(
                               imageVector = Icons.Outlined.DateRange,
                               contentDescription = null,
                               modifier = Modifier.align(Alignment.CenterVertically),
                               tint = PurpleMain
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
                   }


               }
        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {

                },
                text = { Text(text = "완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
        },

        dismissButton = {
            GodLifeButtonWhite(
                onClick = {

                },
                text = { Text(text = "취소", style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)) })
        }
    )
}