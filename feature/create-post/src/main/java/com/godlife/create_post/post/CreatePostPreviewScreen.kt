package com.godlife.create_post.post

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.create_post.R
import com.godlife.designsystem.theme.CheckColor
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.network.BuildConfig

@Composable
fun CreatePostPreviewScreen(
    viewModel: CreatePostViewModel,
    navController: NavController,
    modifier: Modifier = Modifier){

    val imgUriList by viewModel.selectedImgUri.collectAsState()

    GodLifeTheme {

        Column(
            modifier
                .background(Color.White)
        ) {

            Box(
                modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.White)
                    .statusBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "미리 보기", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
            }

            LazyColumn(
                modifier.fillMaxSize()
            ) {

                if(imgUriList?.isNotEmpty() == true){

                    item {
                        imgUriList?.let { ImageBox(imgUriList = it) }
                    }

                }


                item { Content(viewModel, navController) }

            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Content(
    viewModel: CreatePostViewModel,
    navController: NavController,
    modifier: Modifier = Modifier){
    val userInfo by viewModel.userInfo.collectAsState()
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    Column(
        modifier
            .fillMaxWidth()
            .padding(20.dp)) {
        Row(
            modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically){

            Glide.with(LocalContext.current)
                .asBitmap()
                .load(if(userInfo?.profileImage != "") BuildConfig.SERVER_IMAGE_DOMAIN + userInfo?.profileImage else R.drawable.category3)
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
                        .clip(CircleShape)
                        .size(50.dp)
                )
            }

            Spacer(modifier.size(10.dp))

            Column {
                Text(text = userInfo!!.nickname, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Text(text = userInfo!!.whoAmI, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp))
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = viewModel.title.value, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(text = viewModel.text.value, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        FlowRow{
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
        }

        Spacer(modifier.size(20.dp))

        //Text(text = "yyyy-mm-dd(게시물 올린 날짜,시간)", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        //Spacer(modifier.size(20.dp))

        RowButton2(navController)

    }
}

@Composable
fun ImageBox(
    modifier: Modifier = Modifier,
    imgUriList: List<Uri>
){
    val imgCount  = imgUriList.size
    val imgIndex  =  remember { mutableIntStateOf(1) }

    var width by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current

    val initialPage by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { imgUriList.size })

    Box(
        modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    width = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
        ) {index ->

            imgIndex.value = index + 1

            imgUriList.getOrNull(
                index%(imgUriList.size)
            )?.let{
                    item ->
                ImageView(modifier, LocalContext.current, item, width)

            }

        }

        Box(
            modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)){

            Box(modifier = modifier
                .size(width = 50.dp, height = 30.dp)
                .background(color = CheckColor, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${imgIndex.value}/${imgCount}", style = TextStyle(Color.White, fontSize = 15.sp), textAlign = TextAlign.Center)
            }

        }



    }
}

@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    context: Context,
    imgUri: Uri,
    width: Dp
){

    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    Glide.with(context)
        .asBitmap()
        .load(imgUri)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->

        Image(
            modifier = modifier
                .size(width = width, height = 400.dp),
            bitmap = fetchedBitmap,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

    }
}


@Preview(showBackground = true)
@Composable
fun CreatePostPreviewScreenPreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(modifier.background(Color.White)) {

            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "미리 보기", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }

            LazyColumn(
                modifier.fillMaxSize()
            ) {

                /*
                item { LazyRow(modifier.fillMaxWidth().height(500.dp)) {
                    itemsIndexed()
                } }

                 */

                item { Box(
                    modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(GrayWhite)){

                }}

                item { ContentPreview() }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview(modifier: Modifier = Modifier){
    Column(
        modifier
            .fillMaxWidth()
            .padding(20.dp)) {
        Row(
            modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically){

            Box(
                modifier
                    .background(OrangeMain, shape = CircleShape)
                    .size(70.dp))

            Spacer(modifier.size(10.dp))

            Column {
                Text(text = "Nickname", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Text(text = "Introduce", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp))
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = "Title", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(text = "text", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            items(5) {
                TagItemPreview()
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = "yyyy-mm-dd(게시물 올린 날짜,시간)", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        //RowButton2()

    }
}

@Composable
fun RowButton2(navController: NavController){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically){
        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)){

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("CreatePostScreen"){
                            popUpTo("CreatePostScreen")
                            launchSingleTop = true
                        }
                               },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Text(
                    text = "수정하기",
                    color = OrangeMain,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun EX(){
    Box(
        Modifier
            .size(300.dp)
            .background(Color.Black)){

        Box(
            Modifier
                .size(300.dp)
                .padding(10.dp)
                .background(Color.White)
                .fillMaxSize()){

        }
    }
}