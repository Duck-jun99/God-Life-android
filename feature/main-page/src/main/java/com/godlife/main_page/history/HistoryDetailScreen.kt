package com.godlife.main_page.history

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.database.model.TodoEntity
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.model.todo.TodoList

@Composable
fun HistoryDetailScreen(
    modifier: Modifier = Modifier,
    id: Int,
    viewModel: HistoryDetailViewModel = hiltViewModel()
){
    viewModel.getTodoList(id)

    val todo = viewModel.selectedTodoList.collectAsState().value
    val completeTodoSize = viewModel.completedTodoListSize.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding()
            .padding(horizontal = 10.dp)
    ){

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                modifier = modifier
                    .fillMaxWidth(0.9f),
                text = "${todo?.date?.y}/${todo?.date?.m}/${todo?.date?.d}의 기록",
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp
                )
            )

            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = ""
                )
            }

        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {

            item{
                if (todo != null) {
                    HistoryDetailGraph(
                        todo = todo,
                        completeTodoSize = completeTodoSize
                    )
                }
            }

            item{
                HistoryDetailTodoList(
                    viewModel = viewModel
                )
                Spacer(modifier = modifier.height(20.dp))
            }

            item{
                HistoryDetailEvaluation(
                    viewModel = viewModel
                )
                Spacer(modifier = modifier.height(20.dp))
            }

        }


    }
}

@Composable
fun HistoryDetailGraph(
    todo: TodoEntity,
    completeTodoSize: Int
){
    val todayTodoListSize = todo.todoList.size

    val todoPercent =
        360 * ( completeTodoSize.toFloat() / todayTodoListSize.toFloat() )

    val animatedValue = remember { Animatable(0f) }


    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = todoPercent,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$completeTodoSize / $todayTodoListSize", style = TextStyle(color = Color(0xFFFA6B80), fontSize = 25.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
        }


        Canvas(
            modifier = Modifier
                .size(360.dp)
        ) {
            val size: Size = drawContext.size
            val sizeArc = size / 1.75F
            drawArc(
                color = Color(0xFFE1E2E9),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset((size.width - sizeArc.width) / 2f, (size.height - sizeArc.height) / 2f),
                size = sizeArc,
                style = Stroke(width = 35f)
            )

            drawArc(
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
                startAngle = 100f,
                sweepAngle = animatedValue.value,
                useCenter = false,
                topLeft = Offset(
                    (size.width - sizeArc.width) / 2f,
                    (size.height - sizeArc.height) / 2f
                ),
                size = sizeArc,
                style = Stroke(width = 35f, cap = StrokeCap.Round)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryDetailTodoList(
    modifier: Modifier = Modifier,
    viewModel: HistoryDetailViewModel
){
    val completeTodoList = viewModel.completedTodoList.collectAsState().value
    val unCompleteTodoList = viewModel.uncompletedTodoList.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        if(completeTodoList.isNotEmpty()){

            Text(
                text = "완료한 투두",
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = modifier.height(10.dp))

            FlowRow(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                completeTodoList.forEach {
                    Box(
                        modifier = modifier
                            .padding(end = 10.dp, bottom = 10.dp)
                            .background(
                                color = Color(0xFFFF8161),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = it,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                }
            }

            Spacer(modifier = modifier.height(20.dp))

        }

        if(unCompleteTodoList.isNotEmpty()){
            Text(
                text = "미완료 투두",
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = modifier.height(10.dp))

            FlowRow(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                unCompleteTodoList.forEach {
                    Box(
                        modifier = modifier
                            .padding(end = 10.dp, bottom = 10.dp)
                            .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = it,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                }
            }
        }


    }
}

@Composable
fun HistoryDetailEvaluation(
    modifier: Modifier = Modifier,
    viewModel: HistoryDetailViewModel
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
            .padding(10.dp)
    ){

        Text(
            text = viewModel.evaluation.collectAsState().value,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 24.sp
            )
        )
    }
}

@Preview
@Composable
fun HistoryDetailScreenPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding()
            .padding(horizontal = 10.dp)
    ){

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                modifier = modifier
                    .fillMaxWidth(0.9f),
                text = "2024/7/29의 기록",
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp
                )
            )

            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = ""
                )
            }

        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {

            item{
                HistoryDetailGraphPreview()
            }

            item{
                HistoryDetailTodoListPreview()
                Spacer(modifier = modifier.height(20.dp))
            }

            item{
                HistoryDetailEvaluationPreview()
                Spacer(modifier = modifier.height(20.dp))
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun HistoryDetailGraphPreview(
    modifier: Modifier = Modifier
){
    val todayTodoListSize = 10
    val completedTodoListSize = 3

    val todoPercent =
        360 * ( completedTodoListSize.toFloat() / todayTodoListSize.toFloat() )

    val animatedValue = remember { Animatable(0f) }


    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = todoPercent,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$completedTodoListSize / $todayTodoListSize", style = TextStyle(color = Color(0xFFFA6B80), fontSize = 25.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
        }


        Canvas(
            modifier = Modifier
                .size(360.dp)
        ) {
            val size: Size = drawContext.size
            val sizeArc = size / 1.75F
            drawArc(
                color = Color(0xFFE1E2E9),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset((size.width - sizeArc.width) / 2f, (size.height - sizeArc.height) / 2f),
                size = sizeArc,
                style = Stroke(width = 50f)
            )

            drawArc(
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
                startAngle = 100f,
                sweepAngle = animatedValue.value,
                useCenter = false,
                topLeft = Offset(
                    (size.width - sizeArc.width) / 2f,
                    (size.height - sizeArc.height) / 2f
                ),
                size = sizeArc,
                style = Stroke(width = 50f, cap = StrokeCap.Round)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun HistoryDetailTodoListPreview(
    modifier: Modifier = Modifier
){
    val todoList: List<String> = listOf("todo1", "todo2", "todo3", "todo4")

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "완료한 투두",
            style = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = modifier.height(10.dp))

        FlowRow(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            todoList.forEach {
                Box(
                    modifier = modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .background(color = Color(0xFFFF8161), shape = RoundedCornerShape(16.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = it,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

            }
        }

        Spacer(modifier = modifier.height(20.dp))


        Text(
            text = "미완료 투두",
            style = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = modifier.height(10.dp))

        FlowRow(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            todoList.forEach {
                Box(
                    modifier = modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = it,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

            }
        }

    }
}

@Preview
@Composable
fun HistoryDetailEvaluationPreview(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
            .padding(10.dp)
    ){

        Text(
            text = "10개의 투두를 설정하셨지만 목표에 비해 달성하신 비율이 적은 것 같아요.\n" +
                    "완료하지 못한 todo1, todo2, todo3, todo4 도 달성할 수 있도록 노력해보세요.\n" +
                    "저희 굿생팀은 사용자님께서 더욱 노력하실거라 확신해요.\n" +
                    "Good Life를 위해 화이팅!",
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 24.sp
            )
        )
    }
}