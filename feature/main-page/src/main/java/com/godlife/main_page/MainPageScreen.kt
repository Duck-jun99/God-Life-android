package com.godlife.main_page


import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.database.model.TodoEntity
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OrangeLight
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.navigation.HistoryPageRoute
import com.godlife.main_page.update.UpdateAlertDialog
import com.godlife.model.todo.TodoList
import com.godlife.navigator.CreatePostNavigator
import com.godlife.navigator.CreatetodolistNavigator
import com.godlife.navigator.LoginNavigator
import com.godlife.profile.navigation.ProfileScreenRoute
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainPageScreen(
    modifier:Modifier = Modifier,
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator,
    createPostNavigator: CreatePostNavigator,
    loginNavigator: LoginNavigator,
    navController: NavController,
    viewModel: MainPageViewModel = hiltViewModel()
) {


    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)


    //굿생 인증 완료용
    var showCompleteTodayBox by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        delay(2000) // 2초 대기
        showCompleteTodayBox = true
    }


    //Ui State 관찰
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val todayTodoListExists by viewModel.todayTodoListExists.collectAsState()

    val userInfo by viewModel.userInfo.collectAsState()

    val showTodoAlertDialog by viewModel.showTodoAlertDialog.collectAsState()
    val showUpdateAlertDialog by viewModel.showUpdateAlertDialog.collectAsState()

    val todayTodoList by viewModel.todayTodoList.collectAsState()

    LaunchedEffect(todayTodoList) {
        Log.e("MainPageScreen", todayTodoList?.toString() ?: "TodoList is null")
    }

    GodLifeTheme {

        when(uiState){
            is MainPageUiState.Loading -> {

                LoadingMainPageScreen()

            }

            is MainPageUiState.Success -> {

                viewModel.setFcmToken()

                Column(
                    modifier
                        .fillMaxSize()
                        .background(GrayWhite3)
                        .statusBarsPadding()
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

                            //Text(text = "${userInfo.nickname}님 환영해요!", style = GodLifeTypography.titleMedium)

                            /*
                            Text(text = "Good Life",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    lineHeight = 28.sp,
                                    letterSpacing = 0.sp
                                )
                            )

                             */


                            Image(
                                modifier = modifier
                                    .size(height = 70.dp, width = 100.dp),
                                painter = painterResource(id = R.drawable.goodlife_inside_logo),
                                contentDescription = "",
                            )


                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                //프로필 사진
                                GlideImage(
                                    imageModel = { if(userInfo.profileImage != "") BuildConfig.SERVER_IMAGE_DOMAIN + userInfo.profileImage else R.drawable.ic_person },
                                    imageOptions = ImageOptions(
                                        contentScale = ContentScale.FillWidth,
                                        alignment = Alignment.Center
                                    ),
                                    modifier = modifier
                                        .size(30.dp, 30.dp)
                                        .clip(CircleShape)
                                        .fillMaxSize()
                                        .background(color = GrayWhite2)
                                        .clickable { navController.navigate("${ProfileScreenRoute.route}/${userInfo.memberId}") }
                                )

                                Spacer(modifier.size(10.dp))

                                IconButton(
                                    modifier = modifier
                                        .size(30.dp),
                                    onClick = {
                                        navController.navigate(HistoryPageRoute.route){
                                            launchSingleTop = true
                                        }
                                    }
                                ) {

                                    Icon(
                                        painter = painterResource(R.drawable.cases_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                        contentDescription = "",
                                        modifier = modifier
                                            .graphicsLayer(alpha = 0.99f)
                                            .drawWithCache {
                                                onDrawWithContent {
                                                    drawContent()
                                                    drawRect(
                                                        brush = Brush.linearGradient(
                                                            listOf(
                                                                Color(0xFFFF44A2),
                                                                Color(0xFFFF5890),
                                                                Color(0xFFFA6B80),
                                                                Color(0xFFFF7B75),
                                                                Color(0xFFFF8161),
                                                                Color(0xFFFF884D)
                                                            )
                                                        ),
                                                        blendMode = BlendMode.SrcAtop
                                                    )
                                                }
                                            }
                                    )

                                    /*
                                    Icon(imageVector = Icons.Outlined.DateRange,
                                        contentDescription = "History",
                                        tint = Color.Black
                                    )
                                     */
                                }

                            }

                        }
                    }

                    LazyColumn(
                        modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {

                        if (showCompleteTodayBox){
                            item {

                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(initialAlpha = 0.5f
                                    )
                                ) {
                                    CompleteTodayBox(
                                        createPostNavigator = createPostNavigator,
                                        mainActivity = mainActivity
                                    )
                                }

                            }
                        }

                        item { Spacer(modifier = modifier.size(10.dp)) }


                        item {
                            TextToday(viewModel)
                            Spacer(modifier = modifier.size(10.dp))
                        }

                        when{
                            //오늘 설정한 투두리스트가 있을 경우
                            todayTodoList != null -> {

                                item {
                                    todayTodoList?.let { todo ->
                                        MainTodoListBox(
                                            viewModel = viewModel,
                                            todo = todo
                                        )
                                        Spacer(modifier = modifier.size(10.dp))
                                    }
                                }

                                item {
                                    Row(
                                        modifier
                                            .fillMaxWidth()
                                            .height(25.dp)
                                    ){
                                        Icon(painter = painterResource(R.drawable.note_icons8), contentDescription = "", tint = Color.Unspecified)
                                        Spacer(modifier.size(5.dp))
                                        Text(text = "오늘의 투두리스트", style = TextStyle(color = GrayWhite, fontSize = 18.sp), textAlign = TextAlign.Center)
                                    }
                                    Spacer(modifier = modifier.size(10.dp))
                                }


                                item {
                                    todayTodoList?.let { todo ->
                                        TodoListBox(
                                            viewModel = viewModel,
                                            todo = todo
                                        )
                                    }
                                }

                            }

                            //오늘 설정한 투두리스트가 없을 경우
                            else -> {

                                item{ MainNoTodoListBox(mainActivity, createNavigator) }

                            }
                        }

                        // item { Spacer(modifier = modifier.size(30.dp)) }


                    }

                }

                if(showTodoAlertDialog){
                    TodoAlertDialog(viewModel)
                }

                if(showUpdateAlertDialog){
                    todayTodoList?.id?.let {
                        UpdateAlertDialog(
                            viewModel = viewModel,
                            onUpdateComplete = {
                                viewModel.getTodayTodoList()
                                Log.e("onUpdateComplete", "${viewModel.todayTodoList.value}")
                            },
                            todoId = it
                        )
                    }
                }

            }
            is MainPageUiState.Error -> {

                Toast.makeText(context, (uiState as MainPageUiState.Error).message.toString(), Toast.LENGTH_SHORT).show()

                if((uiState as MainPageUiState.Error).message == ErrorType.REFRESH_TOKEN_EXPIRED){
                    moveLoginActivity(loginNavigator, mainActivity)
                }

            }
        }

    }

}

