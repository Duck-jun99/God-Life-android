package com.godlife.community_page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.designsystem.component.CommunityFamousPostList
import com.godlife.designsystem.component.CommunityLatestPostList
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite2
import com.godlife.designsystem.theme.GreyWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.FamousPostItem
import com.godlife.model.community.LatestPostItem
import com.godlife.model.community.TagItem

@Composable
fun CommunityPageScreen(
    navController: NavController,
    viewModel: CommunityPageViewModel = hiltViewModel()

) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {

    }

    CommunityPageView(navController)

}

@Composable
fun CommunityPageView(navController: NavController, modifier: Modifier = Modifier){

    GodLifeTheme {


        LazyColumn(
            modifier
                .fillMaxSize()
                .background(GreyWhite3)
        ) {
            item {
                Surface(shadowElevation = 7.dp) {
                    Box(
                        modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(text = "Guset님! 다른 갓생러들은 어떻게 살고 있을까요?", style = GodLifeTypography.titleSmall)
                    }
                }
            }

            item { Spacer(modifier.size(8.dp)) }

            item { CategoryView(navController) }

            item { Spacer(modifier.size(8.dp)) }

            item { FamousPostPreview() }

            item { Spacer(modifier.size(8.dp)) }

            //item { LatestPostPreview() }



            item{

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

                    }
                }
            }

            val latestPostItem: List<LatestPostItem> = listOf(LatestPostItem(name = "Name1", title = "Title1", rank = "마스터", tagItem = listOf(
                TagItem("TAG1"), TagItem("TAG2")
            )),
                LatestPostItem(name = "Name2", title = "Title2", rank = "마스터", tagItem = listOf(TagItem("TAG1"), TagItem("TAG2"))))

            itemsIndexed(latestPostItem) { index, item ->
                CommunityLatestPostList(latestPostItem = item)
            }
        }


    }
}


@Composable
fun CategoryView(navController: NavController, modifier: Modifier = Modifier){
    Column(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 30.dp,
                bottom = 30.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier.fillMaxWidth()
        ){
            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "인기 게시물", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }

            Button(onClick = { navController.navigate(LatestPostScreenRoute.route) },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "최신 게시물", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }
        }

        Spacer(modifier = modifier.size(30.dp))

        Row(
            modifier.fillMaxWidth()
        ){
            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "갓생 자극", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }

            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "명예의 전당", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }
        }

    }
}

object LatestPostScreenRoute{
    const val route = "LatestPostScreen"
}

@Preview(showBackground = true)
@Composable
fun CommunityPagePreview(modifier: Modifier = Modifier){

    GodLifeTheme {


        LazyColumn(
            modifier
                .fillMaxSize()
                .background(GreyWhite3)
        ) {
            item {
                Surface(shadowElevation = 7.dp) {
                    Box(
                        modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(text = "Guset님! 다른 갓생러들은 어떻게 살고 있을까요?", style = GodLifeTypography.titleSmall)
                    }
                }
            }

            item { Spacer(modifier.size(8.dp)) }

            item { CategoryPreview() }

            item { Spacer(modifier.size(8.dp)) }

            item { FamousPostPreview() }

            item { Spacer(modifier.size(8.dp)) }

            //item { LatestPostPreview() }



            item{

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

                    }
                }
            }

            val latestPostItem: List<LatestPostItem> = listOf(LatestPostItem(name = "Name1", title = "Title1", rank = "마스터", tagItem = listOf(
                TagItem("TAG1"), TagItem("TAG2")
            )),
                LatestPostItem(name = "Name2", title = "Title2", rank = "마스터", tagItem = listOf(TagItem("TAG1"), TagItem("TAG2"))))

            itemsIndexed(latestPostItem) { index, item ->
                CommunityLatestPostList(latestPostItem = item)
            }
        }


    }
}

@Preview
@Composable
fun CategoryPreview(modifier: Modifier = Modifier){
    Column(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 30.dp,
                bottom = 30.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier.fillMaxWidth()
        ){
            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "인기 게시물", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }

            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "최신 게시물", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }
        }

        Spacer(modifier = modifier.size(30.dp))

        Row(
            modifier.fillMaxWidth()
        ){
            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "갓생 자극", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }

            Button(onClick = { /*TODO*/ },
                modifier
                    .size(150.dp, 50.dp)
                    .weight(0.5f)
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(7.dp)) {

                Text(text = "명예의 전당", style = GodLifeTypography.bodyLarge.copy(color = PurpleMain))
            }
        }

    }
}

@Preview
@Composable
fun FamousPostPreview(modifier: Modifier = Modifier){

    var famousPost =
        listOf(FamousPostItem(
            "Name1", "Title1", "Text1", rank = "마스터", tagItem = listOf(TagItem("Tag1"), TagItem("Tag2"))),
            FamousPostItem(
                "Name2", "Title2", "Text2", rank = "실버", tagItem = listOf(TagItem("Tag1"), TagItem("Tag2")))
        )

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

            Text(text = "실시간 인기 갓생 인증글을 확인해보세요!", style = GodLifeTypography.titleSmall)

            Spacer(modifier.size(10.dp))

            LazyRow {
                itemsIndexed(famousPost) { index, item ->
                    CommunityFamousPostList(famousPostItem = item)
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

@Preview
@Composable
fun LatestPostPreview(modifier: Modifier = Modifier){

    val latestPostItem: List<LatestPostItem> = listOf(LatestPostItem(name = "Name1", title = "Title1", rank = "마스터", tagItem = listOf(
        TagItem("TAG1"), TagItem("TAG2")
    )),
        LatestPostItem(name = "Name2", title = "Title2", rank = "마스터", tagItem = listOf(TagItem("TAG1"), TagItem("TAG2"))))
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
