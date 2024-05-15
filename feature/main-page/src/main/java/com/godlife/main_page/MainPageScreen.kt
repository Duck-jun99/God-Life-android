package com.godlife.main_page


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.createtodolist.CreateActivity
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.NullColor
import com.godlife.designsystem.theme.Purple40
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.theme.PurpleSecond
import com.godlife.navigator.CreatetodolistNavigator


@Composable
fun MainPageScreen(
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator,
    viewModel: MainPageViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {
        
        Log.e("MainPageScreen", viewModel.todoList.value.toString())
    }

    val context = LocalContext.current

    GodLifeTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NoTodoListBox(context, mainActivity, createNavigator)

        }
    }

}



@Composable
fun NoTodoListBox(
    context: Context,
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator
) {

    Column(
        Modifier
            .background(
                brush = Brush.verticalGradient(listOf(PurpleSecond, PurpleMain)),
                shape = RoundedCornerShape(30.dp),
                alpha = 0.8f
            )
            .size(300.dp)
            .padding(30.dp),
    ) {

        Text(
            text = "오늘의 투두 리스트를\n만들어주세요!",
            color = Color.White,

            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Divider(
            color = Color.White,
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Button(
            onClick = {
                moveCreateActivity(createNavigator, mainActivity)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NullColor
            ),
        ) {
            Text(text = "> 투두 리스트 만들기 가기",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

        }

    }
}

@Preview
@Composable
fun NoTodoListBoxPreview() {
    Column(
        Modifier
            .background(
                brush = Brush.verticalGradient(listOf(PurpleSecond, PurpleMain)),
                shape = RoundedCornerShape(30.dp),
                alpha = 0.8f
            )
            .size(300.dp)
            .padding(30.dp),
    ) {

        Text(
            text = "오늘의 투두 리스트를\n만들어주세요!",
            color = Color.White,

            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Divider(
            color = Color.White,
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Button(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NullColor
            ),
        ) {
            Text(text = "> 투두 리스트 만들기 가기",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    GodLifeTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            NoTodoListBoxPreview()

            Spacer(modifier = Modifier.height(30.dp))


        }
    }
}


private fun moveCreateActivity(createNavigator: CreatetodolistNavigator, mainActivity: Activity){
    createNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = false
    )

}

