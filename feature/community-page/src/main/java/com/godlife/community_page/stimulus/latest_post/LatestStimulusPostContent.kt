package com.godlife.community_page.stimulus.latest_post

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.navigation.StimulusPostDetailRoute
import com.godlife.community_page.stimulus.StimulusCoverItem
import com.godlife.community_page.stimulus.StimulusCoverItemPreview
import com.godlife.community_page.stimulus.StimulusLoadingScreen
import com.godlife.community_page.stimulus.StimulusPostItem
import com.godlife.community_page.stimulus.StimulusPostUiState
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.StimulusPostList
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                            color = PurpleMain
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

            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = "by ${item.nickname}",
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
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

            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = "by.${item.writer}",
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

