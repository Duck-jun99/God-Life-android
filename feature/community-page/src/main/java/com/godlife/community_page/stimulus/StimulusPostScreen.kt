package com.godlife.community_page.stimulus

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.search.stimulus.StimulusSearchScreen
import com.godlife.community_page.stimulus.famous_post.FamousStimulusPostContent
import com.godlife.community_page.stimulus.latest_post.LatestStimulusPostContent
import com.godlife.community_page.stimulus.most_view_post.MostViewStimulusPostContent
import com.godlife.community_page.stimulus.recommended_author.RecommendedAuthorInfoContent
import com.godlife.community_page.stimulus.recommended_author.RecommendedAuthorInfoContentPreview
import com.godlife.community_page.stimulus.recommended_author_post.RecommendedAuthorStimulusPostContent
import com.godlife.community_page.stimulus.recommended_post.RecommendedStimulusPostContent
import com.godlife.create_post.stimulus.CreateStimulusPostScreen
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.StimulusPostList
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

data class FABItem(
    val icon: ImageVector,
    val text: String,
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StimulusPostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarVisibleState: MutableState<Boolean>,
    viewModel: StimulusPostViewModel = hiltViewModel()
){
    bottomBarVisibleState.value = true

    val navController2 = rememberNavController()

    val fabVisibleState = remember { mutableStateOf(true) }

    val itemList = listOf(
        FABItem(icon = Icons.Outlined.Create, text = "글 작성"),
        FABItem(icon = Icons.Outlined.Search, text = "검색"),
        FABItem(icon = Icons.Outlined.Info, text = "도움말"),
    )


    GodLifeTheme {
        Scaffold(
            floatingActionButton = {
                if(fabVisibleState.value){
                    /*
                    FloatingActionButton(
                        onClick = { navController2.navigate("CreateStimulusPostScreen") }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "")
                    }
                     */

                    CustomExpandableFAB(
                        items = itemList,
                        onItemClick = { item ->
                            when(item.text){
                                "글 작성" -> navController2.navigate("CreateStimulusPostScreen")
                                "검색" -> navController2.navigate("StimulusSearchScreen")
                                "도움말" -> viewModel.setHelpDialogVisible()
                            }
                        },
                        viewModel = viewModel
                    )
                }

            }
        ) {

            NavHost(navController = navController2, startDestination = "StimulusPostScreen"){

                composable("StimulusPostScreen"){
                    fabVisibleState.value = true
                    LazyColumn(
                        modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                        item {
                            RecommendedStimulusPostContent(navController = navController)
                        }


                        item {

                            Text(
                                modifier = modifier
                                    .padding(20.dp),
                                text = "인기 글을 읽어보세요.",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                        }

                        item {
                            FamousStimulusPostContent(navController = navController)
                        }

                        item {

                            Text(
                                modifier = modifier
                                    .padding(20.dp),
                                text = "따끈따끈, 최신글",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                        }

                        item {
                            LatestStimulusPostContent(
                                navController = navController
                            )
                        }

                        item{
                            Text(
                                modifier = modifier
                                    .padding(20.dp),
                                text = "많이 본 글이에요.",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        item{
                            MostViewStimulusPostContent(
                                navController = navController
                            )
                        }

                        item {
                            RecommendedAuthorInfoContent(
                                navController = navController
                            )
                        }

                        /*
                        //RecommendedAuthorInfoContent로 이전
                        item {

                            Text(
                                modifier = modifier
                                    .padding(20.dp),
                                text = "추천 작가님의 게시물",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                        }

                        item {
                            RecommendedAuthorStimulusPostContent(
                                navController = navController
                            )
                        }

                         */


                    }

                    // 도움말 다이얼로그
                    if(viewModel.helpDialogVisible.value){
                        AlertDialog(
                            containerColor = Color.White,
                            onDismissRequest = {
                                viewModel.setHelpDialogVisible()
                            },
                            title = {

                                Text(
                                    text = "굿생 자극이란?",
                                    style = TextStyle(
                                        color = PurpleMain,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            text = {
                                Text(
                                    text = "굿생에 대해 자유롭게 글을 작성하고 읽을 수 있는 공간이에요.\n" +
                                            "굿생러분들의 좋은 글을 통해 여러분의 굿생에 도움을 드리기 위해 제작했어요.\n" +
                                            "또는 굿생을 살기 위한 본인의 팁, 조언 또는 경험이 있다면 자유롭게 작성해주세요.\n",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        lineHeight = 24.sp
                                    )
                                )
                            },
                            confirmButton = { /*TODO*/ }
                        )
                    }

                }

                composable("CreateStimulusPostScreen"){
                    fabVisibleState.value = false
                    CreateStimulusPostScreen(
                        bottomBarVisibleState = bottomBarVisibleState,
                        fabVisibleState = fabVisibleState,
                        parentNavController = navController
                    )

                }

                composable("StimulusSearchScreen"){
                    fabVisibleState.value = false
                    StimulusSearchScreen()

                }

            }


        }

    }

}


@Composable
fun StimulusCoverItem(
    modifier: Modifier = Modifier,
    item: StimulusPostList
){
    Box(
        modifier = modifier
            .padding(10.dp)
            .size(width = 200.dp, height = 250.dp)
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
                .fillMaxWidth(),
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
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CustomExpandableFAB(
    modifier: Modifier = Modifier,
    items: List<FABItem>,
    fabButton: FABItem = FABItem(
        icon = Icons.Rounded.Menu,
        text = "    메뉴    "
    ),
    onItemClick: (FABItem) -> Unit,
    viewModel: StimulusPostViewModel
) {

    val buttonClicked = viewModel.fabExpanded.value

    val interactionSource = MutableInteractionSource()

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {

        // parent layout
        Column(
            modifier = Modifier
                .background(color = GrayWhite3)
        ) {

//            you can also use the spring() in EnterTransition/ExitTransition provided by Material-3 library for a more smooth animation, but it increases the collapse time of the sheet/FAB
//            example - spring(dampingRatio = 3f)

            // The Expandable Sheet layout
            AnimatedVisibility(
                visible = buttonClicked,
                enter = expandVertically(tween(700)) + fadeIn(),
                exit = shrinkVertically(tween(700)) + fadeOut(
                    animationSpec = tween(700)
                )
            ) {
                // display the items
                Column(
                    modifier = Modifier
                        .background(color = GrayWhite3)
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    items.forEach { item ->
                        Row(modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    onItemClick(item)
                                    viewModel.setFabExpanded()
                                    //buttonClicked = false
                                }
                            )) {
                            Icon(
                                imageVector = item.icon, contentDescription = "refresh"
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Text(
                                text = item.text,
                                style = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }

            // The FAB main button
            Card(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        viewModel.setFabExpanded()
                        //buttonClicked = !buttonClicked
                    }
                ), colors = CardDefaults.cardColors(Color.White)) {
                Row(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = fabButton.icon, contentDescription = ""
                    )
                    AnimatedVisibility(
                        visible = buttonClicked,
                        enter = expandVertically(animationSpec = tween(700)) + fadeIn(),
                        exit = shrinkVertically(tween(700)) + fadeOut(tween(700))
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = fabButton.text,
                                style = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }

        }

    }

}

@Preview(showBackground = true)
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CustomExpandableFABPreview(
    modifier: Modifier = Modifier,
    items: List<FABItem> = listOf(
        FABItem(icon = Icons.Rounded.Create, text = "글 작성"),
        FABItem(icon = Icons.Rounded.Search, text = "검색")
    ),
    fabButton: FABItem = FABItem(
        icon = Icons.Rounded.Menu,
        text = "메뉴"
    ),
    //onItemClick: (FABItem) -> Unit
) {

    var buttonClicked by remember {
        mutableStateOf(true)
    }

    val interactionSource = MutableInteractionSource()

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {

        // parent layout
        Column(
            modifier = Modifier
                .background(color = GrayWhite3)
        ) {

//            you can also use the spring() in EnterTransition/ExitTransition provided by Material-3 library for a more smooth animation, but it increases the collapse time of the sheet/FAB
//            example - spring(dampingRatio = 3f)

            // The Expandable Sheet layout
            AnimatedVisibility(
                visible = buttonClicked,
                enter = expandVertically(tween(700)) + fadeIn(),
                exit = shrinkVertically(tween(700)) + fadeOut(
                    animationSpec = tween(700)
                )
            ) {
                // display the items
                Column(
                    modifier = Modifier
                        .background(color = GrayWhite3)
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    items.forEach { item ->
                        Row(modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    //onItemClick(item)
                                    buttonClicked = false
                                }
                            )) {
                            Icon(
                                imageVector = item.icon, contentDescription = "refresh"
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Text(
                                text = item.text,
                                style = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }

            // The FAB main button
            Card(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        buttonClicked = !buttonClicked
                    }
                ), colors = CardDefaults.cardColors(Color.White)) {
                Row(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = fabButton.icon, contentDescription = ""
                    )
                    AnimatedVisibility(
                        visible = buttonClicked,
                        enter = expandVertically(animationSpec = tween(700)) + fadeIn(),
                        exit = shrinkVertically(tween(700)) + fadeOut(tween(700))
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = fabButton.text,
                                style = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }

        }

    }

}


@Preview
@Composable
fun StimulusCoverItemPreview(
    modifier: Modifier = Modifier,
    item: StimulusPostItem = StimulusPostItem(title = "이것이 제목이다", writer = "치킨 러버", coverImg = R.drawable.category3, introText = "")
){
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
}





@Preview
@Composable
fun StimulusLoadingScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(color = PurpleMain)
    }
}


data class StimulusPostItem(
    val title: String,
    val writer: String,
    val coverImg: Int,
    val introText: String
)

data class RecommendUserItem(
    val nickname: String,
    val introText: String,
    val profileImg: Int,
    val backgroundImg: Int,
    val postItem: List<StimulusPostItem>
)


