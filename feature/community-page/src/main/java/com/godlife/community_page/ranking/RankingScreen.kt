package com.godlife.community_page.ranking

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.CommunityPageViewModel
import com.godlife.community_page.R
import com.godlife.community_page.RankingPageUiState
import com.godlife.community_page.navigation.PostDetailRoute
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OrangeLight
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.RankingBody
import com.godlife.network.model.UserProfileBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    parentNavController: NavController,
    viewModel: CommunityPageViewModel
){

    val uiState by viewModel.rankingUiState.collectAsState()

    val weeklyRankingList = viewModel.weeklyRankingList.collectAsState()
    val allRankingList = viewModel.allRankingList.collectAsState()


    when(uiState) {
        is RankingPageUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is RankingPageUiState.Success -> {

            Log.e("allRankingList", allRankingList.value.toString())

            val weeklyPagerState = rememberPagerState(initialPage = 0, pageCount = {weeklyRankingList.value.size})
            val totalPagerState = rememberPagerState(initialPage = 0, pageCount = {allRankingList.value.size})

            val totalPage = remember{ mutableIntStateOf(0) }


            /*
            viewModel.getRankingUserPost(nickname = allRankingList.value[totalPage.value].nickname)

            val rankingUserPostList = viewModel.rankingUserPostList.collectAsLazyPagingItems()


             */

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(GrayWhite3)
            ) {
                item{

                    Box(
                        modifier
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

                            Text(text = "이번주 Top10 굿생러는 누구일까요?", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

                            Spacer(modifier.size(10.dp))

                            HorizontalPager(
                                state = weeklyPagerState,
                                beyondViewportPageCount = 2,
                                pageSize = PageSize.Fixed(250.dp)
                            ) {page ->
                                WeeklyRankingListItem(
                                    weeklyRankingListItem = weeklyRankingList.value[page],
                                    parentNavController = parentNavController
                                )
                            }

                        }
                    }



                }

                item{

                    Column {

                        Text(
                            modifier = modifier
                                .padding(10.dp),
                            text = "전체 Top10 굿생러",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        TotalRankingListItem1(
                            totalRankingListItem = allRankingList.value,
                            index = totalPagerState.currentPage,
                            parentNavController = parentNavController,
                            viewModel = viewModel,
                            pagerState = totalPagerState
                        )

                        Spacer(modifier.size(10.dp))

                    }



                }



                item{
                    viewModel.rankingUserPostList.collectAsState().value?.let {
                        TotalRankingListItem2(
                            userProfileItem = it
                        )
                    }
                }


            }

        }
        is RankingPageUiState.Error -> {

        }
    }


}