@Composable
fun TodoAlertDialog(
    viewModel: MainPageViewModel
){

    val selectedTodo by viewModel.selectedTodo.collectAsState()

    val cScope = rememberCoroutineScope()

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { viewModel.setTodoAlertDialogFlag() },
        title = {
            Text(text = selectedTodo.name, style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            Text(text = "해당 목표를 달성하셨나요?", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {

                    cScope.launch(Dispatchers.IO) {
                        viewModel.setTodoValueCompleted(selectedTodo)
                    }

                    viewModel.setTodoAlertDialogFlag()

                },
                text = { Text(text = "달성하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
            )
        },
        dismissButton = {
            GodLifeButtonWhite(
                onClick = { viewModel.setTodoAlertDialogFlag() },
                text = { Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
            )
        }
    )

}



@Composable
fun MainTodoListBox(
    viewModel: MainPageViewModel,
    modifier: Modifier = Modifier,
    todo: TodoEntity
){

    val dropDownVisble = viewModel.dropDownVisble.collectAsState().value

    val todayTodoListSize = viewModel.todayTodoListSize.collectAsState().value
    val completedTodoListSize = viewModel.completedTodoListSize.collectAsState().value
    Log.e("MainTodoListBox", "todayTodoListSize: $todayTodoListSize, completedTodoListSize: $completedTodoListSize")

    val todoPercent =
        if (completedTodoListSize > 0) {
            360 * ( completedTodoListSize.toFloat() / todayTodoListSize.toFloat() )
        }
        else { 0f }
    Log.e("MainTodoListBox", "todoPercent: $todoPercent")

    val animatedValue = remember { Animatable(0f) }


    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(todoPercent) {
        animatedValue.animateTo(
            targetValue = todoPercent,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "진행상황", style = TextStyle(color = GrayWhite, fontSize = 15.sp), textAlign = TextAlign.Center)
            Text(text = "$completedTodoListSize / $todayTodoListSize", style = TextStyle(color = Color(0xFFFA6B80), fontSize = 25.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
        }


        Canvas(
            modifier = Modifier
                .size(300.dp)
        ) {
            val size: Size = drawContext.size
            val sizeArc = size / 1.5F
            drawArc(
                color = Color(0xFFE1E2E9),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset((size.width - sizeArc.width) / 2f, (size.height - sizeArc.height) / 2f),
                size = sizeArc,
                style = Stroke(width = 30f)
            )

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
                sweepAngle = animatedValue.value,
                useCenter = false,
                topLeft = Offset(
                    (size.width - sizeArc.width) / 2f,
                    (size.height - sizeArc.height) / 2f
                ),
                size = sizeArc,
                style = Stroke(width = 30f, cap = StrokeCap.Round)
            )
        }

        Box(
            modifier = modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .align(Alignment.TopEnd)
        ){
            IconButton(
                modifier = modifier
                    .size(30.dp),
                onClick = { viewModel.setDropDownVisble() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "",
                    tint = GrayWhite
                )
                DropdownMenu(
                    expanded = dropDownVisble,
                    onDismissRequest = { viewModel.setDropDownVisble() },
                    containerColor = Color.White
                ) {

                    Column {

                        DropdownMenuItem(
                            text = { Text(text = "투두리스트 수정", style = TextStyle(color = PurpleMain)) },
                            onClick = {
                                viewModel.setDropDownVisble()
                                viewModel.setUpdateAlertDialogFlag(category = "EDIT_TODOLIST")
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "수정하기", tint = PurpleMain)
                            },
                            colors = MenuDefaults.itemColors(Color.White)
                        )

                        DropdownMenuItem(
                            text = { Text(text = "알림 시간 수정", style = TextStyle(color = PurpleMain)) },
                            onClick = {
                                viewModel.setDropDownVisble()
                                viewModel.setUpdateAlertDialogFlag(category = "EDIT_NOTIFICATION_TIME")
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "수정하기", tint = PurpleMain)
                            },
                            colors = MenuDefaults.itemColors(Color.White)
                        )

                        DropdownMenuItem(
                            text = { Text(text = "삭제하기", style = TextStyle(color = PurpleMain)) },
                            onClick = {
                                viewModel.setDropDownVisble()
                                viewModel.setUpdateAlertDialogFlag(category = "DELETE")

                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "삭제하기", tint = PurpleMain)
                            },
                            colors = MenuDefaults.itemColors(Color.White)
                        )

                    }

                }
            }
        }

    }

}


