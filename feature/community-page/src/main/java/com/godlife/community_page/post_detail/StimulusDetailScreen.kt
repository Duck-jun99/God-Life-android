package com.godlife.community_page.post_detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.post_detail.post_update.stimulus.UpdateStimulusPostScreenRoute
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.StimulusPost
import com.godlife.network.model.UserProfileBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StimulusDetailScreen(
    modifier: Modifier = Modifier,
    postId: String = "",
    navController: NavController,
    viewModel: StimulusPostDetailViewModel = hiltViewModel()
) {

    viewModel.initPostId(postId)
    viewModel.getPostDetail()


    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)

    val uiState by viewModel.uiState.collectAsState()

    val localDensity = LocalDensity.current

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })


    val height = remember {
        mutableStateOf(0.dp)
    }


    val postDetail = viewModel.postDetail.collectAsState()
    val writerInfo = viewModel.writerInfo.collectAsState()


    GodLifeTheme {

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {

            VerticalPager(
                state = pagerState,
                modifier = modifier
                    .fillMaxSize()
                    .onGloballyPositioned {
                        height.value = with(localDensity) {
                            it.size.height.toDp()
                        }
                    }
            ) {index ->
                when(index){
                    0 -> {
                        postDetail.value?.let { it1 ->
                            StimulusPostCover(
                                height = height.value,
                                postDetail = it1
                            )
                        }
                    }
                    1 -> {

                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .background(color = Color.White)
                        ) {

                            item {
                                postDetail.value?.let { post ->
                                    writerInfo.value?.let { writer ->
                                        PostContent(
                                            height = height.value,
                                            postDetail = post,
                                            writerInfo = writer,
                                            navController = navController,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }

                            item{
                                writerInfo.value?.nickname?.let { it1 ->
                                    WriterAnotherPostPreview(
                                        nickname = it1
                                    )
                                }
                            }
                        }


                    }



                }
                
            }

        }

    }
}

@Composable
fun StimulusPostCover(
    modifier: Modifier = Modifier,
    height: Dp,
    postDetail: StimulusPost
) {


    val coverVisible = remember { mutableStateOf(false) }


    LaunchedEffect(true) {
        delay(1500L)
        coverVisible.value = true
    }

    Box(modifier = modifier
        .fillMaxSize()
        .height(height)
    ){

        GlideImage(
            imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + postDetail.thumbnailUrl },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = modifier
                .fillMaxSize()
                .blur(
                    radiusX = 15.dp, radiusY = 15.dp
                ),
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
                .height(height)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            AnimatedVisibility(
                visible = coverVisible.value ,
                enter = fadeIn(initialAlpha = 0.4f)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    StimulusCoverItem(
                        postDetail = postDetail
                    )

                    Spacer(modifier.size(5.dp))

                    Text(
                        text = postDetail.introduction,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    )



                    Spacer(modifier.size(5.dp))

                    HorizontalDivider(modifier.width(200.dp))

                    Spacer(modifier.size(5.dp))

                    //User 이름 들어갈 부분

                    Text(
                        text = "by ${postDetail.nickname}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    )


                    Spacer(modifier.size(50.dp))

                }

            }



        }


    }



}


@Composable
fun StimulusCoverItem(
    modifier: Modifier = Modifier,
    postDetail: StimulusPost
){

    Box(
        modifier = modifier
            .padding(10.dp)
            .size(width = 200.dp, height = 250.dp)
            .shadow(10.dp),
        contentAlignment = Alignment.Center
    ){

        GlideImage(
            imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + postDetail.thumbnailUrl },
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

            Text(text = postDetail.title,
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    height: Dp,
    navController: NavController,
    viewModel: StimulusPostDetailViewModel,
    postDetail: StimulusPost,
    writerInfo: UserProfileBody
){

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .statusBarsPadding()
            .padding(horizontal = 15.dp, vertical = 10.dp)
    ){

        AndroidView(
            modifier = modifier
                .fillMaxSize(),
            factory = { context ->

                WebView(context).apply {
                    loadUrl("file:///android_asset/content_template.html")


                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true

                    // 웹뷰 크기에 맞게 컨텐츠 크기 조정
                    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL


                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            // HTML 템플릿이 로드된 후 콘텐츠를 삽입합니다.
                            view?.evaluateJavascript(
                                """
                        document.querySelector('.ql-editor').innerHTML = `${postDetail.content}`;
                        document.body.style.backgroundColor = 'transparent';
                        document.documentElement.style.backgroundColor = 'transparent';
                        """.trimIndent(),
                                null
                            )
                        }

                    }

                }
            }
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = postDetail.createDate,
            style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "조회수: ${postDetail.view}",
            style = TextStyle(color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))


        Column(modifier = modifier
            .fillMaxWidth()
            .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
            .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                GlideImage(
                    imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + writerInfo.profileImageURL },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .clip(CircleShape)
                        .size(70.dp),
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
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                )

                Spacer(modifier.size(10.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = writerInfo.nickname,
                        style = TextStyle(
                            color = GrayWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )

                    Spacer(modifier.size(5.dp))

                    Text(text = writerInfo.whoAmI,
                        style = TextStyle(
                            color = GrayWhite,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )

                }


            }

        }

        if(postDetail.owner){

            Spacer(modifier.size(20.dp))

            OwnerOption(
                navController = navController,
                viewModel = viewModel,
                postDetail = postDetail
            )

        }




    }



}

