package com.godlife.main_page


import android.content.Context
import android.content.Intent
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
import com.godlife.designsystem.CleanArchitectureTheme
import com.godlife.designsystem.NullColor
import com.godlife.designsystem.Purple40
import com.godlife.designsystem.PurpleMain
import com.godlife.designsystem.PurpleSecond

@Preview(showBackground = true)
@Composable
fun MainPageScreen(
    viewModel: MainPageViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {

    }

    val context = LocalContext.current

    CleanArchitectureTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GradientSquareWithText(context)

        }
    }


}



@Composable
fun GradientSquareWithText(
    context: Context
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
                moveCreateActivity(context = context)
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
    CleanArchitectureTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            //GradientSquareWithText(context)

            Spacer(modifier = Modifier.height(30.dp))


        }
    }
}

@Preview
@Composable
fun TodoListCard() {
    Box(
        modifier = Modifier
            .background(color = Purple40)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "오늘의 투두 리스트를 만들어 주세요!",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "> 투두 리스트 만들기 가기")
            }
        }
    }
}

private fun moveCreateActivity(context:Context){

    val intent = Intent(context, CreateActivity::class.java)
    ContextCompat.startActivity(context, intent, null)

}