@Composable
fun MainNoTodoListBox(
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator,
    modifier: Modifier = Modifier
){

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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(300.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "오늘의 투두리스트를\n 만들어주세요!",
                    style = TextStyle(color = GrayWhite, fontSize = 15.sp),
                    textAlign = TextAlign.Center
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val size: Size = drawContext.size
                    val sizeArc = size / 1.5F
                    drawArc(
                        color = Color(0xFFE1E2E9),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset((size.width - sizeArc.width) / 2f, (size.height - sizeArc.height) / 2f),
                        size = sizeArc,
                        style = Stroke(width = 30f)
                    )

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
                        sweepAngle = animatedValue.value,
                        useCenter = false,
                        topLeft = Offset(
                            (size.width - sizeArc.width) / 2f,
                            (size.height - sizeArc.height) / 2f
                        ),
                        size = sizeArc,
                        style = Stroke(width = 30f, cap = StrokeCap.Round)
                    )
                }
            }

            Box(modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center){

                GodLifeButtonWhite(
                    onClick = { moveCreateTodoListActivity(createNavigator, mainActivity) },
                    text = { Text(text = "투두 리스트 만들기", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                )

            }

        }
    }

}

@Composable
fun TodoListBox(
    viewModel:MainPageViewModel,
    todo: TodoEntity,
    modifier: Modifier = Modifier
){

    //val todayTodoList by viewModel.todayTodoList.collectAsState()

    Log.e(" TodoListBox ", "${todo}")

    Column(modifier = modifier
        .fillMaxWidth()
        .background(
            color = Color.White,
            shape = RoundedCornerShape(20.dp)
        )){

        /*
        itemsIndexed(todayTodoList.todayTodoList){index, item ->
            if(item.iscompleted) CompletedTodayList(item, viewModel) else NoCompletedTodayList(item, viewModel)
        }

         */

        todo.todoList.forEach { item ->
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

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 10.dp),
                thickness = 2.dp,
                color = PurpleMain
            )

            GodLifeButton(
                onClick = {
                          viewModel.setTodoAlertDialogFlag(todo)
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

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 10.dp),
                thickness = 2.dp,
                color = GrayWhite
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(GrayWhite)) {
                Text(text = "달성 완료", style = TextStyle(color = Color.White))
            }

        }
    }
}

