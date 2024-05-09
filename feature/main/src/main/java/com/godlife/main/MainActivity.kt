package com.godlife.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.CleanArchitectureTheme
import com.godlife.designsystem.NullColor
import com.godlife.designsystem.Purple40
import com.godlife.designsystem.PurpleMain
import com.godlife.designsystem.PurpleSecond
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainUiTheme()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUiTheme(){

    CleanArchitectureTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GradientSquareWithText()

        }
    }
}

@Preview
@Composable
fun GradientSquareWithText() {
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
            onClick = { null },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NullColor),
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

            GradientSquareWithText()

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