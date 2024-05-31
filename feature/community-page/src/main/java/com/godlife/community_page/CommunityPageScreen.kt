package com.godlife.community_page

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godlife.community_page.famous.FamousPostPreview
import com.godlife.community_page.famous.FamousPostScreen
import com.godlife.community_page.latest.LatestPostScreen
import com.godlife.community_page.navigation.FamousPostRoute
import com.godlife.community_page.navigation.LatestPostRoute
import com.godlife.community_page.navigation.RankingRoute
import com.godlife.community_page.navigation.StimulusPostRoute
import com.godlife.community_page.ranking.RankingScreen
import com.godlife.community_page.stimulus.StimulusPostScreen
import com.godlife.designsystem.list.CommunityFamousPostList
import com.godlife.designsystem.list.CommunityLatestPostList
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.GreyWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.CategoryItem
import com.godlife.model.community.FamousPostItem
import com.godlife.model.community.LatestPostItem
import com.godlife.model.community.TagItem

@Composable
fun CommunityPageScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityPageViewModel = hiltViewModel()

) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {

    }

    val navController = rememberNavController()

    GodLifeTheme {
        Column(
            modifier
                .fillMaxSize()
                .background(GreyWhite3)
        ){
            Box(
                modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "갓생 커뮤니티", style = GodLifeTypography.titleMedium)
            }

            LazyColumn(
                modifier
                    //.fillMaxSize()
                    .background(GreyWhite3)
            ) {

                item {
                    Box(modifier.padding(start = 20.dp, end = 20.dp)){
                        Text(text = "Guest님이 원하는 정보를 찾아보세요.", style = TextStyle(color = GreyWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
                    }
                }
                item { Spacer(modifier.size(12.dp)) }


                item { CategoryBox(navController = navController) }

                item { Spacer(modifier.size(12.dp)) }

                item { CommunityPageView(navController = navController) }

            }


        }
    }



}

@Composable
fun CommunityPageView(modifier: Modifier = Modifier, navController: NavHostController){

    NavHost(navController = navController, startDestination = FamousPostRoute.route) {

        composable(FamousPostRoute.route){
            FamousPostScreen()
        }

        composable(LatestPostRoute.route) {
            LatestPostScreen()
        }

        composable(StimulusPostRoute.route) {
            StimulusPostScreen()
        }

        composable(RankingRoute.route) {
            RankingScreen()
        }
    }

    /*

    Column {

        Box(
            modifier
                .padding(start = 20.dp, end = 20.dp)
                .height(20.dp)){
            Row(modifier.fillMaxWidth()){
                Icon(painter = painterResource(R.drawable.star_icons8), contentDescription = "", tint = Color.Unspecified)
                Spacer(modifier.size(5.dp))
                Text(text = "실시간 인기 갓생 인증글을 확인해보세요!", style = TextStyle(color = GreyWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier.size(12.dp))

        FamousPostPreview()

        Spacer(modifier.size(12.dp))

    }


     */
}

