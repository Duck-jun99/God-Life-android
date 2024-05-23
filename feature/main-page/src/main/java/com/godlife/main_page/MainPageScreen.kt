package com.godlife.main_page


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.createtodolist.CardTodoList
import com.godlife.createtodolist.CreateActivity
import com.godlife.createtodolist.SelectedCardTodoList
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.NullColor
import com.godlife.designsystem.theme.Purple40
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.theme.PurpleSecond
import com.godlife.model.todo.TodoList
import com.godlife.navigator.CreatePostNavigator
import com.godlife.navigator.CreatetodolistNavigator


@Composable
fun MainPageScreen(
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator,
    createPostNavigator: CreatePostNavigator,
    viewModel: MainPageViewModel = hiltViewModel()
) {


    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)
    LaunchedEffect(key1 = true) {
        
        Log.e("MainPageScreen", viewModel.todoList.value.toString())
        
    }

    val context = LocalContext.current


    val todayTodoList by viewModel.todoList.collectAsState()
    val todayBoolean by viewModel.todayBoolean.collectAsState()

    GodLifeTheme {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UserInfo()
            
            //NoTodoListBox(context, mainActivity, createNavigator)


            //CreatePost 테스트용 임시
            Button(onClick = { moveCreatePostActivity(createPostNavigator, mainActivity) }) {
                Text(text = "CreatePost")
            }


            if (todayBoolean) {
                TodoListBox(viewModel)
            } else {
                NoTodoListBox(context, mainActivity, createNavigator)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxHeight(0.7f)
            ) {
                itemsIndexed(todayTodoList.todoList){index, item ->
                    if(item.iscompleted) CompletedTodayList(item, viewModel) else NoCompletedTodayList(item, viewModel)
                }
            }

        }
    }

}



@Composable
fun NoTodoListBox(
    context: Context,
    mainActivity: Activity,
    createNavigator: CreatetodolistNavigator
) {

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
                text = "오늘의 투두 리스트를\n만들어주세요!",
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


        Button(
            onClick = {
                      moveCreateActivity(createNavigator, mainActivity)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            colors = ButtonDefaults.buttonColors(
                containerColor = NullColor
            ),
        ) {
            Text(text = "> 투두 리스트 만들기 가기",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Top),
                textAlign = TextAlign.Right
            )

        }

    }
}

@Composable
fun TodoListBox(
    viewModel: MainPageViewModel
) {

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
                text = "진행 상황",
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

            var todoListCount = viewModel.getTodoListCount()

            Text(
                text = "${todoListCount[1]} / ${todoListCount[0]}",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.TopEnd)
            )


        }

    }
}

@Composable
fun CompletedTodoListBox() {
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
                style = TextStyle(fontSize = 20.sp, color = GreyWhite)
            )

            Divider(
                color = GreyWhite,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(GreyWhite)) {
                Text(text = "달성하기", style = TextStyle(color = Color.White))
            }

        }
    }
}



//Preview

@Preview
@Composable
fun NoTodoListBoxPreview() {
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
                text = "오늘의 투두 리스트를\n만들어주세요!",
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


        Button(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            colors = ButtonDefaults.buttonColors(
                containerColor = NullColor
            ),
        ) {
            Text(text = "> 투두 리스트 만들기 가기",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Top),
                textAlign = TextAlign.Right
            )

        }

    }
}

@Preview
@Composable
fun TodoListBoxPreview() {
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
                text = "진행 상황",
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

            Text(
                text = "2/5",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.TopEnd)
            )


        }

    }
}

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
                style = TextStyle(fontSize = 20.sp, color = GreyWhite)
            )

            Divider(
                color = GreyWhite,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(GreyWhite)) {
                Text(text = "달성하기", style = TextStyle(color = Color.White))
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview(){
    GodLifeTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            NoTodoListBoxPreview()

            Spacer(modifier = Modifier.height(30.dp))


        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfo(){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(50.dp)
        ) {
            Text(text = "Guset님 환영해요!", style = GodLifeTypography.titleMedium)
            Spacer(modifier = Modifier.size(5.dp))
            Divider(modifier = Modifier
                .fillMaxWidth()
                .size(5.dp), color = PurpleMain)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodayTodoListPreview(){
    GodLifeTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxHeight(0.7f)
            ) {
                item {  }
            }
        }
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
