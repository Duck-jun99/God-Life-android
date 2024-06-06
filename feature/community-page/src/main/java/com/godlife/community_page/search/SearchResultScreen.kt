package com.godlife.community_page.search

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.godlife.designsystem.theme.GodLifeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.CommunityPageViewModel
import com.godlife.community_page.R
import com.godlife.community_page.navigation.PostDetailRoute
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.PostDetailBody

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityPageViewModel,
    navController: NavController
){

    val searchText by viewModel.searchText.collectAsState()
    val searchedPostList = viewModel.searchedPosts.collectAsLazyPagingItems()

    //검색 결과가 있으면 true, 없으면 false
    val searchResultFlag: Boolean = if(searchedPostList.itemCount == 0) false else true

    Log.e("SearchResultScreen", searchedPostList.toString())

    GodLifeTheme {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            if(searchResultFlag){

                Text(text = "${searchText} 에 대한 검색 결과를 찾았어요.", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 18.sp))

                LazyColumn(modifier.padding(10.dp)) {
                    items(searchedPostList.itemCount) {
                        SearchedPostList(navController = navController, item = searchedPostList[it]!!)
                        Spacer(modifier.height(10.dp))
                    }
                }
            }

            else{
                Text(text = "검색 결과가 없어요.", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 18.sp))
            }


        }
    }
}

@Composable
fun SearchedPostList(modifier: Modifier = Modifier, item: PostDetailBody, navController: NavController,){

    val postId = item.board_id.toString()

    Column(
        modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .clickable { navController.navigate("${PostDetailRoute.route}/$postId") }
    ){

        Row(
            modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically){

            Row(modifier.weight(0.8f),
                verticalAlignment = Alignment.CenterVertically){

                //프로필 이미지 부분
                val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                val imageModifier: Modifier = modifier
                    .size(30.dp, 30.dp)
                    .clip(CircleShape)
                    .fillMaxSize()
                    .background(color = GrayWhite)


                Glide.with(LocalContext.current)
                    .asBitmap()
                    .load(BuildConfig.SERVER_IMAGE_DOMAIN + item.profileURL)
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

                Spacer(modifier.size(10.dp))

                Text(text = item.nickname, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                Spacer(modifier.size(10.dp))

                //티어 보여줄 부분
                Text(text = item.tier, style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold, fontSize = 15.sp))

            }
            Box(modifier.weight(0.2f), contentAlignment = Alignment.TopCenter){
                Text(text = item.writtenAt, style = TextStyle(color = GrayWhite, fontSize = 15.sp))
            }

        }

        Column(
            modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()){
            Text(text = item.title, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp))

            Spacer(modifier.size(10.dp))

            Text(text = item.body, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))

        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultScreenPreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(text = "\"\"에 대한 검색 결과를 찾았어요.")

            LazyColumn(modifier.padding(10.dp)) {
                items(10) {
                    SearchedPostListPreview()
                    Spacer(modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchedPostListPreview(modifier: Modifier = Modifier){


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
                        .size(40.dp)
                        .background(Color.Gray, shape = CircleShape)){
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

        Column(
            modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()){
            Text(text = "Title", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp))

            Spacer(modifier.size(10.dp))

            Text(text = "Text", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))

        }

    }
}