@Composable
fun WeeklyRankingListItem(
    modifier: Modifier = Modifier,
    parentNavController: NavController,
    weeklyRankingListItem: RankingBody
){

    Box(
        modifier
            .padding(
                vertical = 10.dp, horizontal = 10.dp
            )
            .width(250.dp)
            .height(200.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(7.dp, shape = RoundedCornerShape(16.dp))
            .clickable { parentNavController.navigate("${"ProfileScreen"}/${weeklyRankingListItem.memberId}") }
    ){

        GlideImage(
            imageModel = { if(weeklyRankingListItem.backgroundUrl.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + weeklyRankingListItem.backgroundUrl else R.drawable.category3 },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
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

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GlideImage(
                imageModel = { if(weeklyRankingListItem.profileURL.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + weeklyRankingListItem.profileURL else R.drawable.ic_person },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(4.dp, Color.White),
                        CircleShape
                    )
                    .padding(4.dp),
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

            Text(
                text = weeklyRankingListItem.nickname,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier.size(10.dp))

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "이번주 굿생 점수: ${weeklyRankingListItem.godLifeScore}",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

            }

        }

    }

}

@Composable
fun TotalRankingListItem1(
    modifier: Modifier = Modifier,
    index: Int,
    totalRankingListItem: List<RankingBody>,
    pagerState: PagerState,
    parentNavController: NavController,
    viewModel: CommunityPageViewModel
){

    LaunchedEffect(key1 = pagerState.currentPage) {
        viewModel.getTotalRankingUserInfo(totalRankingListItem[pagerState.currentPage].memberId.toString())
    }

    Box(
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(OrangeLight),
        contentAlignment = Alignment.Center
    ){

        HorizontalPager(
            state = pagerState
        ) {page ->

            Box(
                modifier = modifier
                    .clickable { parentNavController.navigate("${"ProfileScreen"}/${totalRankingListItem[pagerState.currentPage].memberId}") },
                contentAlignment = Alignment.Center
            ){

                GlideImage(
                    imageModel = { if(totalRankingListItem[pagerState.currentPage].backgroundUrl.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + totalRankingListItem[pagerState.currentPage].backgroundUrl else R.drawable.category3 },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .fillMaxSize()
                        .blur(
                            radiusX = 15.dp, radiusY = 15.dp
                        ),
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

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = modifier
                            .size(45.dp)
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
                        Text(
                            text = "${index+1}",
                            style = TextStyle(
                                color = if(index==1) Color.Black else Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        GlideImage(
                            imageModel = { if(totalRankingListItem[pagerState.currentPage].profileURL.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + totalRankingListItem[pagerState.currentPage].profileURL else R.drawable.category3 },
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            ),
                            modifier = modifier
                                .size(150.dp)
                                .clip(shape = CircleShape),
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

                        Spacer(modifier.size(20.dp))

                        Text(
                            text = totalRankingListItem[pagerState.currentPage].nickname,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier.size(20.dp))

                        Text(
                            modifier = modifier
                                .padding(start = 20.dp, end = 20.dp),
                            text = "\"${totalRankingListItem[pagerState.currentPage].whoAmI}\"",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Center
                        )

                    }


                }


            }

        }

        Row(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                val size = if (pagerState.currentPage == iteration) 10.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(size)
                )
            }
        }

    }


}

@Composable
fun TotalRankingListItem2(
    modifier: Modifier = Modifier,
    userProfileItem: UserProfileBody
){

    Row(
        modifier = modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = modifier
                .weight(0.5f)
                .padding(10.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "", tint = OrangeMain)
                Spacer(modifier.size(10.dp))
                Text(text = "굿생 점수", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
            }
            Spacer(modifier.size(10.dp))

            Text(text = "${userProfileItem.godLifeScore}점", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }

        Column(
            modifier = modifier
                .weight(0.5f)
                .padding(10.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "", tint = OrangeMain)
                Spacer(modifier.size(10.dp))
                Text(text = "게시물 수", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
            }
            Spacer(modifier.size(10.dp))

            Text(text = "${userProfileItem.memberBoardCount}개", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }
    }

}

@Composable
fun RankingUserPostListItem(
    modifier: Modifier = Modifier,
    rankingUserPostListItem: PostDetailBody?,
    navController: NavController
){

    if(rankingUserPostListItem != null){
        Row(
            modifier
                .padding(5.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(15.dp))
                .padding(10.dp)
                .clickable { navController.navigate("${PostDetailRoute.route}/${rankingUserPostListItem.board_id}") },
            verticalAlignment = Alignment.CenterVertically
        ){

            GlideImage(
                imageModel = { if(rankingUserPostListItem.imagesURL?.isNotEmpty() == true) BuildConfig.SERVER_IMAGE_DOMAIN + rankingUserPostListItem.imagesURL!![0] else R.drawable.category3 },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = modifier
                    .size(70.dp)
                    .clip(shape = RoundedCornerShape(15.dp)),
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

            Column {

                // 게시물 제목
                Text(text = rankingUserPostListItem.title, style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold))

                Spacer(modifier.size(5.dp))

                //날짜
                Text(text = rankingUserPostListItem.writtenAt, style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal))

                Spacer(modifier.size(5.dp))

                //조회수, 댓글 수
                Text(text = "조회 수: ${rankingUserPostListItem.views}, 댓글 수: ${rankingUserPostListItem.commentCount}", style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal))

            }


        }
    }

}

@Preview
@Composable
fun RankingPageScreenPreview(
    modifier: Modifier = Modifier
){

    val weeklyPagerState = rememberPagerState(initialPage = 0, pageCount = {2})
    val totalPagerState = rememberPagerState(initialPage = 0, pageCount = {2})

    val totalPage = remember{ mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item{

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

                    Text(text = "이번주 Top10 굿생러", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

                    Spacer(modifier.size(10.dp))

                    HorizontalPager(
                        state = weeklyPagerState,
                        beyondViewportPageCount = 2,
                        pageSize = PageSize.Fixed(250.dp)
                    ) {page ->
                        WeeklyRankingListItemPreview()
                    }

                }
            }



        }

        item{

            Column {

                Text(text = "전체 Top10 굿생러", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

                Spacer(modifier.size(10.dp))

                HorizontalPager(
                    state = totalPagerState
                ) {page ->
                    totalPage.intValue = page
                    TotalRankingListItem1Preview()
                }

                Spacer(modifier.size(10.dp))

            }



        }

        item{
            TotalRankingListItem2Preview()
        }

        item{
            Text(text = "닉네임 님의 굿생 인증", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

            Spacer(modifier.size(10.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                repeat(10){
                    RankingUserPostListItemPreview()
                }

            }
        }
    }
}

@Preview
@Composable
fun WeeklyRankingListItemPreview(
    modifier: Modifier = Modifier
){

    Box(
        modifier
            .padding(
                vertical = 10.dp, horizontal = 10.dp
            )
            .width(250.dp)
            .height(200.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(7.dp, shape = RoundedCornerShape(16.dp))
    ){

        Image(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            painter = painterResource(id = R.drawable.category3),
            contentDescription ="",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(4.dp, Color.White),
                        CircleShape
                    )
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.category4),
                contentDescription ="",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier.size(10.dp))

            Text(
                text = "NAME",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier.size(10.dp))

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "이번주 굿생 점수: 50",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center
                )

            }

        }

    }

}

