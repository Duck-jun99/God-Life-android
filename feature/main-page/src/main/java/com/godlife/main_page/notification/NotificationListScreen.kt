package com.godlife.main_page.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OrangeLight
import com.godlife.main_page.R
import com.godlife.network.model.NotificationListBody

@Composable
fun NotificationListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NotificationListViewModel = hiltViewModel()
) {

    val notificationList = viewModel.notificationList.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = GrayWhite3)
            .statusBarsPadding()
    ){

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = "알림",
            style = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            )
        )

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {

            item{
                notificationList.forEach {
                    when(it.read){
                        true ->
                            ReadNotificationItem(
                                item = it,
                                navController = navController
                            )
                        false ->
                            NoReadNotificationItem(
                                item = it,
                                navController = navController,
                                viewModel = viewModel
                            )
                    }
                }
            }

        }


    }
}

@Composable
fun NoReadNotificationItem(
    modifier: Modifier = Modifier,
    item: NotificationListBody,
    viewModel: NotificationListViewModel,
    navController: NavController
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = Color.White)
            .clickable {
                viewModel.patchNotificationRead(item.alarmId)
                if(item.type == "normal"){
                    navController.navigate("PostDetailScreen/${item.boardId}"){
                        launchSingleTop = true
                    }
                }
                else if(item.type == "stimulus"){
                    navController.navigate("StimulusDetailScreen/${item.boardId}"){
                        launchSingleTop = true
                    }
                }


            }
            .padding(10.dp)
    ) {

        Box(
            modifier = modifier
                .size(70.dp)
                .align(Alignment.CenterVertically)
        ){

            Image(
                painter = painterResource(R.drawable.goodlife_logo),
                contentDescription = "",
                modifier = modifier
                    .fillMaxSize()
            )

        }

        Spacer(modifier.width(20.dp))

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            Text(
                text = item.title,
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier.height(10.dp))

            Text(
                text = item.content,
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )

        }

    }
}

@Composable
fun ReadNotificationItem(
    modifier: Modifier = Modifier,
    item: NotificationListBody,
    navController: NavController
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = OrangeLight)
            .clickable {
                navController.navigate("PostDetailScreen/${item.boardId}"){
                    launchSingleTop = true
                }
            }
            .padding(10.dp)
    ) {

        Box(
            modifier = modifier
                .size(70.dp)
                .align(Alignment.CenterVertically)
        ){

            Image(
                painter = painterResource(R.drawable.goodlife_logo),
                contentDescription = "",
                modifier = modifier
                    .fillMaxSize()
            )

        }

        Spacer(modifier.width(20.dp))

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            Text(
                text = item.title,
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier.height(10.dp))

            Text(
                text = item.content,
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )

        }

    }
}


@Preview
@Composable
fun NotificationListScreenPreview(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = GrayWhite3)
            .statusBarsPadding()
    ){

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = "알림",
            style = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            )
        )



    }
}

@Preview
@Composable
fun NoReadNotificationItemPreview(
    modifier: Modifier = Modifier
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = Color.White)
            .padding(10.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.moon_icon8),
            contentDescription = "",
            modifier = modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier.width(10.dp))

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            Text(
                text = "제목",
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier.height(10.dp))

            Text(
                text = "내용",
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            )

        }

    }
}

@Preview
@Composable
fun ReadNotificationItemPreview(
    modifier: Modifier = Modifier
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = OrangeLight)
            .padding(10.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.moon_icon8),
            contentDescription = "",
            modifier = modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier.width(10.dp))

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            Text(
                text = "제목",
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier.height(10.dp))

            Text(
                text = "내용",
                style = TextStyle(
                    color = GrayWhite,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            )

        }

    }
}