@Composable
fun CompleteTodayBox(
    modifier: Modifier = Modifier,
    createPostNavigator: CreatePostNavigator,
    mainActivity: Activity
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Icon(
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription ="",
            tint = PurpleMain,
            modifier = modifier
                .size(25.dp)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFFF44A2),
                                    Color(0xFFFF5890),
                                    Color(0xFFFA6B80),
                                    Color(0xFFFF7B75),
                                    Color(0xFFFF8161),
                                    Color(0xFFFF884D)
                                )
                            ),
                            blendMode = BlendMode.SrcAtop
                        )
                    }
                }
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "오늘 목표를 모두 달성하셨군요!",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier.size(5.dp))

        Text(
            text = "굿생 인증을 하고 하루를 마무리 해주세요.",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier.size(10.dp))

        GodLifeButtonWhite(
            onClick = {
                moveCreatePostActivity(createPostNavigator, mainActivity)
                      },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "",
                    tint = PurpleMain
                )
            },
            text = {
                Text(
                    text = "굿생 인증",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        )

    }
}

//MainTodoListBox위에 보여질 Text
@Composable
fun TextToday(viewModel: MainPageViewModel, modifier: Modifier = Modifier){
    val item = viewModel.setTodayTimeText()
    //item[0] -> Text, item[1] -> Icon resource

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            modifier = modifier.size(25.dp),
            painter = painterResource(item[1].toString().toInt()),
            contentDescription = "",
            tint = Color.Unspecified
        )
        Spacer(modifier.size(5.dp))

        Text(
            text = item[0].toString(),
            style = TextStyle(color = GrayWhite, fontSize = 18.sp)
        )

    }
}