@Composable
fun CategoryBox(modifier: Modifier = Modifier, navController: NavController){

    val categoryItem: List<CategoryItem> = listOf(
        CategoryItem(title = "#인기 게시물", imgPath = R.drawable.category1, route = FamousPostRoute.route),
        CategoryItem(title = "#최신 게시물", imgPath = R.drawable.category2, route = LatestPostScreenRoute.route),
        CategoryItem(title = "#갓생 자극", imgPath = R.drawable.category3, route = StimulusPostRoute.route),
        CategoryItem(title = "#명예의 전당", imgPath = R.drawable.category4, route = RankingRoute.route),
    )

    Box(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(200.dp)){

        LazyRow {
            itemsIndexed(categoryItem){index, item ->
                CategoryItem(modifier = modifier, categoryItem = item, navController)
            }

        }


    }

}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryItem: CategoryItem,
    navController: NavController
) {
    val title = categoryItem.title
    val imgPath = categoryItem.imgPath
    val route = categoryItem.route
    Box(
        modifier
            .padding(20.dp)
            .size(width = 100.dp, height = 150.dp)
            .background(shape = RoundedCornerShape(10.dp), color = GreyWhite)
            .clickable { navController.navigate(route) },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imgPath),
            contentDescription = "",
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier
                .fillMaxSize()
                .background(color = OpaqueDark, shape = RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {

            Text(
                text = title,
                style = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            )

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
        Column(
            modifier
                .fillMaxSize()
                .background(GreyWhite3)
        ) {
            Box(
                modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "갓생 커뮤니티", style = GodLifeTypography.titleMedium)
            }

            LazyColumn(
                modifier
                    .background(GreyWhite3)
            ) {

                item {
                    Box(modifier.padding(start = 20.dp, end = 20.dp)){
                        Text(text = "Guest님이 원하는 정보를 찾아보세요.", style = TextStyle(color = GreyWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
                    }
                }

                item { Spacer(modifier.size(12.dp)) }

                item { CategoryBoxPreview() }

                item { Spacer(modifier.size(12.dp)) }

                item {
                    Box(
                        modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .height(20.dp)){
                        Row(modifier.fillMaxWidth()){
                            Icon(painter = painterResource(R.drawable.star_icons8), contentDescription = "", tint = Color.Unspecified)
                            Spacer(modifier.size(5.dp))
                            Text(text = "실시간 인기 갓생 인증글을 확인해보세요!", style = TextStyle(color = GreyWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
                        }
                    }
                }

                item { Spacer(modifier.size(12.dp)) }

                item { FamousPostPreview() }

                item { Spacer(modifier.size(12.dp)) }

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
}

@Preview
@Composable
fun CategoryPreview(modifier: Modifier = Modifier){

    Box(
        modifier
            .background(Color.White)
            .fillMaxWidth()
    ){

        Column(
            modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 30.dp,
                    bottom = 30.dp
                )
                .background(Color.White),
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

    
}

@Preview
@Composable
fun CategoryBoxPreview(modifier: Modifier = Modifier){

    val categoryItem: List<CategoryItem> = listOf(
        CategoryItem(title = "#인기 게시물", imgPath = R.drawable.category1, route = ""),
        CategoryItem(title = "#최신 게시물", imgPath = R.drawable.category2, route = ""),
        CategoryItem(title = "#갓생 자극", imgPath = R.drawable.category3, route = ""),
        CategoryItem(title = "#명예의 전당", imgPath = R.drawable.category4, route = ""),
    )

    Box(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(200.dp)){

        LazyRow {
            itemsIndexed(categoryItem){index, item ->
                CategoryItemPreview(modifier = modifier, categoryItem = item)
            }

        }


    }

}

@Preview
@Composable
fun CategoryItemPreview(
    modifier: Modifier = Modifier,
    categoryItem: CategoryItem = CategoryItem(title = "#인기 게시물", imgPath = R.drawable.category1, route = ""),
) {
    val title = categoryItem.title
    val imgPath = categoryItem.imgPath
    Box(
        modifier
            .padding(20.dp)
            .size(width = 100.dp, height = 150.dp)
            .background(shape = RoundedCornerShape(10.dp), color = GreyWhite),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imgPath),
            contentDescription = "",
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier
                .fillMaxSize()
                .background(color = OpaqueDark, shape = RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {

            Text(
                text = title,
                style = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            )

        }


    }
}


/*
@Composable
fun CommunityPageView(modifier: Modifier = Modifier){

    val navController = rememberNavController()

    LazyColumn(
        modifier
            //.fillMaxSize()
            .background(GreyWhite3)
    ) {

        item {
            Box(modifier.padding(start = 20.dp, end = 20.dp)){
                Text(text = "Guest님이 원하는 정보를 찾아보세요.", style = TextStyle(color = GreyWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
            }
        }
        item { Spacer(modifier.size(12.dp)) }


        item { CategoryBox(navController = navController) }

        item { Spacer(modifier.size(12.dp)) }

        item {
            Box(
                modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .height(20.dp)){
                Row(modifier.fillMaxWidth()){
                    Icon(painter = painterResource(R.drawable.star_icons8), contentDescription = "", tint = Color.Unspecified)
                    Spacer(modifier.size(5.dp))
                    Text(text = "실시간 인기 갓생 인증글을 확인해보세요!", style = TextStyle(color = GreyWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
                }
            }
        }

        item { Spacer(modifier.size(12.dp)) }

        item { FamousPostPreview() }

        item { Spacer(modifier.size(12.dp)) }

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
 */