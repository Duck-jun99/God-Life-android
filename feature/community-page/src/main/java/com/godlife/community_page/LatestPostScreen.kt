package com.godlife.community_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.godlife.community_page.navigation.LatestPostViewModel
import com.godlife.designsystem.component.CommunityLatestPostList
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite3
import com.godlife.model.community.LatestPostItem
import com.godlife.model.community.TagItem

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

        items(postList.itemCount) { index ->
            postList[index]?.let { item ->
                LatestPostListView()
                Divider(color = Color.Gray, thickness = 0.5.dp)
            }
        }

    }
}


@Preview
@Composable
fun LatestPostListView(modifier: Modifier = Modifier){

    val latestPostItem: List<LatestPostItem> = listOf(
        LatestPostItem(name = "Name1", title = "Title1", rank = "마스터", tagItem = listOf(
        TagItem("TAG1"), TagItem("TAG2")
    )),
        LatestPostItem(name = "Name2", title = "Title2", rank = "마스터", tagItem = listOf(TagItem("TAG1"), TagItem("TAG2")))
    )
    Box(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 10.dp
            ),
        contentAlignment = Alignment.CenterStart
    ){
        Column {

            Box(modifier.padding(start = 10.dp)){
                Text(text = "따끈따끈 최신 게시물", style = GodLifeTypography.titleSmall)
            }

            Spacer(modifier.size(10.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.height(300.dp)
            ) {
                itemsIndexed(latestPostItem) { index, item ->
                    CommunityLatestPostList(latestPostItem = item)
                }
            }



            Spacer(modifier.size(10.dp))

            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = modifier.size(30.dp)
            ) {
                Text(text = "> 더보기", style = GodLifeTypography.titleSmall)
            }


        }
    }
}
