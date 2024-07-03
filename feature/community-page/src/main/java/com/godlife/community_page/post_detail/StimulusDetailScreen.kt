package com.godlife.community_page.post_detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OpaqueLight
import com.godlife.network.model.StimulusPost
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StimulusDetailScreen(
    modifier: Modifier = Modifier,
    postId: String = "",
    viewModel: StimulusPostDetailViewModel = hiltViewModel()
) {

    viewModel.initPostId(postId)
    viewModel.getPostDetail()


    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)

    val uiState by viewModel.uiState.collectAsState()

    var height by remember {
        mutableStateOf(0.dp)
    }
    val localDensity = LocalDensity.current
    val context = LocalContext.current
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    val postDetail = viewModel.postDetail.collectAsState()


    GodLifeTheme {

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {
            Box(modifier = modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    height = with(localDensity) {
                        it.size.height.toDp()
                    }
                }
            ){

                Glide.with(context)
                    .asBitmap()
                    .load(BuildConfig.SERVER_IMAGE_DOMAIN + postDetail.value?.thumbnailUrl)
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
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .fillMaxSize()
                            .blur(
                                radiusX = 15.dp, radiusY = 15.dp
                            )
                    )

                }

                LazyColumn{


                    item {
                        postDetail.value?.let { it1 ->
                            StimulusPostCover(
                                height = height,
                                postDetail = it1
                            )
                        }
                    }

                    item {
                        postDetail.value?.let { it1 ->
                            PostContent(
                                height = height,
                                postDetail = it1
                            )
                        }
                    }


                    item {
                        AnotherPostPreview()
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

                HorizontalDivider()

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


@Composable
fun StimulusCoverItem(
    modifier: Modifier = Modifier,
    postDetail: StimulusPost
){
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .padding(10.dp)
            .size(width = 200.dp, height = 250.dp)
            .shadow(10.dp),
        contentAlignment = Alignment.Center
    ){

        Glide.with(context)
            .asBitmap()
            .load(BuildConfig.SERVER_IMAGE_DOMAIN + postDetail.thumbnailUrl)
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
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize()
            )

        }

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
    height: Dp = 800.dp,
    postDetail: StimulusPost
){

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
                        document.querySelector('.ql-editor').innerHTML = `${postDetail.content}`;
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

                item { AnotherPostPreview() }

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
fun AnotherPostPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(color = OpaqueLight, shape = RoundedCornerShape(18.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ){

        Text(
            text = "닉네임 님, 이런 글은 어때요?",
            style = TextStyle(color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        )

        HorizontalDivider()


    }

}