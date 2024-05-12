package com.godlife.createtodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.PurpleMain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateActivity :ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CreateTodoList()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CreateTodoList() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        NavHost(navController = navController, startDestination = CreateTodoListScreen1Route.route,
            modifier = Modifier.fillMaxHeight(0.7f)) {

            composable(CreateTodoListScreen1Route.route){
                CreateTodoListScreen1()
            }

            composable(CreateTodoListScreen2Route.route){
                CreateTodoListScreen2()
            }

            composable(CreateTodoListScreen3Route.route){
                CreateTodoListScreen3()
            }

        }

        Button(onClick = {
            if(currentRoute == CreateTodoListScreen1Route.route) {navController.navigate(CreateTodoListScreen2Route.route)}
            else if (currentRoute == CreateTodoListScreen2Route.route) {navController.navigate(CreateTodoListScreen3Route.route)}

        },
            colors = ButtonDefaults.buttonColors(
                containerColor = PurpleMain
            )
            , modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.3f)) {
            Text(text =
            if(currentRoute == CreateTodoListScreen1Route.route) {"다음"}
            else if (currentRoute == CreateTodoListScreen2Route.route) {"완료"}
            else {"갓생 시작"}
            ,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
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