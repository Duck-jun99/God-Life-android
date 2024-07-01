package com.godlife.community_page.post_detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.godlife.community_page.R
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OpaqueLight
import kotlinx.coroutines.delay

@Preview
@Composable
fun StimulusDetailScreen(
    modifier: Modifier = Modifier
) {

    var height by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current


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


                item { StimulusPostCover(height = height) }

                item { PostContentPreview(height = height) }

                item { AnotherPostPreview() }

            }
        }


    }
}

@Preview
@Composable
fun StimulusPostCover(
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

                StimulusCoverItem(title = "title.value")

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


@Composable
fun StimulusCoverItem(
    modifier: Modifier = Modifier,
    title: String,
    coverImg: Bitmap? = null
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

            Text(text = title,
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
    val loadTestData = "<p><span class=\"ql-size-large\" style=\"font-size: 1.5em;\">Hello!!</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">테스트글을 작성중...</p><p class=\"ql-align-justify\"><span class=\"ql-size-small\" style=\"font-size: 0.75em;\">더 작은 글도 적고</span></p><p class=\"ql-align-justify\"><span class=\"ql-size-huge\" style=\"font-size: 2.5em;\">완전 큰 글도 적고</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>볼드체 처리도 하고</strong></p><ol><li data-list=\"ordered\" class=\"ql-align-justify\"><span class=\"ql-ui\" contenteditable=\"false\"></span>이렇게도</li><li data-list=\"ordered\" class=\"ql-align-justify\"><span class=\"ql-ui\" contenteditable=\"false\"></span>저렇게도</li><li data-list=\"ordered\" class=\"ql-align-justify\"><span class=\"ql-ui\" contenteditable=\"false\"></span>하고</li></ol><p class=\"ql-align-center\">가운데에 적어보기도 하고</p><ol><li data-list=\"bullet\" class=\"ql-align-center\"><span class=\"ql-ui\" contenteditable=\"false\"></span>여기도</li><li data-list=\"bullet\" class=\"ql-align-center\"><span class=\"ql-ui\" contenteditable=\"false\"></span>저기도</li></ol><p class=\"ql-align-right\">여기도 적어보고</p><p class=\"ql-align-right\"><span class=\"ql-size-huge\" style=\"font-size: 2.5em;\">더 크게!!!</span></p><p class=\"ql-align-justify\"><em><u>이렇게도</u></em></p><p class=\"ql-align-justify\"><s>저렇게도</s></p><p class=\"ql-align-justify\"><span style=\"color: rgb(230, 0, 0);\">빨갛게도</span></p><p class=\"ql-align-justify\"><span style=\"color: rgb(153, 51, 255);\">보라색도</span></p><p class=\"ql-align-justify\"><s style=\"color: rgb(0, 102, 204);\"><u>파란</u></s><s style=\"color: rgb(0, 102, 204); font-size: 2.5em;\" class=\"ql-size-huge\"><u>색도 작성</u></s></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><br></p>"

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

                        // WebView 배경을 투명하게 설정
                        setBackgroundColor(Color.Transparent.toArgb())
                        //setBackgroundColor(0x00000000)

                        // 하드웨어 가속 비활성화
                        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

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