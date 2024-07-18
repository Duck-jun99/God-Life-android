package com.godlife.community_page.stimulus

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.search.stimulus.StimulusSearchScreen
import com.godlife.community_page.stimulus.famous_post.FamousStimulusPostContent
import com.godlife.community_page.stimulus.latest_post.LatestStimulusPostContent
import com.godlife.community_page.stimulus.latest_post.LatestStimulusPostListPreview
import com.godlife.community_page.stimulus.most_view_post.MostViewStimulusPostContent
import com.godlife.community_page.stimulus.recommended_author_post.RecommendedAuthorInfoContentPreview
import com.godlife.community_page.stimulus.recommended_author_post.RecommendedAuthorStimulusPostContent
import com.godlife.community_page.stimulus.recommended_post.RecommendedStimulusPostContent
import com.godlife.create_post.stimulus.CreateStimulusPostScreen
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.StimulusPostList

data class FABItem(
    val icon: ImageVector,
    val text: String,
)


@OptIn(ExperimentalMaterial3Api::class)
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
        FABItem(icon = Icons.Rounded.Create, text = "글 작성"),
        FABItem(icon = Icons.Rounded.Search, text = "검색"),
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
                            }
                        }
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
                                text = "닉네임 님, 인기 글을 읽어보세요.",
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
                                text = "최신글",
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
                                text = "조회수가 높은 글이에요.",
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

                        item { RecommendedAuthorInfoContentPreview() }

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
                    .fillMaxWidth()
            )   //bitmap이 없다면
        } ?: Image(
            painter = painterResource(id = R.drawable.category3),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxWidth()
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
    onItemClick: (FABItem) -> Unit
) {

    var buttonClicked by remember {
        mutableStateOf(false)
    }

    val interactionSource = MutableInteractionSource()

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {

        // parent layout
        Column {

//            you can also use the spring() in EnterTransition/ExitTransition provided by Material-3 library for a more smooth animation, but it increases the collapse time of the sheet/FAB
//            example - spring(dampingRatio = 3f)

            // The Expandable Sheet layout
            AnimatedVisibility(
                visible = buttonClicked,
                enter = expandVertically(tween(1000)) + fadeIn(),
                exit = shrinkVertically(tween(1000)) + fadeOut(
                    animationSpec = tween(1000)
                )
            ) {
                // display the items
                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    items.forEach { item ->
                        Row(modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    onItemClick(item)
                                    buttonClicked = false
                                }
                            )) {
                            Icon(
                                imageVector = item.icon, contentDescription = "refresh"
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Text(text = item.text)
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
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 30.dp)
                ) {
                    Icon(
                        imageVector = fabButton.icon, contentDescription = "refresh"
                    )
                    AnimatedVisibility(
                        visible = buttonClicked,
                        enter = expandVertically(animationSpec = tween(1500)) + fadeIn(),
                        exit = shrinkVertically(tween(1200)) + fadeOut(tween(1200))
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = fabButton.text)
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


