package com.godlife.setting_page

import android.app.Activity
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite2
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.navigator.LoginNavigator

@Composable
fun SettingPageScreen(
    mainActivity: Activity,
    loginNavigator: LoginNavigator,
    viewModel: SettingPageViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {

    }

    val logoutResult by viewModel.logoutResult.collectAsState()

    if(logoutResult == true){
        moveLoginActivity(loginNavigator, mainActivity)
    }

    GodLifeTheme {
        LazyColumn(
            modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            item {
                Surface(shadowElevation = 7.dp) {
                    Box(
                        modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            //프로필 이미지 부분
                            Box(
                                modifier
                                    .size(50.dp)
                                    .background(Color.Gray)){
                                Text(text = "Image")
                            }

                            Spacer(modifier.size(10.dp))

                            Text(text = "Nickname", style = GodLifeTypography.titleMedium)

                            Spacer(modifier.size(10.dp))

                            //티어 보여줄 부분
                            Text(text = "마스터", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                        }

                    }
                }
            }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.Person, text = "프로필 수정") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.ThumbUp, text = "갓생 점수 자세히 보기") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.Notifications, text = "알림 설정") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.ArrowBack, text = "로그아웃", modifier = modifier.clickable { logout(viewModel) }) }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.Warning, text = "회원 탈퇴") }


        }
    }

    /*
    GodLifeTheme {



        Column(modifier = Modifier.fillMaxSize()) {


            GodLifeButton(onClick = {
                logout(viewModel)
            }) {
                Text(text = "로그아웃", style = TextStyle(color = Color.White))
            }



        }


    }

     */


}

private fun logout(viewModel: SettingPageViewModel){

    viewModel.logout()

}

private fun moveLoginActivity(loginNavigator: LoginNavigator, mainActivity: Activity){

    Log.e("moveLoginActivity", "moveLoginActivity")

    loginNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = true
    )
}

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String

) {
    Card(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = PurpleMain,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = PurpleMain,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
fun SettingPagePreview(modifier: Modifier = Modifier){
    GodLifeTheme {
        LazyColumn(
            modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            item {
                Surface(shadowElevation = 7.dp) {
                    Box(
                        modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            //프로필 이미지 부분
                            Box(
                                modifier
                                    .size(50.dp)
                                    .background(Color.Gray)){
                                Text(text = "Image")
                            }

                            Spacer(modifier.size(10.dp))

                            Text(text = "Nickname", style = GodLifeTypography.titleMedium)

                            Spacer(modifier.size(10.dp))

                            //티어 보여줄 부분
                            Text(text = "마스터", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                        }

                    }
                }
            }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.Person, text = "프로필 수정") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.ThumbUp, text = "갓생 점수 자세히 보기") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.Notifications, text = "알림 설정") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.ArrowBack, text = "로그아웃") }

            item{ Spacer(modifier.size(12.dp)) }

            item { ProfileButton(imageVector = Icons.Outlined.Warning, text = "회원 탈퇴") }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileButtonPreview(
    modifier: Modifier = Modifier,
    //@DrawableRes iconResId: Int,
    text: String = "TEXT"

) {
    Card(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                tint = PurpleMain,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = PurpleMain,
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}

