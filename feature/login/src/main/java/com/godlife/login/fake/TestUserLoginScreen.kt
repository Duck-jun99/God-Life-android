package com.godlife.login.fake

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextField
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.login.LoginActivity
import com.godlife.login.LoginViewModel
import com.godlife.login.SignUpScreenRoute
import com.godlife.navigator.MainNavigator
import com.godlife.network.model.BodyQuery
import kotlinx.coroutines.launch

@Composable
fun TestUserLoginScreen(
    context: Context,
    loginViewModel: LoginViewModel,
    navController: NavController,
    mainNavigator: MainNavigator,
    loginActivity: LoginActivity
){

    val cScoped = rememberCoroutineScope()

    val id = remember { mutableStateOf("") }

    Log.e("TestUserLoginScreen", id.value)
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp)
            .statusBarsPadding()
    ){
        Text(text = "ID")

        Box(
            modifier = Modifier
                .background(GrayWhite3)
                .fillMaxWidth()
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ){

            GodLifeTextFieldGray(
                text = id.value,
                onTextChanged = { id.value = it },
                hint = "ID를 입력해주세요."
            )

        }

        GodLifeButtonWhite(
            onClick = {

                if(loginViewModel.getUserId() == ""){
                    loginViewModel.saveUserId(id.value)
                    Log.e("TestUserLoginScreen", "SAVE USER ID: ${id.value}")
                }

                var checkLoginOrSignUp:Boolean? = null
                var userInfo: BodyQuery? = null

                cScoped.launch {

                    launch {
                        userInfo = loginViewModel.checkUserExistence(id.value)
                        Log.e("TestUserLoginScreen", userInfo.toString())
                        checkLoginOrSignUp = userInfo?.alreadySignUp
                        //checkLoginOrSignUp = loginViewModel.checkUserExistence(user.id.toString())
                    }.join()

                    launch {

                        Log.e("TestUserLoginScreen", checkLoginOrSignUp.toString())

                        if (!checkLoginOrSignUp!!) {
                            //navController.navigate(SignUpScreenRoute.route)
                            Toast.makeText(context, "회원가입 되지 않은 ID 입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            userInfo?.let { it.accessToken?.let { it1 ->
                                loginViewModel.saveAccessToken(
                                    it1
                                )
                            } }
                            userInfo?.let { it.refreshToken?.let { it1 ->
                                loginViewModel.saveRefreshToken(
                                    it1
                                )
                            } }
                            moveMainActivity(mainNavigator, loginActivity)
                        }

                    }

                }
                      },
            text = { Text(text = "로그인") }
        )
    }
}

private fun moveMainActivity(mainNavigator: MainNavigator, loginActivity: LoginActivity){

    mainNavigator.navigateFrom(
        activity = loginActivity,
        withFinish = true
    )

}


@Preview
@Composable
fun TestUserLoginScreenPreview(){
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp)
            .statusBarsPadding()
    ){
        Text(text = "ID")

        Box(
            modifier = Modifier
                .background(GrayWhite3)
                .fillMaxWidth()
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ){

            GodLifeTextFieldGray(
                text = "",
                onTextChanged = {},
                hint = "ID를 입력해주세요."
            )

        }



        GodLifeButtonWhite(
            onClick = { /*TODO*/ },
            text = { Text(text = "로그인") }
        )
    }
}