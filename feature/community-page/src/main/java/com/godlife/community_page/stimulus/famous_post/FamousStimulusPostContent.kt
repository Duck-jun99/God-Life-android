package com.godlife.community_page.stimulus.famous_post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.ThumbUp
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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

@Composable
fun FamousStimulusPostContent(
    navController: NavController,
    viewModel: FamousStimulusPostViewModel = hiltViewModel()
){

    val item = viewModel.postList.collectAsState().value

    val uiState = viewModel.uiState.collectAsState().value

    when(uiState){
        is StimulusPostUiState.Loading -> {
            StimulusLoadingScreen()
        }
        is StimulusPostUiState.Success -> {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                itemsIndexed(item){index, it ->

                    if (it != null) {
                        FamousStimulusItem(
                            item = it,
                            navController = navController
                        )
                    }

                }
            }

        }
        is StimulusPostUiState.Error -> {

        }
    }

}

@Composable
fun FamousStimulusItem(
    modifier: Modifier = Modifier,
    item: StimulusPostList,
    navController: NavController
){
    Column(
        modifier = modifier
            .size(width = 300.dp, height = 350.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .padding(10.dp)
                .size(width = 200.dp, height = 250.dp)
                .shadow(10.dp)
                .clickable { navController.navigate("${StimulusPostDetailRoute.route}/${item.boardId}") },
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
                    .padding(30.dp)
                    .background(color = OpaqueDark)
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                contentAlignment = Alignment.Center
            ){

                Text(text = item.title,
                    style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )

            }

        }

        Spacer(modifier.size(5.dp))

        Text(
            text = item.introduction,
            style = TextStyle(
                color = GrayWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier.size(5.dp))

        HorizontalDivider(modifier.width(200.dp))

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
                text = item.nickname,
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Spacer(modifier.width(5.dp))

            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "",
                tint = GrayWhite
            )

            Spacer(modifier.width(2.dp))

            Text(
                text = item.godLifeScore.toString(),
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
fun FamousStimulusPostContentPreview(
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

    LazyRow {
        itemsIndexed(item){index, it ->

            FamousStimulusItemPreview(item = it)

        }
    }
}

@Preview
@Composable
fun FamousStimulusItemPreview(
    modifier: Modifier = Modifier,
    item: StimulusPostItem = StimulusPostItem(title = "이것이 제목이다", writer = "치킨 러버", coverImg = R.drawable.category3, introText = "갓생을 살고 싶어하는 당신을 위해 작성한 글!")
){
    Column(
        modifier = modifier
            .size(width = 300.dp, height = 350.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .padding(10.dp)
                .size(width = 200.dp, height = 250.dp)
                .shadow(10.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                modifier = modifier
                    .fillMaxWidth(),
                painter = painterResource(id = item.coverImg),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .background(color = OpaqueDark)
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                contentAlignment = Alignment.Center
            ){

                Text(text = item.title,
                    style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )

            }

        }

        Spacer(modifier.size(5.dp))

        Text(
            text = item.introText,
            style = TextStyle(
                color = GrayWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier.size(5.dp))

        HorizontalDivider(modifier.width(200.dp))

        Spacer(modifier.size(5.dp))

        Text(
            text = "by ${item.writer}",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )

    }

}