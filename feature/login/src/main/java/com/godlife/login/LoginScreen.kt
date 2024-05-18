package com.godlife.login

import android.content.Context
import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.login.login_manager.KakaoLoginManager
import com.godlife.navigator.MainNavigator
import kotlinx.coroutines.flow.first


@Composable
fun LoginScreen(
    context: Context,
    navController: NavController,
    mainNavigator:MainNavigator,
    loginActivity: LoginActivity,
    loginViewModel: LoginViewModel = hiltViewModel()){

    val kakaoLoginManager: KakaoLoginManager = KakaoLoginManager(LocalContext.current, loginViewModel, navController, mainNavigator, loginActivity)


    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text("God Life\nGood Life", style = TextStyle(
                color = Color.White,
                fontSize = 35.sp,
                fontStyle = FontStyle.Italic
            ))

            Spacer(modifier = Modifier.height(30.dp))

            IconButton(onClick = { kakaoLoginManager.startKakaoLogin { context } },
                modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.kakao_login_large_narrow),
                    contentDescription = "KAKAO_LOGIN")
            }

            Button(onClick = { navController.navigate(SignUpScreenRoute.route) },
                colors = ButtonDefaults.buttonColors(PurpleMain)
            ){
                Text("회원가입 테스트", style = TextStyle(color = Color.White))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    GodLifeTheme(
        modifier = Modifier.background(PurpleMain)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text("God Life\nGood Life", style = TextStyle(
                color = Color.White,
                fontSize = 35.sp,
                fontStyle = FontStyle.Italic
            ))

            Spacer(modifier = Modifier.height(30.dp))

            IconButton(onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.kakao_login_large_narrow),
                    contentDescription = "KAKAO_LOGIN")
            }

            Button(onClick = {

            }, colors = ButtonDefaults.buttonColors(PurpleMain)){
                Text("회원가입 테스트", style = TextStyle(color = Color.White))
            }

        }
    }
}

private fun moveMainActivity(mainNavigator: MainNavigator, loginActivity: LoginActivity){
    //val intent = Intent(context, MainActivity::class.java)
    //ContextCompat.startActivity(context, intent, null)
    mainNavigator.navigateFrom(
        activity = loginActivity,
        withFinish = true
    )

}