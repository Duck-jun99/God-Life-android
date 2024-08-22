package com.godlife.designsystem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.PurpleMain

@Preview
@Composable
fun GodLifeErrorScreen(
    modifier: Modifier = Modifier,
    errorMessageEnabled: Boolean = true,
    errorMessage: String = "",
    buttonEnabled: Boolean = true,
    buttonEvent: () -> Unit = {},
    buttonText: String = "메인으로 돌아가기"

){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = modifier
                .size(40.dp),
            imageVector = Icons.Outlined.Warning,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "오류가 발생했어요.\n잠시 후 다시 시도해주세요.",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        if(errorMessageEnabled){

            Text(
                text = "에러 메시지: $errorMessage",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center
            )

        }



        if(buttonEnabled){

            Spacer(modifier.size(20.dp))

            GodLifeButtonWhite(
                onClick = buttonEvent,
                text = { Text(text = buttonText, style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
            )

        }



    }
}