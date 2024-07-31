package com.godlife.main_page.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.database.model.TodoEntity
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.navigation.HistoryDetailRoute

@Composable
fun HistoryPageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HistoryPageViewModel = hiltViewModel()
){
    val todoList = viewModel.todoList.collectAsState().value

    Box(
        modifier = modifier
            .fillMaxSize()
    ){

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = GrayWhite3)
                .statusBarsPadding()
                .padding(10.dp)
        ){

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    text = "기록 저장소",
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp
                    )
                )

                Box(
                    modifier = modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ){

                    IconButton(
                        onClick = { viewModel.showHelpDialog() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = ""
                        )
                    }

                }


            }



            Spacer(modifier = modifier.height(10.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = GrayWhite3)
            ) {


                todoList?.size?.let { it ->
                    items(it){
                        HistoryTodoItem(
                            navController = navController,
                            todo = todoList[it]
                        )
                    }
                }
            }

        }

        if(viewModel.helpDialogVisble.collectAsState().value){
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = {
                    viewModel.showHelpDialog()
                },
                title = {

                        Text(
                            text = "기록 저장소란?",
                            style = TextStyle(
                                color = PurpleMain,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                },
                text = {
                    Text(
                        text = "지금까지 사용자님께서 설정하신 투두 리스트를 볼 수 있는 공간이에요.\n" +
                                "그동안 굿생을 살기 위해 어떤 노력을 하셨는지 확인해보세요.",
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

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryTodoItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    todo: TodoEntity
){
    Row(
        modifier = modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate("${HistoryDetailRoute.route}/${todo.id}"){
                    launchSingleTop = true
                }
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = modifier
                .size(150.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(
                modifier = modifier
                    .fillMaxSize()
            ) {
                val size: Size = drawContext.size
                val sizeArc = size / 1.5F

                drawArc(
                    brush = Brush.linearGradient(
                        colors =
                        listOf(
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
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(
                        (size.width - sizeArc.width) / 2f,
                        (size.height - sizeArc.height) / 2f
                    ),
                    size = sizeArc,
                    style = Stroke(width = 15f, cap = StrokeCap.Round)
                )
            }

            Text(
                text = if(todo.isCompleted) "완료" else "미완료",
                style = TextStyle(
                    color = if(todo.isCompleted) Color(0xFFFA6B80) else GrayWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

        }


        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${todo.date.y}/${todo.date.m}/${todo.date.d}",
                style = TextStyle(
                    color = Color(0xFFFF8161),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = modifier.height(10.dp))

            FlowRow(
                modifier = modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                todo.todoList.forEach {
                    Box(
                        modifier = modifier
                            .padding(end = 5.dp, bottom = 5.dp)
                            .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ){

                        Text(
                            text = it.name,
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

@Preview
@Composable
fun HistoryPageScreenPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = GrayWhite3)
            .statusBarsPadding()
            .padding(10.dp)
    ){

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                text = "기록 저장소",
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp
                )
            )

            Box(
                modifier = modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){

                IconButton(
                    onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = ""
                    )
                }

            }



        }


        Spacer(modifier = modifier.height(10.dp))

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = GrayWhite3)
        ) {


            items(10){
                HistoryTodoItemPreview()
            }
        }

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun HistoryTodoItemPreview(
    modifier: Modifier = Modifier,
){
    val todoList: List<String> = listOf("todo1", "todo2", "todo3", "todo4")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = modifier
                .size(150.dp),
            contentAlignment = Alignment.Center
        ){

            Canvas(
                modifier = modifier
                    .size(150.dp)
            ) {
                val size: Size = drawContext.size
                val sizeArc = size / 1.5F

                drawArc(
                    brush = Brush.linearGradient(
                        colors =
                        listOf(
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
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(
                        (size.width - sizeArc.width) / 2f,
                        (size.height - sizeArc.height) / 2f
                    ),
                    size = sizeArc,
                    style = Stroke(width = 15f, cap = StrokeCap.Round)
                )
            }

            Text(
                text = "완료",
                style = TextStyle(
                    color =  Color(0xFFFA6B80),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            
        }

        VerticalDivider(
            modifier = modifier
                .height(150.dp)
        )


        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "2024/05/01",
                style = TextStyle(
                    color = Color(0xFFFF8161),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = modifier.height(10.dp))

            FlowRow(
                modifier = modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                todoList.forEach {
                    Box(
                        modifier = modifier
                            .padding(end = 5.dp, bottom = 5.dp)
                            .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                            .padding(5.dp),
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