@Preview
@Composable
fun TotalRankingListItem1Preview(
    modifier: Modifier = Modifier
){

    Box(
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){


        Image(
            modifier = modifier
                .fillMaxSize()
                .blur(
                    radiusX = 15.dp, radiusY = 15.dp
                ),
            painter = painterResource(id = R.drawable.category3),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.6f
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = modifier
                    .size(50.dp)
                    .background(color = OpaqueDark, shape = CircleShape),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "1",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Image(
                    modifier = modifier
                        .size(150.dp)
                        .clip(shape = CircleShape),
                    painter = painterResource(id = R.drawable.category4),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier.size(20.dp))

                Text(
                    text = "item.nickname",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier.size(20.dp))

                Text(
                    modifier = modifier
                        .padding(start = 20.dp, end = 20.dp),
                    text = "\"Introduce\"",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )



            }


        }


    }

}

@Preview
@Composable
fun TotalRankingListItem2Preview(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = modifier
                .weight(0.5f)
                .padding(10.dp)
                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "", tint = OrangeMain)
                Spacer(modifier.size(10.dp))
                Text(text = "굿생 점수", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
            }
            Spacer(modifier.size(10.dp))

            Text(text = "100점", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }

        Column(
            modifier = modifier
                .weight(0.5f)
                .padding(10.dp)
                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "", tint = OrangeMain)
                Spacer(modifier.size(10.dp))
                Text(text = "게시물 수", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
            }
            Spacer(modifier.size(10.dp))

            Text(text = "100개", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }
    }

}



@Preview
@Composable
fun RankingUserPostListItemPreview(
    modifier: Modifier = Modifier
){
    Row(
        modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        //대표 이미지 보일 부분
        Box(
            modifier = modifier
                .size(70.dp)
                .background(GrayWhite2, shape = RoundedCornerShape(15.dp))
        )

        Spacer(modifier.size(10.dp))

        Column {

            // 게시물 제목
            Text(text = "게시물 제목", style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier.size(5.dp))

            //날짜
            Text(text = "1일 전", style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal))

            Spacer(modifier.size(5.dp))

            //조회수, 댓글 수
            Text(text = "조회 수: 100, 댓글 수: 10", style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal))

        }


    }
}