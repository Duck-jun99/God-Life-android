package com.godlife.community_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite3

@Preview(showBackground = true)
@Composable
fun LatestPostScreen(modifier: Modifier = Modifier){
    GodLifeTheme {
        Column(
            modifier.fillMaxSize()
                .background(GreyWhite3)
        ) {
            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = "따끈따끈 최신 게시물을 볼까요?", style = GodLifeTypography.titleSmall)
                }
            }


        }
    }
}
