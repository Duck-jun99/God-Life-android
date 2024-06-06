package com.godlife.community_page.post_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.community_page.R
import com.godlife.community_page.navigation.StimulusPostRoute
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OpaqueLight
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.theme.PurpleSecond

@Preview
@Composable
fun StimulusDetailScreen(modifier: Modifier = Modifier) {

    GodLifeTheme {

        LazyColumn {


        }
    }
}

@Preview
@Composable
fun StimulusPostCover(modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize()){
        Image(painter = painterResource(id = R.drawable.category3), contentDescription = "", modifier.fillMaxSize(), contentScale = ContentScale.Crop)
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(listOf(OpaqueLight, OpaqueDark, PurpleMain))
        ),
        contentAlignment = Alignment.BottomStart
    ){

        Column(modifier = modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(10.dp),
            verticalArrangement = Arrangement.Center){

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.ic_person), contentDescription = "",
                    modifier
                        .background(color = GrayWhite, shape = CircleShape)
                        .size(30.dp))

                Spacer(modifier.size(10.dp))

                Text(text = "Nickname", style = TextStyle(color = Color.White))
            }

            Spacer(modifier.size(20.dp))

            Text(text = "Title", style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }

    }

}