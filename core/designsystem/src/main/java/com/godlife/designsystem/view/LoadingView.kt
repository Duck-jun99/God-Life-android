package com.godlife.designsystem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godlife.designsystem.theme.OpaqueDark

@Preview
@Composable
fun GodLifeLoadingScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = OpaqueDark,
    textColor: Color = Color.White,
    progressColor: Color = Color.White,
    text: String = "유저님의 정보를 받아오고 있어요."
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = progressColor)
        Spacer(modifier.size(10.dp))
        Text(text = text, style = TextStyle(color = textColor), textAlign = TextAlign.Center)
    }
}