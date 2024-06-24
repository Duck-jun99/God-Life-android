package com.godlife.setting_page

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.navigator.LoginNavigator
import com.godlife.network.model.UserInfoBody
import com.godlife.profile.navigation.ProfileEditScreenRoute

@Composable
fun SettingPageScreen(
    mainActivity: Activity,
    loginNavigator: LoginNavigator,
    navController: NavController,
    viewModel: SettingPageViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {

    }

    val logoutResult by viewModel.logoutResult.collectAsState()
    val userInfo by viewModel.userInfo.collectAsState()

    if(logoutResult == true){
        moveLoginActivity(loginNavigator, mainActivity)
    }

    GodLifeTheme {

        Column(
            modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
        ){

            Box(
                modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier.weight(0.9f)){
                        Text(text = "설정", style = GodLifeTypography.titleMedium,)
                    }

                    Box(modifier.weight(0.1f)){

                        /*
                        Icon(imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notification",
                            tint = GrayWhite,
                            modifier = modifier.align(Alignment.TopEnd))

                         */
                    }

                }
            }

            LazyColumn(
                modifier
                    .fillMaxSize()
                    .background(GrayWhite3)
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {

                item { ProfileCard(userInfo = userInfo) }

                item{ Spacer(modifier.size(12.dp)) }

                item { SelectMenu1(navController = navController) }

                item{ Spacer(modifier.size(12.dp)) }

                item { ProfileButton(imageVector = Icons.Outlined.Notifications, text = "알림 설정") }

                item{ Spacer(modifier.size(12.dp)) }

                item { ProfileButton(imageVector = Icons.Outlined.ArrowBack, text = "로그아웃", modifier = modifier.clickable { logout(viewModel) }) }

                item{ Spacer(modifier.size(12.dp)) }

                item { ProfileButton(imageVector = Icons.Outlined.Warning, text = "회원 탈퇴") }

                item{ Spacer(modifier.size(200.dp)) }

                item {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Text(text = "Version 1.0.0\nIcons by Icons8", textAlign = TextAlign.Center)
                    }
                }

            }

        }


    }

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
fun ProfileCard(
    modifier: Modifier = Modifier,
    userInfo: UserInfoBody
) {

    Log.e("ProfileCard", userInfo.nickname)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically){

            //프로필 이미지 부분
            val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
            val imageModifier: Modifier = modifier
                .size(50.dp, 50.dp)
                .clip(CircleShape)
                .fillMaxSize()
                .background(color = GrayWhite)

            Glide.with(LocalContext.current)
                .asBitmap()
                .load( if(userInfo.profileImage != "") BuildConfig.SERVER_IMAGE_DOMAIN + userInfo.profileImage else R.drawable.ic_person)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmap.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
                Image(
                    bitmap = fetchedBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = imageModifier
                )   //bitmap이 없다면
            } ?: Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier
            )

            Spacer(modifier.size(10.dp))

            Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

                //티어 보여줄 부분
                Text(text = "마스터", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                HorizontalDivider(modifier.padding(top = 10.dp, bottom = 10.dp))

                Text(text = userInfo.nickname, style = GodLifeTypography.titleMedium)
            }

        }
    }
}

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String

) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Box(modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = PurpleMain,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = GrayWhite,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

        }

    }
}

@Composable
fun SelectMenu1(
    modifier: Modifier = Modifier,
    navController: NavController){

    Box(modifier = modifier
        .fillMaxWidth()
        .height(150.dp)
        .background(shape = RoundedCornerShape(20.dp), color = Color.White),
        contentAlignment = Alignment.Center){

        Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

            //프로필 수정
            Box(
                modifier
                    .weight(0.3f)
                    .clickable { navController.navigate(ProfileEditScreenRoute.route) },
                contentAlignment = Alignment.Center){

                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.person_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "프로필 수정", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }

            }

            //갓생 점수
            Box(
                modifier
                    .weight(0.3f)
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center){

                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.graph_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "갓생 점수", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }

            }

            //레포트 요청
            Box(
                modifier
                    .weight(0.3f)
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center){

                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.report_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "레포트 요청", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }

            }

        }
    }

}



