package com.godlife.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.CleanArchitectureTheme
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.login.social_login_manager.KakaoLoginManager
import com.godlife.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


private lateinit var autoLoginState:AutoLoginConstant
private fun autoLogin(content: View, context: Context) {

    content.viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (::autoLoginState.isInitialized) {
                    if (autoLoginState == AutoLoginConstant.AUTO_LOGIN_SUCCESS) {

                        Log.e("LoginActivity", "AUTO_LOGIN_SUCCESS")
                        moveMainActivity(context)
                    }
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    false
                }
            }
        }
    )
}



private fun checkAutoLoginState(loginViewModel: LoginViewModel) {

    if (loginViewModel.getAccessToken() != "") {
        autoLoginState = AutoLoginConstant.AUTO_LOGIN_SUCCESS
        Log.e("checkAutoLoginState", "AUTO_LOGIN_SUCCESS")
    } else {
        autoLoginState = AutoLoginConstant.AUTO_LOGIN_FAILURE
        Log.e("checkAutoLoginState", "AUTO_LOGIN_FAILURE")
    }


}

private fun moveMainActivity(context:Context){
    val intent = Intent(context, MainActivity::class.java)
    startActivity(context, intent, null)

}

@AndroidEntryPoint
class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this

        setContent {
            LoginUi(context)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginUi(context: Context,
            loginViewModel: LoginViewModel = hiltViewModel()){


    checkAutoLoginState(loginViewModel)

    val content: View = LocalView.current

    autoLogin(content, context)

    val kakaoLoginManager: KakaoLoginManager = KakaoLoginManager(context, loginViewModel)

    CleanArchitectureTheme {

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

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    CleanArchitectureTheme {

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

        }
    }
}