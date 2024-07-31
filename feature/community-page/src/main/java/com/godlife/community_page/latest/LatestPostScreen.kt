package com.godlife.community_page.latest

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.CommunityPageViewModel
import com.godlife.community_page.R
import com.godlife.community_page.navigation.PostDetailRoute
import com.godlife.designsystem.component.shimmerEffect
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.PostDetailBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun LatestPostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CommunityPageViewModel
){

    //val viewModel = hiltViewModel<LatestPostViewModel>()

    //val postList = viewModel.getLatestPost().collectAsLazyPagingItems()
    val postList = viewModel.latestPostList.collectAsLazyPagingItems()

    GodLifeTheme {

        LazyColumn(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ) {

            items(postList.itemCount){
                postList[it]?.let { it1 -> LatestPostListView(item = it1, navController = navController) }
            }

            /*
            for(i in 0 until postList.itemCount){
                postList[i]?.let { item ->

                    Box(modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp)){
                        LatestPostListView(item = item, navController = navController)
                    }

                }
            }

             */


        }


    }
}

@Composable
fun LatestPostListView(
    item: PostDetailBody,
    navController: NavController,
    modifier: Modifier = Modifier
){

    val postId = item.board_id.toString()

    Column(
        modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp, top = 5.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .clickable {
                navController.navigate("${PostDetailRoute.route}/$postId") {
                    popUpTo(PostDetailRoute.route)
                    launchSingleTop = true
                }
            }
    ){
        Row(
            modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                modifier.weight(0.7f),
                verticalAlignment = Alignment.CenterVertically
            ){

                //프로필 이미지 부분
                GlideImage(
                    imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + item.profileURL },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .size(30.dp, 30.dp)
                        .clip(CircleShape)
                        .background(GrayWhite2)
                        .fillMaxSize(),
                    loading = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )

                    },
                    failure = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                )

                Spacer(modifier.size(10.dp))

                Text(text = item.nickname, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                Spacer(modifier.size(10.dp))

                //티어 보여줄 부분
                //Text(text = item.tier, style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))
            }
            Text(
                modifier = modifier
                    .weight(0.3f)
                    .padding(end = 10.dp),
                text = item.writtenAt,
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.End
            )
        }

        Box(
            modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
        ){
            Text(text = item.title)
        }

        //게시물 대표 사진 보여질 곳
        if(item.imagesURL?.isNotEmpty() == true){

            GlideImage(
                imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + item.imagesURL?.get(0) },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = modifier
                    .height(500.dp)
                    .fillMaxSize()
                    .background(color = Color.Black),
                loading = {
                    Image(
                        painter = painterResource(id = R.drawable.category3),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )

                },
                failure = {
                    Image(
                        painter = painterResource(id = R.drawable.category3),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
            )
        }

        Box(
            modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "조회수: ${item.views}    댓글: ${item.commentCount}개")
        }
    }
}

@Preview
@Composable
fun LoadingLatestPostScreen(modifier: Modifier = Modifier){
    GodLifeTheme {

        LazyColumn(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ) {
            items(3) {

                Column(
                    modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp, top = 5.dp)
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(15.dp))
                ){
                    Row(
                        modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = modifier
                                .size(30.dp, 30.dp)
                                .clip(CircleShape)
                                .fillMaxSize()
                                .shimmerEffect()
                        )


                        Spacer(modifier.size(10.dp))

                        Box(modifier = modifier
                            .size(width = 200.dp, height = 18.dp)
                            .shimmerEffect()
                        )



                    }

                    Box(
                        modifier
                            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                            .size(width = 200.dp, height = 18.dp)
                            .shimmerEffect()
                    )

                    Box(
                        modifier
                            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                            .size(width = 100.dp, height = 18.dp)
                            .shimmerEffect()
                    )
                }

            }

        }
    }
}


@Preview
@Composable
fun LatestPostScreenPreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ) {
            Box(modifier.padding(start = 20.dp, end = 20.dp)){
                Text(text = "따끈따끈 최신 게시물이에요.", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
            }

            LazyColumn {

                item { Spacer(modifier = modifier.size(20.dp))}

                items(5) {
                    LatestPostListPreview()
                    Spacer(modifier.size(20.dp))
                }

            }

        }

    }
}


@Preview
@Composable
fun LatestPostListPreview(modifier: Modifier = Modifier){

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
                        .size(50.dp)
                        .background(Color.Gray)){
                    Text(text = "Image")
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

        Box(
            modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()){
            Text(text = "Title")
        }

        //게시물 대표 사진 보여질 곳
        Box(
            modifier
                .background(PurpleMain)
                .height(500.dp)
                .fillMaxWidth()){

            Text(text = "Image")
        }

        Box(
            modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.CenterStart){
            Text(text = "조회수: 10    댓글: 1개")
        }

    }
}

