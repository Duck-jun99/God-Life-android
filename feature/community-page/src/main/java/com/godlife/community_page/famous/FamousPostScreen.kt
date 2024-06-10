package com.godlife.community_page.famous

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.community_page.R
import com.godlife.designsystem.list.CommunityFamousPostList
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.model.community.FamousPostItem
import com.godlife.model.community.TagItem

@Composable
fun FamousPostScreen(modifier: Modifier = Modifier){

    GodLifeTheme {
        Column(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ) {

            Box(
                modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .height(25.dp)){


                Row(modifier.fillMaxWidth()){
                    Icon(painter = painterResource(R.drawable.star_icons8), contentDescription = "", tint = Color.Unspecified)
                    Spacer(modifier.size(5.dp))
                    Text(text = "실시간 인기 갓생 인증글을 확인해보세요!", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
                }


            }



            Spacer(modifier = modifier.size(20.dp))

            FamousPostPreview()

        }
    }
}

@Preview
@Composable
fun FamousPostPreview(modifier: Modifier = Modifier){

    var famousPost =
        listOf(
            FamousPostItem(
            "Name1", "Title1", "Text1", rank = "마스터", tagItem = listOf(TagItem("Tag1"), TagItem("Tag2"))),
            FamousPostItem(
                "Name2", "Title2", "Text2", rank = "실버", tagItem = listOf(TagItem("Tag1"), TagItem("Tag2")))
        )

    Box(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 10.dp
            ),
        contentAlignment = Alignment.CenterStart
    ){
        Column {


            Spacer(modifier.size(10.dp))

            LazyRow {
                itemsIndexed(famousPost) { index, item ->
                    CommunityFamousPostList(famousPostItem = item)
                }
            }

            Spacer(modifier.size(10.dp))

            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = modifier.size(30.dp)
            ) {
                Text(text = "> 더보기", style = GodLifeTypography.titleSmall)
            }


        }
    }
}