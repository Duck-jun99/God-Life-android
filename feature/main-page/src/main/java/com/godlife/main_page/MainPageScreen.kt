package com.godlife.main_page


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.todo.TodoList
import com.godlife.navigator.CreatePostNavigator
import com.godlife.navigator.CreatetodolistNavigator
import kotlinx.coroutines.delay


@Composable
fun MainPageScreen(
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator,
    createPostNavigator: CreatePostNavigator,
    viewModel: MainPageViewModel = hiltViewModel(),
    modifier:Modifier = Modifier.statusBarsPadding()
) {


    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {
        
        Log.e("MainPageScreen", viewModel.todoList.value.toString())
        
    }

    //공지용
    var showNotificationBox by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000) // 2초 대기
        showNotificationBox = true
    }

    val context = LocalContext.current

    val todayBoolean by viewModel.todayBoolean.collectAsState()

    GodLifeTheme {


        Column(
            modifier
                .fillMaxSize()
                .background(GrayWhite3)
        ){
            Box(
                modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier.weight(0.9f)){
                        Text(text = "Guest님 환영해요!", style = GodLifeTypography.titleMedium,)
                    }

                    Box(modifier.weight(0.1f)){

                        Icon(imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notification",
                            tint = GrayWhite,
                            modifier = modifier.align(Alignment.TopEnd))
                    }

                }
            }

            LazyColumn(
                modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {

                if (showNotificationBox){
                    item { AnimatedVisibility(visible = true, enter = fadeIn(initialAlpha = 0.5f) ) {
                        NotificationBox()
                    } }
                }

                item { Spacer(modifier = modifier.size(10.dp)) }


                //item {Text(text = viewModel.todayTimeText("Guest"), style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center) }
                item { TextToday(viewModel) }

                item { Spacer(modifier = modifier.size(10.dp)) }

                if (todayBoolean) {
                    item{ MainTodoListBox(viewModel)}
                } else {
                    item{ MainNoTodoListBox(context, mainActivity, createNavigator) }
                }

                item { Spacer(modifier = modifier.size(30.dp)) }

                if (todayBoolean) {

                    //item {Text(text = "오늘의 투두리스트", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center) }

                    item { Row(modifier.fillMaxWidth()
                        .height(25.dp)){
                        Icon(painter = painterResource(R.drawable.note_icons8), contentDescription = "", tint = Color.Unspecified)
                        Spacer(modifier.size(5.dp))
                        Text(text = "오늘의 투두리스트", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center) }
                    }

                    item { Spacer(modifier = modifier.size(10.dp)) }

                    item{ TodoListBox(viewModel)}
                }

            }

        }
    }

}

@Composable
fun MainTodoListBox(viewModel: MainPageViewModel,
                    modifier: Modifier = Modifier){

    var todoListCount = viewModel.getTodoListCount()

    var todoPercent = 360 * (todoListCount[1].toFloat() / todoListCount[0].toFloat())

    val animatedValue = remember { Animatable(0f) }


    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            //targetValue = targetvalue,
            targetValue = todoPercent,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        )
    }



    Box(modifier = modifier
        .fillMaxWidth()
        .height(360.dp)
        .background(
            color = Color.White,
            shape = RoundedCornerShape(20.dp)
        )){

        Column(
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            ) {

                Column(modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "진행상황", style = TextStyle(color = GrayWhite, fontSize = 15.sp), textAlign = TextAlign.Center)
                    Text(text = "${todoListCount[1]} / ${todoListCount[0]}", style = TextStyle(color = PurpleMain, fontSize = 25.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
                }



                Canvas(
                    modifier = Modifier.fillMaxSize()
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
                                Color(0xff63C6C4), PurpleMain
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
    }

}


@Composable
fun MainNoTodoListBox(context: Context,
                      mainActivity: Activity,
                      createNavigator: CreatetodolistNavigator,
                      modifier: Modifier = Modifier){

    val animatedValue = remember { Animatable(0f) }


    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            //targetValue = targetvalue,
            targetValue = 360f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        )
    }



    Box(modifier = modifier
        .fillMaxWidth()
        .height(450.dp)
        .background(
            color = Color.White,
            shape = RoundedCornerShape(20.dp)
        )){

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            ) {

                Column(modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "오늘의 투두리스트를\n 만들어주세요!", style = TextStyle(color = GrayWhite, fontSize = 15.sp), textAlign = TextAlign.Center)
                    //Text(text = "2/5", style = TextStyle(color = PurpleMain, fontSize = 25.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
                }

                Canvas(
                    modifier = Modifier.fillMaxSize()
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
                                Color(0xff63C6C4), PurpleMain
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

            Box(modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center){

                GodLifeButtonWhite(
                    onClick = { moveCreateActivity(createNavigator, mainActivity) },
                    text = { Text(text = "투두 리스트 만들기", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                )

            }

        }
    }

}

@Composable
fun TodoListBox(
    viewModel:MainPageViewModel,
    modifier: Modifier = Modifier){

    val todayTodoList by viewModel.todoList.collectAsState()

    Column(modifier = modifier
        .fillMaxWidth()
        .background(
            color = Color.White,
            shape = RoundedCornerShape(20.dp)
        )){

        /*
        itemsIndexed(todayTodoList.todoList){index, item ->
            if(item.iscompleted) CompletedTodayList(item, viewModel) else NoCompletedTodayList(item, viewModel)
        }

         */

        todayTodoList.todoList.forEachIndexed { index, item ->
            if (item.iscompleted) {
                CompletedTodayList(item, viewModel)
            } else {
                NoCompletedTodayList(item, viewModel)
            }
        }

    }
}


@Composable
fun NoCompletedTodayList(
    todo: TodoList,
    viewModel: MainPageViewModel
){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
                .padding(10.dp)
        ) {
            Text(text = todo.name,
                style = TextStyle(fontSize = 20.sp, color = PurpleMain)
            )

            Divider(
                color = PurpleMain,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            GodLifeButton(
                onClick = {
                    viewModel.completeTodo(todo)
                },
                modifier = Modifier.align(Alignment.End)) {
                Text(text = "달성하기", style = TextStyle(color = Color.White))
            }

        }
    }
}