@Preview
@Composable
fun MainTodoListBoxPreview(
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
            .fillMaxWidth()
            .height(360.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "진행상황", style = TextStyle(color = GrayWhite, fontSize = 15.sp), textAlign = TextAlign.Center)
            Text(text = "$completedTodoListSize / $todayTodoListSize", style = TextStyle(color = PurpleMain, fontSize = 25.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
        }


        Canvas(
            modifier = Modifier
                .size(300.dp)
        ) {
            val size: Size = drawContext.size
            val sizeArc = size / 1.5F
            drawArc(
                color = Color(0xFFE1E2E9),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset((size.width - sizeArc.width) / 2f, (size.height - sizeArc.height) / 2f),
                size = sizeArc,
                style = Stroke(width = 30f)
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
                style = Stroke(width = 30f, cap = StrokeCap.Round)
            )
        }

        Box(
            modifier = modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .align(Alignment.TopEnd)
        ){
            IconButton(
                modifier = modifier
                    .size(30.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "",
                    tint = PurpleMain
                )
            }
        }
    }

}
//Preview

@Preview
@Composable
fun MainNoTodoListBoxPreview(
    modifier: Modifier = Modifier
){

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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(360.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "오늘의 투두리스트를\n 만들어주세요!",
                    style = TextStyle(color = GrayWhite, fontSize = 15.sp),
                    textAlign = TextAlign.Center
                )

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
                    onClick = {  },
                    text = { Text(text = "투두 리스트 만들기", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                )

            }

        }
    }

}

@Preview
@Composable
fun LoadingMainPageScreen(modifier: Modifier = Modifier){
    GodLifeTheme(modifier.background(Color.White)) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteTodayBoxPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Icon(
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription ="",
            tint = PurpleMain,
            modifier = modifier
                .size(25.dp)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFFF44A2),
                                    Color(0xFFFF5890),
                                    Color(0xFFFA6B80),
                                    Color(0xFFFF7B75),
                                    Color(0xFFFF8161),
                                    Color(0xFFFF884D)
                                )
                            ),
                            blendMode = BlendMode.SrcAtop
                        )
                    }
                }
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "오늘 목표를 모두 달성하셨군요!",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier.size(5.dp))

        Text(
            text = "굿생 인증을 하고 하루를 마무리 해주세요.",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier.size(10.dp))

        GodLifeButtonWhite(
            onClick = { /*TODO*/ },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "",
                    tint = PurpleMain
                )
            },
            text = {
                Text(
                    text = "굿생 인증",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        )

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
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
        ){
        Icon(
            modifier = modifier.size(25.dp),
            painter = painterResource(R.drawable.sun_icons8),
            contentDescription = "",
            tint = Color.Unspecified
        )

        Spacer(modifier.size(5.dp))

        Text(text = "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!", style = TextStyle(color = GrayWhite, fontSize = 18.sp))
    }
}

private fun moveCreateTodoListActivity(createNavigator: CreatetodolistNavigator, mainActivity: Activity){
    createNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = true
    )

}

private fun moveCreatePostActivity(createPostNavigator: CreatePostNavigator, mainActivity: Activity){
    createPostNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = true
    )

}

private fun moveLoginActivity(loginNavigator: LoginNavigator, mainActivity: Activity){
    loginNavigator.navigateFrom(
        activity = mainActivity,
        withFinish = true
    )
}

@Preview(showBackground = true)
@Composable
fun TabBarPreview(modifier: Modifier = Modifier){

    Row(
        modifier
            .height(70.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Text(text = "${userInfo.nickname}님 환영해요!", style = GodLifeTypography.titleMedium)

        /*
        Text(text = "Good Life",
            style = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            )
        )

         */


        Image(
            modifier = modifier
                .size(height = 70.dp, width = 100.dp),
            painter = painterResource(id = R.drawable.goodlife_inside_logo),
            contentDescription = "",
        )
    }

}