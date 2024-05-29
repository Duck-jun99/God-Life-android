package com.godlife.community_page

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.navigation.LatestPostViewModel
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.GreyWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.PostDetailBody

@Preview(showBackground = true)
@Composable
fun LatestPostScreen(modifier: Modifier = Modifier){
    GodLifeTheme {
        Column(
            modifier
                .fillMaxSize()
                .background(GreyWhite3)
        ) {
            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = "따끈따끈 최신 게시물을 볼까요?", style = GodLifeTypography.titleSmall)
                }
            }

            PagingListScreen()
        }
    }
}

@Composable
fun PagingListScreen() {
    val viewModel = hiltViewModel<LatestPostViewModel>()

    val postList = viewModel.getLatestPost().collectAsLazyPagingItems()

    LazyColumn {

        item { Spacer(modifier = Modifier.size(20.dp))}

        items(postList.itemCount) { index ->
            postList[index]?.let { item ->
                LatestPostListView(item)
                Divider(color = Color.Gray, thickness = 0.5.dp)
            }
        }

    }
}



@Composable
fun LatestPostListView(item: PostDetailBody, modifier: Modifier = Modifier){
    GodLifeTheme(
        modifier
            .height(375.dp)
            .background(GreyWhite3)
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(375.dp)
                .drawBehind {
                    val shadowColor = Color(0x1F000000) // 원하는 그림자 색상 설정
                    val transparent = Color.Transparent

                    val brush = Brush.verticalGradient(
                        colors = listOf(transparent, shadowColor),
                        startY = size.height - 50.dp.toPx(), // 그림자가 시작되는 위치 설정
                        endY = size.height
                    )

                    drawRect(brush)
                }
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(360.dp)
            ){
                Row(
                    modifier
                        .weight(0.2f)
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row(
                        modifier.weight(0.8f),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        //프로필 이미지 부분

                        val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                        val imageModifier: Modifier = modifier
                            .size(50.dp, 50.dp)
                            .clip(CircleShape)
                            .fillMaxSize()
                            .background(color = GreyWhite)

                        if(item.profileURL != ""){
                            Glide.with(LocalContext.current)
                                .asBitmap()
                                .load(item.profileURL)
                                .error(R.drawable.ic_person)
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
                                    modifier = imageModifier
                                )   //bitmap이 없다면
                            } ?: Image(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = imageModifier
                            )
                        }

                        else{
                            Glide.with(LocalContext.current)
                                .asBitmap()
                                .load(R.drawable.ic_person)
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
                                    modifier = imageModifier
                                )   //bitmap이 없다면
                            } ?: Image(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = imageModifier
                            )
                        }





                        Spacer(modifier.size(10.dp))

                        Text(text = item.nickname, style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                        Spacer(modifier.size(10.dp))

                        //티어 보여줄 부분
                        Text(text = item.tier, style = TextStyle(color = Color.Magenta, fontWeight = FontWeight.Bold, fontSize = 15.sp))
                    }
                    Box(modifier.weight(0.2f)){
                        Text(text = item.writtenAt, style = TextStyle(color = GreyWhite, fontSize = 15.sp))
                    }
                }

                Box(
                    modifier
                        .weight(0.2f)
                        .padding(5.dp)
                        .fillMaxWidth()
                ){
                    Text(text = item.title)
                }

                //게시물 대표 사진 보여질 곳
                Box(
                    modifier
                        .background(PurpleMain)
                        .weight(0.4f)
                        .fillMaxSize()
                ){
                    Text(text = "Image")
                }

                Box(
                    modifier
                        .weight(0.2f)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "조회수: ${item.views}    댓글: ${item.commentCount}개")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LatestPostListPreview(modifier: Modifier = Modifier){
    GodLifeTheme(
        modifier
            .height(375.dp)
            .background(GreyWhite3)
    ) {
        Surface(
            shadowElevation = 7.dp
        ){
            Column(
                modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(360.dp)
            ){

                Row(
                    modifier
                        .weight(0.2f)
                        .fillMaxWidth()
                        .padding(5.dp),
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

                        Text(text = "Name", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                        Spacer(modifier.size(10.dp))

                        //티어 보여줄 부분
                        Text(text = "마스터", style = TextStyle(color = Color.Magenta, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                    }
                    Box(modifier.weight(0.2f)){
                        Text(text = "39분전", style = TextStyle(color = GreyWhite, fontSize = 15.sp))
                    }

                }

                Box(
                    modifier
                        .weight(0.2f)
                        .padding(5.dp)
                        .fillMaxWidth()){
                    Text(text = "Title")
                }



                //게시물 대표 사진 보여질 곳
                Box(
                    modifier
                        .background(PurpleMain)
                        .weight(0.4f)
                        .height(350.dp)
                        .fillMaxWidth()){

                    Text(text = "Image")
                }

                Box(
                    modifier
                        .weight(0.2f)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center){
                    Text(text = "조회수: 10    댓글: 1개")
                }


            }
        }

    }
}