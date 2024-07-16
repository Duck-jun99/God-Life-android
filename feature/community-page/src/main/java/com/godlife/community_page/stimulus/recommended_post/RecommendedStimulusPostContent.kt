package com.godlife.community_page.stimulus.recommended_post

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
import com.godlife.designsystem.theme.PurpleMain
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecommendedStimulusPostContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecommendedStimulusPostViewModel = hiltViewModel()
){

    val item = viewModel.recommendedPost.collectAsState().value

    val uiState = viewModel.uiState.collectAsState().value

    when(uiState){
        is StimulusPostUiState.Loading -> {
            StimulusLoadingScreen()
        }
        is StimulusPostUiState.Success -> {
            //Log.e("RecommendedStimulusPostContent", item.size.toString())

            var width by remember {
                mutableStateOf(0.dp)
            }

            val localDensity = LocalDensity.current


            var initialPage by remember { mutableIntStateOf(0) }

            val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { item.size })

            LaunchedEffect(key1 = true) {

                //initialPage = Int.MAX_VALUE / 2

                while (initialPage % item.size != 0) {
                    initialPage++
                }
                pagerState.scrollToPage(initialPage)
            }

            LaunchedEffect(key1 = pagerState.currentPage) {
                launch {
                    while (true) {
                        delay(2000L)

                        withContext(NonCancellable) {

                            pagerState.animateScrollToPage(pagerState.currentPage + 1)

                        }
                    }
                }
            }

            Box(){

                HorizontalPager(
                    state = pagerState,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .onGloballyPositioned {
                            width = with(localDensity) {
                                it.size.width.toDp()
                            }
                        }
                ) {index ->

                    item.getOrNull(
                        index% (item.size)
                    )?.let { item ->

                        Box(
                            modifier = modifier
                                .fillMaxHeight()
                                .width(width)
                                .background(Color.Black)
                                .clickable { navController?.navigate(StimulusPostDetailRoute.route) },
                            contentAlignment = Alignment.Center
                        ){

                            val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

                            Glide.with(LocalContext.current)
                                .asBitmap()
                                .load(BuildConfig.SERVER_IMAGE_DOMAIN + item.thumbnailUrl)
                                .error(R.drawable.category3)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        bitmap.value = resource
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {}
                                })

                            bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
                                Image(
                                    bitmap = fetchedBitmap,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = modifier
                                        .fillMaxSize()
                                        .blur(
                                            radiusX = 15.dp, radiusY = 15.dp
                                        )
                                )   //bitmap이 없다면
                            } ?: Image(
                                painter = painterResource(id = R.drawable.category3),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = modifier
                                    .fillMaxSize()
                                    .blur(
                                        radiusX = 15.dp, radiusY = 15.dp
                                    )
                            )


                            Column(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .width(220.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                StimulusCoverItem(item = item)

                                Spacer(modifier.size(5.dp))

                                Text(
                                    text = item.introduction,
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                )

                                Spacer(modifier.size(5.dp))

                                HorizontalDivider()

                                Spacer(modifier.size(5.dp))

                                Text(
                                    text = "by.${item.nickname}",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                )

                            }

                        }

                    }

                }

                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(10.dp)
                        )
                    }
                }

            }
        }
        is StimulusPostUiState.Error -> {

        }
    }

}

@Preview
@Composable
fun RecommendedStimulusPostContentPreview(
    modifier: Modifier = Modifier,
    navController: NavController? = null
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
        )
    )

    var width by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current


    var initialPage by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { item.size })

    LaunchedEffect(key1 = Unit) {

        //initialPage = Int.MAX_VALUE / 2

        while (initialPage % item.size != 0) {
            initialPage++
        }
        pagerState.scrollToPage(initialPage)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        launch {
            while (true) {
                delay(2000L)

                withContext(NonCancellable) {

                    pagerState.animateScrollToPage(pagerState.currentPage + 1)

                }
            }
        }
    }

    Box(){

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .onGloballyPositioned {
                    width = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
        ) {index ->

            item.getOrNull(
                index% (item.size)
            )?.let { item ->

                Box(
                    modifier = modifier
                        .fillMaxHeight()
                        .width(width)
                        .background(Color.Black)
                        .clickable { navController?.navigate(StimulusPostDetailRoute.route) },
                    contentAlignment = Alignment.Center
                ){


                    Image(
                        modifier = modifier
                            .fillMaxSize()
                            .blur(
                                radiusX = 15.dp, radiusY = 15.dp
                            ),
                        painter = painterResource(id = item.coverImg),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        alpha = 0.7f
                    )

                    Column(
                        modifier = modifier
                            .fillMaxHeight()
                            .width(220.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        StimulusCoverItemPreview(item = item)

                        Spacer(modifier.size(5.dp))

                        Text(
                            text = item.introText,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier.size(5.dp))

                        HorizontalDivider()

                        Spacer(modifier.size(5.dp))

                        Text(
                            text = "by.${item.writer}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        )

                    }

                }

            }

        }

        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }

    }

}