@Composable
fun CompletedTodayList(
    todo: TodoList,
    viewModel: MainPageViewModel
){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
                .padding(10.dp)
        ) {
            Text(text = todo.name,
                style = TextStyle(fontSize = 20.sp, color = GrayWhite)
            )

            Divider(
                color = GrayWhite,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(GrayWhite)) {
                Text(text = "달성하기", style = TextStyle(color = Color.White))
            }

        }
    }
}

//MainTodoListBox위에 보여질 Text
@Composable
fun TextToday(viewModel: MainPageViewModel, modifier: Modifier = Modifier){
    val item = viewModel.todayTimeText("GUEST")
    //item[0] -> Text, item[1] -> Icon resource


    Row(modifier.fillMaxWidth()
        .height(25.dp)){
        Icon(painter = painterResource(item[1].toString().toInt()), contentDescription = "", tint = Color.Unspecified)
        Spacer(modifier.size(5.dp))
        Text(text = item[0].toString(), style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
    }
}


//Preview

@Preview
@Composable
fun NotificationBox(modifier: Modifier = Modifier){
    Card(modifier = modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(top = 10.dp, bottom = 20.dp)
        .background(
            color = Color.White,
            shape = RoundedCornerShape(20.dp)
        ),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(Color.White)){

        Row(
            modifier.padding(20.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Email,
                contentDescription = "Message",
                modifier
                    .weight(0.2f)
                    .size(100.dp),
                tint = PurpleMain)

            Box(
                modifier
                    .weight(0.8f)
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically)){
                Text(text = "공지가 도착했어요!", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center)

            }

        }

    }
}
/*
@Preview
@Composable
fun CompletedTodoListBoxPreview() {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(listOf(PurpleSecond, PurpleMain)),
                shape = RoundedCornerShape(30.dp),
                alpha = 0.8f
            )
            .size(300.dp)
            .padding(20.dp),
    ) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.4f)){

            Text(
                text = "오늘의 투두를 모두 완료했어요!\n인증 게시물을 올려보세요!",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.BottomStart)
            )


        }

        Box(modifier = Modifier
            .padding(vertical = 10.dp)
            .weight(0.2f))
        {
            Divider(
                color = Color.White,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .align(Alignment.Center)
            )
        }


        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.4f)){

            GodLifeButtonWhite(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.Center),
                text = { Text(text = "게시물 작성", style = TextStyle(fontWeight = FontWeight.Bold)) }
            )

        }

    }
}

 */

@Preview(showBackground = true)
@Composable
fun NoCompletedTodayListPreview(){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
                .padding(10.dp)
        ) {
            Text(text = "아침 식사",
                style = TextStyle(fontSize = 20.sp, color = PurpleMain)
            )

            Divider(
                color = PurpleMain,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            GodLifeButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End)) {
                Text(text = "달성하기", style = TextStyle(color = Color.White))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompletedTodayListPreview(){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
                .padding(10.dp)
        ) {
            Text(text = "아침 식사",
                style = TextStyle(fontSize = 20.sp, color = GrayWhite)
            )

            Divider(
                color = GrayWhite,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(GrayWhite)) {
                Text(text = "달성하기", style = TextStyle(color = Color.White))
            }

        }
    }
}

//MainTodoListBox위에 보여질 Text
@Preview(showBackground = true)
@Composable
fun TextTodayPreview(modifier: Modifier = Modifier){
    Row(modifier.fillMaxWidth()
        .height(20.dp)){
        Icon(painter = painterResource(R.drawable.sun_icons8), contentDescription = "", tint = Color.Unspecified)
        Spacer(modifier.size(5.dp))
        Text(text = "Hello World!", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
    }
}

private fun moveCreateActivity(createNavigator: CreatetodolistNavigator, mainActivity: Activity){
    createNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = false
    )

}

private fun moveCreatePostActivity(createPostNavigator: CreatePostNavigator, mainActivity: Activity){
    createPostNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = false
    )

}
