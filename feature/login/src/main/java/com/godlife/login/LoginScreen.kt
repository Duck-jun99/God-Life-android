package com.godlife.login

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.designsystem.theme.GodLifeTheme
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

            Button(onClick = {
                kakaoLoginManager.startKakaoLogin { context }

            }) {
                Text("KaKao Login")
            }

            Button(onClick = {
                navController.navigate(SignUpScreenRoute.route)
            }){
                Text("회원가입 테스트")
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

            }) {
                Text("KaKao Login")
            }


            Button(onClick = {
            }){
                Text("회원가입 테스트")
            }

        }
    }
}