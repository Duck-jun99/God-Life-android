package com.godlife.community_page.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import com.godlife.community_page.CommunityPageViewModel
import com.godlife.designsystem.list.TagItemPreview
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.PurpleMain

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CommunityPageViewModel
){

}

@Preview
@Composable
fun RankingPageScreenPreview(
    modifier: Modifier = Modifier
){


}

@Preview
@Composable
fun WeeklyRankingListPreview(
    modifier: Modifier = Modifier
){

    Box(
        modifier
            .padding(
                vertical = 10.dp, horizontal = 10.dp
            )
            .width(250.dp)
            .height(400.dp)
            .shadow(7.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ){

    }

}


@Preview
@Composable
fun CommunityFamousPostListPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier
            .padding(
                top = 5.dp,
                bottom = 5.dp,
                start = 10.dp,
                end = 10.dp
            )
            .width(250.dp)
            .height(400.dp)
            .shadow(7.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        Box(
            modifier
                .size(width = 250.dp, height = 250.dp)
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
        ){
            Text(text = "IMAGE", modifier.align(Alignment.Center))
        }
        Column(
            modifier
                .size(width = 250.dp, height = 150.dp)
                .fillMaxWidth()
                .padding(20.dp)) {

            Text(text = "NAME", style = GodLifeTypography.titleMedium)

            Spacer(modifier.size(5.dp))

            //티어 보여줄 부분
            Text(text = "마스터", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))


            Spacer(modifier.size(10.dp))

            Text(text = "이번 주 갓생 인정: 100개", style = GodLifeTypography.titleSmall)


        }
    }

}