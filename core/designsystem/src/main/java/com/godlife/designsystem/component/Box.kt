package com.godlife.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GodLifeBox(modifier: Modifier = Modifier){
    Box(modifier = modifier
        .fillMaxWidth()
        .background(
            color = Color.White,
            shape = RoundedCornerShape(15.dp))
    ){

    }
}