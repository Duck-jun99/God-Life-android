package com.godlife.login

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.login.login_manager.KakaoLoginManager


@Composable
fun LoginScreen(
    context: Context,
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()){

    val kakaoLoginManager: KakaoLoginManager = KakaoLoginManager(LocalContext.current, loginViewModel)


    GodLifeTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text("Login")

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = { kakaoLoginManager.startKakaoLogin { context } },
                colors = ButtonDefaults.buttonColors(PurpleMain)
            ) {
                Text("KaKao Login", style = TextStyle(color = Color.White))
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
    GodLifeTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text("Login")

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {

            }, colors = ButtonDefaults.buttonColors(PurpleMain)) {
                Text("KaKao Login", style = TextStyle(color = Color.White))
            }


            Button(onClick = {

            }, colors = ButtonDefaults.buttonColors(PurpleMain)){
                Text("회원가입 테스트", style = TextStyle(color = Color.White))
            }

        }
    }
}