@Preview
@Composable
fun SettingPagePreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ){

            Box(
                modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier.weight(0.9f)){
                        Text(text = "설정", style = GodLifeTypography.titleMedium)
                    }

                    Box(modifier.weight(0.1f)){

                        /*
                        Icon(imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notification",
                            tint = GrayWhite,
                            modifier = modifier.align(Alignment.TopEnd))

                         */
                    }

                }
            }

            LazyColumn(
                modifier
                    .fillMaxSize()
                    .background(GrayWhite3)
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {

                item { ProfileCardPreview() }

                item{ Spacer(modifier.size(12.dp)) }

                item { SelectMenu1Preview() }

                item{ Spacer(modifier.size(12.dp)) }

                item { ProfileButton(imageVector = Icons.Outlined.Notifications, text = "알림 설정") }

                item{ Spacer(modifier.size(12.dp)) }

                item { ProfileButton(imageVector = Icons.Outlined.ArrowBack, text = "로그아웃") }

                item{ Spacer(modifier.size(12.dp)) }

                item { ProfileButton(imageVector = Icons.Outlined.Warning, text = "회원 탈퇴") }

                item{ Spacer(modifier.size(200.dp)) }

                item {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Text(text = "Version 1.0.0\nIcons by Icons8", textAlign = TextAlign.Center)
                    }
                }


            }

        }


    }
}

@Preview
@Composable
fun ProfileCardPreview(
    modifier: Modifier = Modifier,
    //@DrawableRes iconResId: Int,
    text: String = "TEXT"

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically){
            //프로필 이미지 부분
            Box(
                modifier
                    .size(100.dp)
                    .background(Color.Gray)){
                Text(text = "Image")
            }

            Spacer(modifier.size(10.dp))

            Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

                //티어 보여줄 부분
                Text(text = "마스터", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                HorizontalDivider(modifier.padding(top = 10.dp, bottom = 10.dp))

                Text(text = "Nickname", style = GodLifeTypography.titleMedium)
            }

        }
    }
}

@Preview
@Composable
fun ProfileButtonPreview(
    modifier: Modifier = Modifier,
    //@DrawableRes iconResId: Int,
    text: String = "TEXT"

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Box(modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = PurpleMain,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = PurpleMain,
                    style = TextStyle(fontSize = 20.sp)
                )
            }

        }

    }
}

@Preview
@Composable
fun SelectMenu1Preview(
    modifier: Modifier = Modifier){

    Box(modifier = modifier
        .fillMaxWidth()
        .height(150.dp)
        .background(shape = RoundedCornerShape(20.dp), color = Color.White),
        contentAlignment = Alignment.Center){

        Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

            //프로필 수정
            Box(
                modifier
                    .weight(0.3f)
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center){

                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.person_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "프로필 수정", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }

            }

            //갓생 점수
            Box(
                modifier
                    .weight(0.3f)
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center){

                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.graph_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "갓생 점수", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }

            }

            //레포트 요청
            Box(
                modifier
                    .weight(0.3f)
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center){

                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.report_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "레포트 요청", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }

            }

        }
    }

}

@Preview
@Composable
fun SelectMenu2(modifier: Modifier = Modifier){
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

        Box(modifier.weight(0.3f), contentAlignment = Alignment.Center){

            Box(
                modifier = modifier
                    .width(120.dp)
                    .height(150.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.person_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "프로필 수정", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }
            }

        }

        Box(modifier.weight(0.3f), contentAlignment = Alignment.Center){

            Box(
                modifier = modifier
                    .width(120.dp)
                    .height(150.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.graph_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "갓생 점수", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }
            }

        }

        Box(modifier.weight(0.3f), contentAlignment = Alignment.Center){

            Box(
                modifier = modifier
                    .width(120.dp)
                    .height(150.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Icon(painter = painterResource(id = R.drawable.report_icons8), contentDescription = "", tint = Color.Unspecified, modifier = modifier.size(70.dp))

                    }

                    HorizontalDivider(modifier.padding(10.dp))

                    Text(text = "레포트 요청", style = TextStyle(color = GrayWhite), textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
                }
            }

        }



    }

}
