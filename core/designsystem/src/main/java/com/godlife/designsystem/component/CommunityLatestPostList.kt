package com.godlife.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.GreyWhite2
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.LatestPostItem

@Composable
fun CommunityLatestPostList(modifier: Modifier = Modifier,
                            latestPostItem: LatestPostItem){
    GodLifeTheme(
        modifier
            .height(375.dp)
            .background(GreyWhite2)
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(375.dp)
                .drawBehind {
                    val shadowColor = Color(0x1F000000) // 원하는 그림자 색상 설정
                    val transparent = Color.Transparent

                    val brush = Brush.verticalGradient(
                        colors = listOf(transparent, shadowColor),
                        startY = size.height - 50.dp.toPx(), // 그림자가 시작되는 위치 설정
                        endY = size.height
                    )

                    drawRect(brush)
                }
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(360.dp)
            ){
                Row(
                    modifier
                        .weight(0.2f)
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row(
                        modifier.weight(0.8f),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        //프로필 이미지 부분
                        Box(
                            modifier
                                .size(50.dp)
                                .background(Color.Gray)
                        ){
                            Text(text = "Image")
                        }

                        Spacer(modifier.size(10.dp))

                        Text(text = latestPostItem.name, style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                        Spacer(modifier.size(10.dp))

                        //티어 보여줄 부분
                        Text(text = latestPostItem.rank, style = TextStyle(color = Color.Magenta, fontWeight = FontWeight.Bold, fontSize = 15.sp))
                    }
                    Box(modifier.weight(0.2f)){
                        Text(text = "39분전", style = TextStyle(color = GreyWhite, fontSize = 15.sp))
                    }
                }

                Box(
                    modifier
                        .weight(0.2f)
                        .padding(5.dp)
                        .fillMaxWidth()
                ){
                    Text(text = latestPostItem.title)
                }

                //게시물 대표 사진 보여질 곳
                Box(
                    modifier
                        .background(PurpleMain)
                        .weight(0.4f)
                        .fillMaxSize()
                ){
                    Text(text = "Image")
                }

                Box(
                    modifier
                        .weight(0.2f)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "조회수: ${latestPostItem.view}    댓글: ${latestPostItem.comments}개")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityLatestPostListPreview(modifier: Modifier = Modifier){
    GodLifeTheme(
        modifier.height(375.dp)
            .background(GreyWhite2)
    ) {
        Surface(
            shadowElevation = 7.dp
        ){
            Column(
                modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(360.dp)
            ){

                Row(
                    modifier
                        .weight(0.2f)
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically){

                    Row(modifier.weight(0.8f),
                        verticalAlignment = Alignment.CenterVertically){
                        //프로필 이미지 부분
                        Box(
                            modifier
                                .size(50.dp)
                                .background(Color.Gray)){
                            Text(text = "Image")
                        }

                        Spacer(modifier.size(10.dp))

                        Text(text = "Name", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                        Spacer(modifier.size(10.dp))

                        //티어 보여줄 부분
                        Text(text = "마스터", style = TextStyle(color = Color.Magenta, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                    }
                    Box(modifier.weight(0.2f)){
                        Text(text = "39분전", style = TextStyle(color = GreyWhite, fontSize = 15.sp))
                    }

                }

                Box(modifier.weight(0.2f)
                    .padding(5.dp)
                    .fillMaxWidth()){
                    Text(text = "Title")
                }



                //게시물 대표 사진 보여질 곳
                Box(
                    modifier
                        .background(PurpleMain)
                        .weight(0.4f)
                        .height(350.dp)
                        .fillMaxWidth()){

                    Text(text = "Image")
                }

                Box(modifier.weight(0.2f)
                    .padding(5.dp),
                    contentAlignment = Alignment.Center){
                    Text(text = "조회수: 10    댓글: 1개")
                }


            }
        }

    }
}