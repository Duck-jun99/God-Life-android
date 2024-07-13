package com.godlife.community_page.ranking

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.CommunityPageViewModel
import com.godlife.community_page.R
import com.godlife.community_page.RankingPageUiState
import com.godlife.community_page.navigation.PostDetailRoute
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.RankingBody
import kotlin.math.absoluteValue

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
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


            viewModel.getRankingUserPost(nickname = allRankingList.value[totalPage.value].nickname)

            val rankingUserPostList = viewModel.rankingUserPostList.collectAsLazyPagingItems()


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
                                WeeklyRankingListItem(
                                    Modifier
                                        .graphicsLayer {
                                            // Calculate the absolute offset for the current page from the
                                            // scroll position. We use the absolute value which allows us to mirror
                                            // any effects for both directions
                                            val pageOffset = (
                                                    (weeklyPagerState.currentPage - page) + weeklyPagerState
                                                        .currentPageOffsetFraction
                                                    ).absoluteValue

                                            // We animate the alpha, between 50% and 100%
                                            alpha = lerp(
                                                start = 0.5f,
                                                stop = 1f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            )

                                        }
                                    ,weeklyRankingListItem = weeklyRankingList.value[page],
                                    parentNavController = parentNavController
                                )
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
                            TotalRankingListItem1(
                                totalRankingListItem = allRankingList.value[page],
                                index = page,
                                parentNavController = parentNavController
                            )
                        }

                        Spacer(modifier.size(10.dp))

                    }



                }



                item{
                    TotalRankingListItem2(
                        totalRankingListItem = allRankingList.value[totalPage.intValue],
                        rankingUserPostList = rankingUserPostList
                    )
                }



                item{
                    Text(text = "${allRankingList.value[totalPage.intValue].nickname} 님의 굿생 인증", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp), textAlign = TextAlign.Center)

                    Spacer(modifier.size(10.dp))

                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {

                        for(i in 0 until rankingUserPostList.itemCount){
                            RankingUserPostListItem(
                                rankingUserPostListItem = rankingUserPostList[i],
                                navController = navController
                            )
                        }


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

    val bitmapBack: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val bitmapProfile: MutableState<Bitmap?> = remember { mutableStateOf(null) }

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
                shape = RoundedCornerShape(16.dp)
            )
            .clickable{ parentNavController.navigate("${"ProfileScreen"}/${weeklyRankingListItem.memberId}")}
    ){

        Glide.with(LocalContext.current)
            .asBitmap()
            .load(if(weeklyRankingListItem.backgroundUrl.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + weeklyRankingListItem.backgroundUrl else R.drawable.category3)
            .error(R.drawable.category3)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapBack.value = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        bitmapBack.value?.asImageBitmap()?.let { fetchedBitmap ->
            Image(
                bitmap = fetchedBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier.size(10.dp))

            Glide.with(LocalContext.current)
                .asBitmap()
                .load(if(weeklyRankingListItem.profileURL.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + weeklyRankingListItem.profileURL else R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmapProfile.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            bitmapProfile.value?.asImageBitmap()?.let { fetchedBitmap ->
                Image(
                    bitmap = fetchedBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(4.dp, Color.White),
                            CircleShape
                        )
                        .padding(4.dp),
                )
            }


            Spacer(modifier.size(10.dp))

            Text(
                text = weeklyRankingListItem.nickname,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier.size(10.dp))

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    text = weeklyRankingListItem.whoAmI,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    text = "이번주 굿생 점수: ${weeklyRankingListItem.godLifeScore}",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    ),
                    textAlign = TextAlign.Center
                )

            }

        }

    }

}