@Composable
fun OwnerOption(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: StimulusPostDetailViewModel,
    postDetail: StimulusPost
){
    Row(modifier = modifier
        .fillMaxWidth()
        .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = modifier
                .weight(0.5f)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ){

            GodLifeButtonWhite(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = {
                          navController.navigate("${UpdateStimulusPostScreenRoute.route}/${postDetail.boardId}"){
                              launchSingleTop = true
                          }
                          },
                text = {
                    Text(
                        text = "수정하기",
                        style = TextStyle(
                            color = PurpleMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            )

        }

        Box(
            modifier = modifier
                .weight(0.5f)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ){

            GodLifeButtonWhite(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "삭제하기",
                        style = TextStyle(
                            color = PurpleMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            )

        }



    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun StimulusDetailScreenPreview(
    modifier: Modifier = Modifier,
    postId: String = "",

) {

    //val snackBarHostState = remember { SnackbarHostState() }
    //SnackbarHost(hostState = snackBarHostState)

    //val uiState by viewModel.uiState.collectAsState()

    var height by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current

    //viewModel.initPostId(postId)
    //viewModel.getPostDetail()


    GodLifeTheme {

        Box(modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                height = with(localDensity) {
                    it.size.height.toDp()
                }
            }
        ){

            Image(
                painter = painterResource(id = R.drawable.category3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize()
                    .blur(
                        radiusX = 15.dp, radiusY = 15.dp
                    )
            )

            LazyColumn{


                item { StimulusPostCoverPreview(height = height) }

                item { PostContentPreview(height = height) }

                item { WriterAnotherPostPreview() }

            }
        }

    }
}

@Preview
@Composable
fun StimulusPostCoverPreview(
    modifier: Modifier = Modifier,
    height: Dp = 800.dp
) {
    val context = LocalContext.current
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    val coverVisible = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        delay(1500L)
        coverVisible.value = true
    }

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = coverVisible.value ,
            enter = fadeIn(initialAlpha = 0.4f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                StimulusCoverItemPreview()

                Spacer(modifier.size(5.dp))

                Text(
                    text = "description.value",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )



                Spacer(modifier.size(5.dp))

                HorizontalDivider()

                Spacer(modifier.size(5.dp))

                //User 이름 들어갈 부분

                Text(
                    text = "by.User" /* Todo */,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )


                Spacer(modifier.size(50.dp))

            }

        }



    }

}


@Preview
@Composable
fun StimulusCoverItemPreview(
    modifier: Modifier = Modifier,
){

    Box(
        modifier = modifier
            .padding(10.dp)
            .size(width = 200.dp, height = 250.dp)
            .shadow(10.dp),
        contentAlignment = Alignment.Center
    ){

        Image(
            painter = painterResource(id = R.drawable.category3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(30.dp)
                .background(color = OpaqueDark)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ){

            Text(text = "postDetail.title",
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
@Preview
@Composable
fun PostContentPreview(
    modifier: Modifier = Modifier,
    height: Dp = 800.dp
){
    //val loadTestData = "<p><span class=\"ql-size-large\" style=\"font-size: 1.5em;\">Hello!!</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">테스트글을 작성중...</p><p class=\"ql-align-justify\"><span class=\"ql-size-small\" style=\"font-size: 0.75em;\">더 작은 글도 적고</span></p><p class=\"ql-align-justify\"><span class=\"ql-size-huge\" style=\"font-size: 2.5em;\">완전 큰 글도 적고</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>볼드체 처리도 하고</strong></p><ol><li data-list=\"ordered\" class=\"ql-align-justify\"><span class=\"ql-ui\" contenteditable=\"false\"></span>이렇게도</li><li data-list=\"ordered\" class=\"ql-align-justify\"><span class=\"ql-ui\" contenteditable=\"false\"></span>저렇게도</li><li data-list=\"ordered\" class=\"ql-align-justify\"><span class=\"ql-ui\" contenteditable=\"false\"></span>하고</li></ol><p class=\"ql-align-center\">가운데에 적어보기도 하고</p><ol><li data-list=\"bullet\" class=\"ql-align-center\"><span class=\"ql-ui\" contenteditable=\"false\"></span>여기도</li><li data-list=\"bullet\" class=\"ql-align-center\"><span class=\"ql-ui\" contenteditable=\"false\"></span>저기도</li></ol><p class=\"ql-align-right\">여기도 적어보고</p><p class=\"ql-align-right\"><span class=\"ql-size-huge\" style=\"font-size: 2.5em;\">더 크게!!!</span></p><p class=\"ql-align-justify\"><em><u>이렇게도</u></em></p><p class=\"ql-align-justify\"><s>저렇게도</s></p><p class=\"ql-align-justify\"><span style=\"color: rgb(230, 0, 0);\">빨갛게도</span></p><p class=\"ql-align-justify\"><span style=\"color: rgb(153, 51, 255);\">보라색도</span></p><p class=\"ql-align-justify\"><s style=\"color: rgb(0, 102, 204);\"><u>파란</u></s><s style=\"color: rgb(0, 102, 204); font-size: 2.5em;\" class=\"ql-size-huge\"><u>색도 작성</u></s></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p>"
    val loadTestData = "<p><strong class=\"ql-size-huge\" style=\"font-size: 2.5em;\">이것은 테스트 글입니다.</strong></p><p class=\"ql-align-center\"><img src=\"https://storage.googleapis.com/god-life-bucket-image/a7676c49-7c94-486c-84d6-261acb289cd0\"></p><p class=\"ql-align-center\"><span class=\"ql-size-small\" style=\"font-size: 0.75em;\">테스트 사진을 올려봤어요.</span></p><p></p><p>이렇게 사진도 잘 올라가고</p><ol><li>이렇게</li><li>저렇게</li><li><strong>또는 이렇게</strong></li></ol><p></p><p><em>이렇게도 기울이고</em></p><p class=\"ql-align-right\"><em>요쪽에도 </em><em class=\"ql-size-large\" style=\"font-size: 1.5em;\">이렇게 </em><strong class=\"ql-size-large\" style=\"font-size: 1.5em;\"><em>작성이 </em></strong><strong class=\"ql-size-large\" style=\"color: rgb(230, 0, 0); font-size: 1.5em;\"><em>됩니다 <u>으어아아</u> </em></strong></p><p class=\"ql-align-center\"><strong class=\"ql-size-large\" style=\"font-size: 1.5em;\"><em><img src=\"https://storage.googleapis.com/god-life-bucket-image/f2d5106c-c88e-4b04-b96a-7d005386ae55\"> </em></strong><em class=\"ql-size-large\" style=\"font-size: 1.5em;\"> </em><em> </em><strong> </strong> <span class=\"ql-size-small\" style=\"font-size: 0.75em;\"> </span></p><p class=\"ql-align-center\">사진을 하나 더 올립니다.</p>"

    LazyColumn(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .background(color = Color.White, shape = RoundedCornerShape(18.dp))
    ){

        item {
            AndroidView(
                modifier = modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp)),
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                // HTML 템플릿이 로드된 후 콘텐츠를 삽입합니다.
                                view?.evaluateJavascript(
                                    """
                        document.querySelector('.ql-editor').innerHTML = `$loadTestData`;
                        document.body.style.backgroundColor = 'transparent';
                        document.documentElement.style.backgroundColor = 'transparent';
                        """.trimIndent(),
                                    null
                                )
                            }

                        }
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true


                        loadUrl("file:///android_asset/content_template.html")

                    }
                }
            )
        }

        item {
            Spacer(modifier.size(20.dp))
        }

        item {
            Row {

                Text(
                    text = "조회수: 100",
                    style = TextStyle(color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Normal)
                )

                Spacer(modifier.size(20.dp))

                Text(
                    text = "댓글: 33",
                    style = TextStyle(color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Normal)
                )
            }

            Spacer(modifier.size(20.dp))
        }


        item {
            Column(modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End){

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {

                        Text(text = "작성자 닉네임",
                            style = TextStyle(
                                color = Color.Black
                            )
                        )

                        Text(text = "대충 나를 이렇게 소개한다는 내용",
                            style = TextStyle(
                                color = Color.Black
                            )
                        )

                    }


                    Spacer(modifier.size(30.dp))

                    Image(painter = painterResource(id = R.drawable.ic_person), contentDescription ="",
                        modifier
                            .background(color = GrayWhite, shape = CircleShape)
                            .size(50.dp))

                }

            }

        }



    }



}


@Preview
@Composable
fun WriterAnotherPostPreview(
    modifier: Modifier = Modifier,
    nickname: String = "작성자"
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ){

        Text(
            text = "${nickname}님의 다른 글도 읽어보세요.",
            style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier.size(10.dp))

        HorizontalDivider()


    }

}

@Preview(showBackground = true)
@Composable
fun OwnerOptionPreview(
    modifier: Modifier = Modifier
){
    Row(modifier = modifier
        .fillMaxWidth()
        .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = modifier
                .weight(0.5f)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ){

            GodLifeButtonWhite(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "수정하기",
                        style = TextStyle(
                            color = PurpleMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            )

        }

        Box(
            modifier = modifier
                .weight(0.5f)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ){

            GodLifeButtonWhite(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "삭제하기",
                        style = TextStyle(
                            color = PurpleMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            )

        }



    }
}