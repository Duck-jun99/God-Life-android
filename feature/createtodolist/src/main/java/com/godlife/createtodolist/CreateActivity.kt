package com.godlife.createtodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.navigator.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateActivity :ComponentActivity() {

    @Inject
    lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CreateTodoList(mainNavigator, this)
        }

    }
}

@Composable
fun CreateTodoList(
    mainNavigator: MainNavigator,
    createActivity: CreateActivity,
    createViewModel: CreateTodoListViewModel = hiltViewModel()

) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Column(
        modifier = Modifier.fillMaxWidth().statusBarsPadding()
    ) {

        NavHost(navController = navController, startDestination = CreateTodoListScreen1Route.route,
            modifier = Modifier
                .fillMaxSize()) {


            composable(CreateTodoListScreen1Route.route){
                CreateTodoListScreen1(
                    navController,
                    createViewModel = createViewModel
                )
            }

            composable(CreateTodoListScreen2Route.route){
                CreateTodoListScreen2(
                    navController,
                    createViewModel = createViewModel
                )
            }

            composable(CreateTodoListScreen3Route.route){
                CreateTodoListScreen3(
                    navController,
                    createActivity,
                    mainNavigator,
                    createViewModel = createViewModel
                )
            }

            /*
            composable(
                route = CreateTodoListScreen2Route.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(CreateTodoListScreen1Route.route)
                }
                CreateTodoListScreen2(
                    navController,
                    createViewModel = hiltViewModel(parentEntry)
                )
            }

            composable(
                route = CreateTodoListScreen3Route.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(CreateTodoListScreen2Route.route)
                }

                CreateTodoListScreen3(
                    navController,
                    createViewModel = hiltViewModel(parentEntry)
                )
            }

             */

        }
        

        /*
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.1f)){

            GodLifeButton(
                onClick = {
                    //첫번째 화면
                    if(currentRoute == CreateTodoListScreen1Route.route) {

                        navController.navigate(CreateTodoListScreen2Route.route)
                    }
                    //두번째 화면
                    else if (currentRoute == CreateTodoListScreen2Route.route) {

                        navController.navigate(CreateTodoListScreen3Route.route)
                    }
                    //세번째 화면
                    else{
                        MoveMainActivity(mainNavigator = mainNavigator, createActivity = createActivity)
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.5f),
                text = {Text(text = if(currentRoute == CreateTodoListScreen1Route.route) {"다음"}
                else if (currentRoute == CreateTodoListScreen2Route.route) {"완료"}
                else {"갓생 시작"},
                    style = TextStyle(color = Color.White)
                )
                }) {
            }
        }

         */




    }

}

@Preview(showBackground = true)
@Composable
fun CreateUiPreview(){
    GodLifeTheme {

        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)){

                //CreateTodoListScreen1()
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)){

                GodLifeButton(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.Center),
                    text = {Text(text = "다음",
                        style = TextStyle(color = Color.White)
                    )}
                ) {
                }
            }

        }

    }
}

object CreateTodoListScreen1Route {
    const val route = "CreateTodoListScreen1"
}

object CreateTodoListScreen2Route {
    const val route = "CreateTodoListScreen2"
}

object CreateTodoListScreen3Route {
    const val route = "CreateTodoListScreen3"
}
