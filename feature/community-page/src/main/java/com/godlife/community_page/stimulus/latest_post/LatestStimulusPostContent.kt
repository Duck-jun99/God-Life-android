package com.godlife.community_page.stimulus.latest_post

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.navigation.StimulusPostDetailRoute
import com.godlife.community_page.stimulus.StimulusLoadingScreen
import com.godlife.community_page.stimulus.StimulusPostItem
import com.godlife.community_page.stimulus.StimulusPostUiState
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.network.model.StimulusPostList
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

/*
TODO : LazyHorizontalGrid를 페이저로 변경해야 함.
 */
@Composable
fun LatestStimulusPostContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LatestStimulusPostViewModel = hiltViewModel()
){

    val postList = viewModel.latestPostList.collectAsLazyPagingItems()

    val uiState = viewModel.uiState.collectAsState().value

    when(uiState){
        is StimulusPostUiState.Loading -> {
            StimulusLoadingScreen()
        }
        is StimulusPostUiState.Success -> {

            LazyHorizontalGrid(
                modifier = modifier
                    .fillMaxWidth()
                    .height(500.dp),
                rows = GridCells.Fixed(3)
            ) {

                items(postList.itemCount){
                    postList[it]?.let { it1 -> LatestStimulusItem(item = it1, navController = navController)  }
                }
            }
        }
        is StimulusPostUiState.Error -> {

        }
    }

}

@Composable
fun LatestStimulusItem(
    modifier: Modifier = Modifier,
    item: StimulusPostList,
    navController: NavController
){

    val postId = item.boardId.toString()

    Row(
        modifier = modifier
            .size(height = 150.dp, width = 300.dp)
            .background(Color.White)
            .padding(10.dp)
            .clickable { navController.navigate("${StimulusPostDetailRoute.route}/$postId") },
        verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = modifier
                .padding(10.dp)
                .size(width = 100.dp, height = 150.dp)
                .shadow(10.dp),
            contentAlignment = Alignment.Center
        ){

            GlideImage(
                imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + item.thumbnailUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = modifier
                    .fillMaxSize(),
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


            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .background(color = OpaqueDark)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                contentAlignment = Alignment.Center
            ){

                Text(text = item.title,
                    style = TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis
                )

            }

        }

        Spacer(modifier.size(10.dp))

        Column(

        ) {
            Text(
                text = item.introduction,
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

            Row(
                modifier = modifier
                    .height(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "",
                    tint = GrayWhite
                )

                Spacer(modifier.width(2.dp))


                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = item.nickname,
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

            }

            Spacer(modifier.size(5.dp))

            Row(
                modifier = modifier
                    .height(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    painter = painterResource(id = R.drawable.visibility_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "",
                    tint = GrayWhite
                )

                Spacer(modifier.width(2.dp))


                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = item.view.toString(),
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

            }
        }
    }
}


@Preview
@Composable
fun LatestStimulusPostListPreview(
    modifier: Modifier = Modifier
){

    val item = listOf(
        StimulusPostItem(
            title = "이것은 제목!",
            writer = "치킨 러버",
            coverImg = R.drawable.category3,
            introText = "대충 이러이러한 내용이라고 소개하는 내용"
        ),
        StimulusPostItem(
            title = "나도 제목!",
            writer = "초코 러버",
            coverImg = R.drawable.category4,
            introText = "대충 이러이러한 내용이라고 소개하는 내용"
        ),
        StimulusPostItem(
            title = "제목 등장",
            writer = "라면 좋아",
            coverImg = R.drawable.category3,
            introText = "대충 이러이러한 내용이라고 소개하는 내용"
        ),
        StimulusPostItem(
            title = "제모오오오옥",
            writer = "헬로우",
            coverImg = R.drawable.category4,
            introText = "대충 이러이러한 내용이라고 소개하는 내용"
        ),
        StimulusPostItem(
            title = "제목 등장",
            writer = "라면 좋아",
            coverImg = R.drawable.category3,
            introText = "대충 이러이러한 내용이라고 소개하는 내용"
        ),
        StimulusPostItem(
            title = "제모오오오옥",
            writer = "헬로우",
            coverImg = R.drawable.category4,
            introText = "대충 이러이러한 내용이라고 소개하는 내용"
        )
    )

    LazyHorizontalGrid(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp),
        rows = GridCells.Fixed(3)
    ) {
        itemsIndexed(item){index, it ->
            LatestStimulusItemPreview(item = it)
        }
    }
}

@Preview
@Composable
fun LatestStimulusItemPreview(
    modifier: Modifier = Modifier,
    item: StimulusPostItem = StimulusPostItem(title = "이것이 제목이다", writer = "치킨 러버", coverImg = R.drawable.category3, introText = "갓생을 살고 싶어하는 당신을 위해 작성한 글!")
){
    Row(
        modifier = modifier
            .size(height = 150.dp, width = 300.dp)
            .background(Color.White)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = modifier
                .size(100.dp),
            painter = painterResource(id = item.coverImg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier.size(10.dp))

        Column(

        ) {
            Text(
                text = item.introText,
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

            Row(
                modifier = modifier
                    .height(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "",
                    tint = GrayWhite
                )

                Spacer(modifier.width(2.dp))


                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = item.writer,
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                )

            }

            Spacer(modifier.size(5.dp))

            Row(
                modifier = modifier
                    .height(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "",
                    tint = GrayWhite
                )

                Spacer(modifier.width(2.dp))


                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = "100",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                )

            }


        }
    }
}

