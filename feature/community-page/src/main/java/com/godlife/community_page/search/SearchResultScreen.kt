package com.godlife.community_page.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.godlife.designsystem.theme.GodLifeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.PurpleMain

@Preview(showBackground = true)
@Composable
fun SearchResultScreen(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(text = "\"\"에 대한 검색 결과를 찾았어요.")

            LazyColumn(modifier.padding(10.dp)) {
                items(10) {
                    SearchedPostListPreview()
                    Spacer(modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchedPostListPreview(modifier: Modifier = Modifier){


    Column(
        modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
    ){

        Row(
            modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically){

            Row(modifier.weight(0.8f),
                verticalAlignment = Alignment.CenterVertically){

                //프로필 이미지 부분
                Box(
                    modifier
                        .size(40.dp)
                        .background(Color.Gray, shape = CircleShape)){
                }

                Spacer(modifier.size(10.dp))

                Text(text = "Name", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                Spacer(modifier.size(10.dp))

                //티어 보여줄 부분
                Text(text = "마스터", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))

            }
            Box(modifier.weight(0.2f), contentAlignment = Alignment.TopCenter){
                Text(text = "39분전", style = TextStyle(color = GrayWhite, fontSize = 15.sp))
            }

        }

        Column(
            modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()){
            Text(text = "Title", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp))

            Spacer(modifier.size(10.dp))

            Text(text = "Text", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))

        }

    }
}
