package com.godlife.setting_page.score

import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.OrangeLight
import com.godlife.designsystem.theme.OrangeMain
import kotlinx.coroutines.delay

@Composable
fun ScoreDetailScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    score: Int,
    viewModel: ScoreDetailViewModel = hiltViewModel()
){
    val animatedValue = remember { Animatable(0f) }
    val graphVisble = remember { mutableStateOf(false) }
    val text1Visble = remember { mutableStateOf(false) }
    val tierVisble = remember { mutableStateOf(false) }
    val text2Visble = remember { mutableStateOf(false) }
    val text3Visble = remember { mutableStateOf(false) }

    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(Unit) {
        viewModel.setUserTier(score)

        animatedValue.animateTo(
            targetValue = viewModel.graphTargetValue.value,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )

        graphVisble.value = true
        //delay(1000)
        text1Visble.value = true
        //delay(1000)
        tierVisble.value = true
        //delay(1000)
        text2Visble.value = true
        //delay(1000)
        text3Visble.value = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ){

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {

            item {

                Column(
                    modifier = modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(10.dp)
                ) {

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ){
                        Box(
                            modifier = modifier
                                .weight(0.4f)
                        ) {
                            BoxWithConstraints(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val boxWidth = constraints.maxWidth.toFloat()
                                val canvasWidth = 100f
                                val startX = (boxWidth - canvasWidth) / 2

                                Canvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    val totalHeight = 650f
                                    val startY = 80f + totalHeight // 그래프의 시작점을 바닥으로 변경

                                    // 배경 (회색) 사각형
                                    drawRoundRect(
                                        color = GrayWhite2,
                                        topLeft = Offset(startX, 80f),
                                        size = Size(canvasWidth, totalHeight),
                                        cornerRadius = CornerRadius(45f)
                                    )

                                    // 채워진 (그라데이션) 사각형
                                    drawRoundRect(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFF44A2),  // 밝은 핫핑크
                                                Color(0xFFFF5890),  // 연한 핑크
                                                Color(0xFFFA6B80),  // 연한 코럴 핑크
                                                Color(0xFFFF7B75),  // 연한 살몬
                                                Color(0xFFFF8161),  // 밝은 코럴
                                                Color(0xFFFF884D),  // 연한 오렌지
                                            ),
                                            start = Offset.Zero,
                                            end = Offset.Infinite,
                                        ),
                                        topLeft = Offset(startX, startY - animatedValue.value),
                                        size = Size(canvasWidth, animatedValue.value),
                                        cornerRadius = CornerRadius(45f)
                                    )

                                    // 텍스트 그리기
                                    val paint = Paint().asFrameworkPaint().apply {
                                        isAntiAlias = true
                                        textSize = 30f
                                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                                        color = GrayWhite.toArgb()
                                    }

                                    val paint2 = Paint().asFrameworkPaint().apply {
                                        isAntiAlias = true
                                        textSize = 30f
                                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                                        color = OrangeMain.toArgb()
                                    }

                                    // "현재 티어 최소 점수"
                                    drawContext.canvas.nativeCanvas.drawText(
                                        "${viewModel.minScore.value}",
                                        startX + canvasWidth + 10,
                                        startY,
                                        paint
                                    )

                                    // "현재 점수"
                                    drawContext.canvas.nativeCanvas.drawText(
                                        "$score",
                                        startX + canvasWidth + 10,
                                        startY - animatedValue.value,
                                        paint2
                                    )

                                    // "현재 티어 최고 점수"
                                    drawContext.canvas.nativeCanvas.drawText(
                                        "${viewModel.maxScore.value}",
                                        startX + canvasWidth + 10,
                                        80f,
                                        paint
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .weight(0.6f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){

                            AnimatedVisibility(
                                visible = graphVisble.value,
                                enter = fadeIn(
                                    initialAlpha = 0f,
                                    animationSpec = tween(durationMillis = 2000)
                                ) + slideInVertically(
                                    initialOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            ) {

                                Column {

                                    Text(
                                        text = "${nickname} 님의 티어는",
                                        style = TextStyle(
                                            color = GrayWhite,
                                            fontSize = 25.sp
                                        )
                                    )

                                    Text(
                                        text = "${viewModel.tier.collectAsState().value}",
                                        style = TextStyle(
                                            color = OrangeMain,
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = "입니다.",
                                        style = TextStyle(
                                            color = GrayWhite,
                                            fontSize = 25.sp
                                        )
                                    )
                                }


                            }


                        }
                    }


                    Spacer(modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = text1Visble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "다음 승급까지",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 20.sp
                                )
                            )

                            Spacer(modifier.width(5.dp))

                            Text(
                                text = "${viewModel.requiredScore.collectAsState().value}점",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier.width(5.dp))

                            Text(
                                text = "남았어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 20.sp
                                )
                            )


                        }

                    }



                    Spacer(modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = tierVisble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = "",
                                    tint = OrangeMain
                                )

                                Spacer(modifier.width(5.dp))

                                Text(
                                    text = "굿생 티어표",
                                    style = TextStyle(
                                        color = OrangeMain,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier.height(10.dp))

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "레전드 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF44A2),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "1000점 이상",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "마스터 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF5890),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "700 ~ 999점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "다이아 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFA6B80),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "500 ~ 699점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "골드 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF7B75),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "200 ~ 499점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "실버 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF8161),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "100 ~ 199점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "브론즈 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF884D),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "30 ~ 99점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "새싹 : ",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "0 ~ 29점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                    }

                    Spacer(modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = text2Visble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Text(
                            text = "굿생 점수는 이렇게 획득하실 수 있어요.",
                            style = TextStyle(
                                color = OrangeMain,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                        )

                    }


                    Spacer(modifier.height(10.dp))

                    AnimatedVisibility(
                        visible = text3Visble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Column(
                            modifier = modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .background(color = OrangeLight, shape = RoundedCornerShape(16.dp))
                                .padding(10.dp)
                        ) {

                            Text(
                                text = "- 굿생 인증",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier.height(5.dp))

                            Text(
                                text = "매일 굿생인증을 통해 2점을 획득하실 수 있어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = modifier
                                    .padding(start = 10.dp)
                            )

                            Spacer(modifier.height(10.dp))

                            Text(
                                text = "- 굿생 인정받기",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier.height(5.dp))

                            Text(
                                text = "회원님의 굿생인증 또는 굿생자극 게시물에 굿생 인정을 받으시면 2점을 획득하실 수 있어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = modifier
                                    .padding(start = 10.dp)
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
fun ScoreDetailScreenPreview(
    modifier: Modifier = Modifier
){
    val animatedValue = remember { Animatable(0f) }
    val graphVisble = remember { mutableStateOf(false) }
    val text1Visble = remember { mutableStateOf(false) }
    val tierVisble = remember { mutableStateOf(false) }
    val text2Visble = remember { mutableStateOf(false) }
    val text3Visble = remember { mutableStateOf(false) }

    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = 350f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )

        graphVisble.value = true
        //delay(1000)
        text1Visble.value = true
        //delay(1000)
        tierVisble.value = true
        //delay(1000)
        text2Visble.value = true
        //delay(1000)
        text3Visble.value = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ){

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {

            item {

                Column(
                    modifier = modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(10.dp)
                ) {

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ){
                        Box(
                            modifier = modifier
                                .weight(0.4f)
                        ) {
                            BoxWithConstraints(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val boxWidth = constraints.maxWidth.toFloat()
                                val canvasWidth = 100f
                                val startX = (boxWidth - canvasWidth) / 2

                                Canvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    val totalHeight = 650f
                                    val startY = 80f + totalHeight // 그래프의 시작점을 바닥으로 변경

                                    // 배경 (회색) 사각형
                                    drawRoundRect(
                                        color = GrayWhite2,
                                        topLeft = Offset(startX, 80f),
                                        size = Size(canvasWidth, totalHeight),
                                        cornerRadius = CornerRadius(45f)
                                    )

                                    // 채워진 (그라데이션) 사각형
                                    drawRoundRect(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFF44A2),  // 밝은 핫핑크
                                                Color(0xFFFF5890),  // 연한 핑크
                                                Color(0xFFFA6B80),  // 연한 코럴 핑크
                                                Color(0xFFFF7B75),  // 연한 살몬
                                                Color(0xFFFF8161),  // 밝은 코럴
                                                Color(0xFFFF884D),  // 연한 오렌지
                                            ),
                                            start = Offset.Zero,
                                            end = Offset.Infinite,
                                        ),
                                        topLeft = Offset(startX, startY - animatedValue.value),
                                        size = Size(canvasWidth, animatedValue.value),
                                        cornerRadius = CornerRadius(45f)
                                    )

                                    // 텍스트 그리기
                                    val paint = Paint().asFrameworkPaint().apply {
                                        isAntiAlias = true
                                        textSize = 30f
                                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                                        color = GrayWhite.toArgb()
                                    }

                                    // "브론즈" 텍스트
                                    drawContext.canvas.nativeCanvas.drawText(
                                        "100",
                                        startX + canvasWidth + 10,
                                        startY,
                                        paint
                                    )

                                    // "실버" 텍스트
                                    drawContext.canvas.nativeCanvas.drawText(
                                        "120",
                                        startX + canvasWidth + 10,
                                        startY - animatedValue.value,
                                        paint
                                    )

                                    // "골드" 텍스트
                                    drawContext.canvas.nativeCanvas.drawText(
                                        "199",
                                        startX + canvasWidth + 10,
                                        80f,
                                        paint
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .weight(0.6f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){

                            AnimatedVisibility(
                                visible = graphVisble.value,
                                enter = fadeIn(
                                    initialAlpha = 0f,
                                    animationSpec = tween(durationMillis = 2000)
                                ) + slideInVertically(
                                    initialOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            ) {

                                Column {

                                    Text(
                                        text = "유저님의 티어는",
                                        style = TextStyle(
                                            color = GrayWhite,
                                            fontSize = 25.sp
                                        )
                                    )

                                    Text(
                                        text = "실버",
                                        style = TextStyle(
                                            color = OrangeMain,
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = "입니다.",
                                        style = TextStyle(
                                            color = GrayWhite,
                                            fontSize = 25.sp
                                        )
                                    )
                                }


                            }


                        }
                    }


                    Spacer(modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = text1Visble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "다음 승급까지",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 20.sp
                                )
                            )

                            Spacer(modifier.width(5.dp))

                            Text(
                                text = "79점",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier.width(5.dp))

                            Text(
                                text = "남았어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 20.sp
                                )
                            )


                        }

                    }



                    Spacer(modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = tierVisble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = "",
                                    tint = OrangeMain
                                )

                                Spacer(modifier.width(5.dp))

                                Text(
                                    text = "굿생 티어표",
                                    style = TextStyle(
                                        color = OrangeMain,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier.height(10.dp))

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "레전드 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF44A2),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "1000점 이상",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "마스터 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF5890),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "700 ~ 999점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "다이아 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFA6B80),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "500 ~ 699점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "골드 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF7B75),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "200 ~ 499점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "실버 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF8161),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "100 ~ 199점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "브론즈 : ",
                                    style = TextStyle(
                                        color = Color(0xFFFF884D),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "30 ~ 99점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }

                            Row(
                                modifier = modifier
                                    .width(170.dp)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "새싹 : ",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "0 ~ 29점",
                                    style = TextStyle(
                                        color = GrayWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                    }

                    Spacer(modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = text2Visble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Text(
                            text = "굿생 점수는 이렇게 획득하실 수 있어요.",
                            style = TextStyle(
                                color = OrangeMain,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                        )

                    }


                    Spacer(modifier.height(10.dp))

                    AnimatedVisibility(
                        visible = text3Visble.value,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 2000)
                        ) + slideInVertically(
                            initialOffsetY = { fullHeight -> fullHeight },
                            animationSpec = tween(durationMillis = 1000)
                        )
                    ) {

                        Column(
                            modifier = modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .background(color = OrangeLight, shape = RoundedCornerShape(16.dp))
                                .padding(10.dp)
                        ) {

                            Text(
                                text = "- 굿생 인증",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier.height(5.dp))

                            Text(
                                text = "매일 굿생인증을 통해 2점을 획득하실 수 있어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = modifier
                                    .padding(start = 10.dp)
                            )

                            Spacer(modifier.height(10.dp))

                            Text(
                                text = "- 굿생 인정받기",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier.height(5.dp))

                            Text(
                                text = "회원님의 굿생인증 또는 굿생자극 게시물에 굿생 인정을 받으시면 2점을 획득하실 수 있어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = modifier
                                    .padding(start = 10.dp)
                            )

                        }

                    }

                }

            }


        }

    }


}