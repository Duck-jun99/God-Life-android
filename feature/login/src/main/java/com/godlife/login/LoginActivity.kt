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
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main.MainActivity
import com.godlife.navigator.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity: ComponentActivity() {
    @Inject
    lateinit var mainNavigator: MainNavigator

    private val loginViewModel: LoginViewModel by viewModels<LoginViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this
        val content: View = this.findViewById(android.R.id.content)

        checkAutoLoginState(loginViewModel)

        autoLogin(content, context, mainNavigator, this)

        setContent {
            LoginUi(context)
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginUi(context: Context){

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(
        modifier = Modifier.fillMaxSize()
            .background(PurpleMain)
    ) {
        NavHost(navController = navController, startDestination = LoginScreenRoute.route,
            modifier = Modifier.fillMaxHeight()) {

            composable(LoginScreenRoute.route){
                LoginScreen(context, navController)
            }

            composable(SignUpScreenRoute.route){
                SignUpScreen()
            }

        }
    }


}

object LoginScreenRoute{
    const val route = "LoginScreen"
}

object SignUpScreenRoute{
    const val route = "SignUpScreen"
}

private lateinit var autoLoginState:AutoLoginConstant
private fun autoLogin(content: View, context: Context, mainNavigator: MainNavigator, loginActivity: LoginActivity) {

    content.viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (::autoLoginState.isInitialized) {
                    if (autoLoginState == AutoLoginConstant.AUTO_LOGIN_SUCCESS) {

                        Log.e("LoginActivity", "AUTO_LOGIN_SUCCESS")
                        moveMainActivity(mainNavigator, loginActivity)
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

private fun moveMainActivity(mainNavigator: MainNavigator, loginActivity: LoginActivity){
    //val intent = Intent(context, MainActivity::class.java)
    //ContextCompat.startActivity(context, intent, null)
    mainNavigator.navigateFrom(
        activity = loginActivity,
        withFinish = true
    )

}

private fun moveSignUpActivity(context: Context){
    //val intent = Intent(context, MainActivity::class.java)
    //ContextCompat.startActivity(context, intent, null)

}