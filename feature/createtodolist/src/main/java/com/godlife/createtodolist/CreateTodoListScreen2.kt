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
import com.godlife.designsystem.GodLifeTheme
import com.godlife.designsystem.GreyWhite
import com.godlife.designsystem.PurpleMain

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

            Text(text = "PM 10:00",
                style = TextStyle(
                    color = GreyWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center
            )

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

            Text(text = "PM 09:00",
                style = TextStyle(
                    color = GreyWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center
            )

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

            Text(text = "PM 10:00",
                style = TextStyle(
                    color = GreyWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center
            )

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

            Text(text = "PM 09:00",
                style = TextStyle(
                    color = GreyWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
                , textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))


        }

    }
}