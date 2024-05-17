package com.godlife.setting_page

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.navigator.LoginNavigator

@Composable
fun SettingPageScreen(
    mainActivity: Activity,
    loginNavigator: LoginNavigator,
    viewModel: SettingPageViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {

    }

    val logoutResult by viewModel.logoutResult.collectAsState()

    GodLifeTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            GodLifeButton(onClick = {
                logout(viewModel)
            }) {
                Text(text = "로그아웃", style = TextStyle(color = Color.White))
            }
        }
    }

    if(logoutResult == true){
        moveLoginActivity(loginNavigator, mainActivity)
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
