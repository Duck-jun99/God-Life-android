package com.godlife.community_page.famous

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.CommunityPageViewModel
import com.godlife.community_page.R
import com.godlife.community_page.navigation.PostDetailRoute
import com.godlife.designsystem.list.CommunityFamousPostList
import com.godlife.designsystem.list.CommunityFamousPostListPreview
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.network.model.PostDetailBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun FamousPostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CommunityPageViewModel
){

    GodLifeTheme {
        LazyColumn(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ) {

            item{ WeeklyFamousPostListView(viewModel = viewModel, navController = navController) }

            item{TotalFamousPostListView(viewModel = viewModel, navController = navController)}

        }
    }
}

@Composable
fun WeeklyFamousPostListView(
    modifier: Modifier = Modifier,
    viewModel: CommunityPageViewModel,
    navController: NavController
){

    val weeklyFamousPost = viewModel.weeklyFamousPostList.collectAsState().value

    Box(
        modifier
            //.background(Color.White)
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

            Text(text = "이번주 굿생 인정 게시물", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

            Spacer(modifier.size(10.dp))

            LazyRow {
                itemsIndexed(weeklyFamousPost) { index, item ->

                    WeeklyFamousPostItem(famousPostItem = item, navController = navController)
                }
            }

            Spacer(modifier.size(10.dp))


        }
    }
}

@Composable
fun WeeklyFamousPostItem(
    modifier: Modifier = Modifier,
    famousPostItem: PostDetailBody,
    navController: NavController
){
    val postId = famousPostItem.board_id.toString()

    CommunityFamousPostList(
        famousPostItem = famousPostItem,
        clickOption = {navController.navigate("${PostDetailRoute.route}/$postId")}
    )


}

@Composable
fun TotalFamousPostListView(
    modifier: Modifier = Modifier,
    viewModel: CommunityPageViewModel,
    navController: NavController
){

    val items = viewModel.allFamousPostList.collectAsState()

    Box(
        modifier
            //.background(Color.White)
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

            Text(text = "전체 Top10 굿생 인정 게시물", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

            Spacer(modifier.size(10.dp))

            for(index in 0 until items.value.size){
                TotalFamousPostItem(index = index, item = items.value[index], navController = navController)
            }




        }
    }
}

@Composable
fun TotalFamousPostItem(
    modifier: Modifier = Modifier,
    index: Int,
    item: PostDetailBody,
    navController: NavController
){
    Row(
        modifier = modifier
            .padding(bottom = 10.dp)
            .height(150.dp)
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { navController.navigate("${PostDetailRoute.route}/${item.board_id}") }
            .padding(10.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    if(index==0) {
                        Color(0xFFFFBE3B)
                    }
                    else if(index==1) {
                        Color(0xFFF3E3E3)
                    }
                    else if(index==2) {
                        Color(0xFFFD8F6D)
                    }
                    else {
                        OpaqueDark
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Text(text = "${index+1}", style = TextStyle(color = if(index==1) Color.Black else Color.White, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(10.dp))

        GlideImage(
            imageModel = { if(item.imagesURL?.isNotEmpty() == true) BuildConfig.SERVER_IMAGE_DOMAIN + item.imagesURL?.get(0).toString() else R.drawable.category3 },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = modifier
                .size(100.dp),
            loading = {
                Box(
                    modifier = modifier
                        .background(GrayWhite3)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){

                    CircularProgressIndicator(
                        color = OrangeMain
                    )

                }

            },
            failure = {
                Image(
                    painter = painterResource(id = R.drawable.category3),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        )

        Spacer(modifier.size(10.dp))

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = modifier
                    .weight(0.7f)
            ) {
                Text(
                    text = item.nickname,
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier.size(5.dp))

                HorizontalDivider()

                Spacer(modifier.size(5.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = item.title,
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Box(
                modifier = modifier
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ){
                Text(text = "${item.godScore}점", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold))
            }

        }


    }

}


@Preview
@Composable
fun WeeklyFamousPostListViewPreview(
    modifier: Modifier = Modifier
){

    Box(
        modifier
            .background(GrayWhite3)
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

            Text(text = "이번주 굿생 인정 게시물", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)


            Spacer(modifier.size(10.dp))

            LazyRow {
                items(5) { item ->
                    CommunityFamousPostListPreview()
                }
            }

            Spacer(modifier.size(10.dp))



        }
    }
}

@Preview
@Composable
fun TotalFamousPostListViewPreview(
    modifier: Modifier = Modifier
){

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

            Text(text = "전체 Top10 굿생 인정 게시물", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

            Spacer(modifier.size(10.dp))

            LazyColumn {
                items(10) { item ->
                    TotalFamousPostItemPreview()
                }
            }




        }
    }
}

@Preview
@Composable
fun TotalFamousPostItemPreview(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(OpaqueDark),
            contentAlignment = Alignment.Center
        ){
            Text(text = "1", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(10.dp))

        Image(
            modifier = modifier
                .size(100.dp),
            painter = painterResource(id = R.drawable.category3),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier.size(10.dp))

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Column(
                modifier = modifier
                    .weight(0.7f)
            ) {
                Text(
                    text = "nickname",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier.size(5.dp))

                HorizontalDivider()

                Spacer(modifier.size(5.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = "title",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Box(
                modifier = modifier
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ){
                Text(text = "40점", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp))
            }


        }


    }

}