@Composable
fun TotalRankingListItem1(
    modifier: Modifier = Modifier,
    index: Int,
    totalRankingListItem: RankingBody,
    parentNavController: NavController
){

    val bitmapBack: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val bitmapProfile: MutableState<Bitmap?> = remember { mutableStateOf(null) }


    Box(
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(Color.Black)
            .clickable { parentNavController.navigate("${"ProfileScreen"}/${totalRankingListItem.memberId}") },
        contentAlignment = Alignment.Center
    ){

        Glide.with(LocalContext.current)
            .asBitmap()
            .load(if(totalRankingListItem.backgroundUrl.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + totalRankingListItem.backgroundUrl else R.drawable.category3)
            .error(R.drawable.category3)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapBack.value = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        bitmapBack.value?.asImageBitmap()?.let { fetchedBitmap ->
            Image(
                bitmap = fetchedBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize()
                    .blur(
                        radiusX = 15.dp, radiusY = 15.dp
                    )
            )
        }

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
                    text = "${index+1}",
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

                Glide.with(LocalContext.current)
                    .asBitmap()
                    .load(if(totalRankingListItem.profileURL.isNotEmpty()) BuildConfig.SERVER_IMAGE_DOMAIN + totalRankingListItem.profileURL else R.drawable.category3)
                    .error(R.drawable.category3)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            bitmapProfile.value = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })

                bitmapProfile.value?.asImageBitmap()?.let { fetchedBitmap ->
                    Image(
                        bitmap = fetchedBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(150.dp)
                            .clip(shape = CircleShape),
                    )
                }


                Spacer(modifier.size(20.dp))

                Text(
                    text = totalRankingListItem.nickname,
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
                    text = "\"${totalRankingListItem.whoAmI}\"",
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

@Composable
fun TotalRankingListItem2(
    modifier: Modifier = Modifier,
    totalRankingListItem: RankingBody,
    rankingUserPostList: LazyPagingItems<PostDetailBody>?
){

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ){
        item{
            Column(
                modifier = modifier
                    .padding(10.dp)
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .size(width = 300.dp, height = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "", tint = PurpleMain)
                    Spacer(modifier.size(10.dp))
                    Text(text = "굿생 점수", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
                }
                Spacer(modifier.size(10.dp))

                Text(text = "${totalRankingListItem.godLifeScore}점", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

            }
        }

        item{
            Column(
                modifier = modifier
                    .padding(10.dp)
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .size(width = 300.dp, height = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "", tint = PurpleMain)
                    Spacer(modifier.size(10.dp))
                    Text(text = "굿생 인증 게시물", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
                }
                Spacer(modifier.size(10.dp))

                if (rankingUserPostList != null) {
                    Text(text = "${rankingUserPostList.itemCount}개", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }

            }
        }
    }

}

@Composable
fun RankingUserPostListItem(
    modifier: Modifier = Modifier,
    rankingUserPostListItem: PostDetailBody?,
    navController: NavController
){
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

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

            Glide.with(LocalContext.current)
                .asBitmap()
                .load(if(rankingUserPostListItem.imagesURL?.isNotEmpty() == true) BuildConfig.SERVER_IMAGE_DOMAIN + rankingUserPostListItem.imagesURL!![0] else R.drawable.category3)
                .error(R.drawable.category3)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmap.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            //대표 이미지 보일 부분
            bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
                Image(
                    bitmap = fetchedBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(70.dp)
                        .clip(shape = RoundedCornerShape(15.dp)),
                )
            }

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

    val weeklyPagerState = rememberPagerState(initialPage = 0, pageCount = {10})
    val totalPagerState = rememberPagerState(initialPage = 0, pageCount = {10})

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
                        WeeklyRankingListItemPreview(
                            Modifier
                                .graphicsLayer {
                                    // Calculate the absolute offset for the current page from the
                                    // scroll position. We use the absolute value which allows us to mirror
                                    // any effects for both directions
                                    val pageOffset = (
                                            (weeklyPagerState.currentPage - page) + weeklyPagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue

                                    // We animate the alpha, between 50% and 100%
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )

                                }
                        )
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
            .height(400.dp)
            .shadow(7.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
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

            Spacer(modifier.size(10.dp))

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
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier.size(10.dp))

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = OpaqueDark, shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    text = "Introduce",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    text = "이번주 굿생 점수: 50",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
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
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ){
        item{
            Column(
                modifier = modifier
                    .padding(10.dp)
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .size(width = 300.dp, height = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "", tint = PurpleMain)
                    Spacer(modifier.size(10.dp))
                    Text(text = "굿생 점수", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
                }
                Spacer(modifier.size(10.dp))

                Text(text = "100점", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

            }
        }

        item{
            Column(
                modifier = modifier
                    .padding(10.dp)
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .size(width = 300.dp, height = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "", tint = PurpleMain)
                    Spacer(modifier.size(10.dp))
                    Text(text = "굿생 인증 게시물", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Normal))
                }
                Spacer(modifier.size(10.dp))

                Text(text = "100개", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

            }
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