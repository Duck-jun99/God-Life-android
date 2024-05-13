package com.godlife.createtodolist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.godlife.createtodolist.model.TodoList
import com.godlife.createtodolist.model.TodoListForm
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.theme.CheckColor
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GreyWhite
import com.godlife.designsystem.theme.PurpleMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTodoListScreen1(
    createViewModel: CreateViewModel = hiltViewModel(),
    //onNextClick: () -> Unit
){

    //val todoList = TodoList().getTodoList()

    val todoList by createViewModel.todoList.collectAsState()
    Log.e("todoList", todoList.toString())

    val selectedList by createViewModel.selectedList.collectAsState()
    Log.e("kjldaslkd",selectedList.toString())

    val navController = rememberNavController()



    GodLifeTheme {

        val coroutineScope = rememberCoroutineScope()

        val getOnClick: () -> Unit = {
            coroutineScope.launch {
                withContext(Dispatchers.IO){
                    createViewModel.getDatabase()
                }

            }
        }

        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {

            Text(
                text = "오늘 달성할 목표를 선택해주세요.",
                style = GodLifeTypography.titleLarge
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxHeight(0.7f)
            ) {
                itemsIndexed(todoList){ index, item ->
                    if(!item.isSelected) CardTodoList(item)
                    else SelectedCardTodoList(item)

                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
            ){

                Text(text = "선택한 목표",
                    style = GodLifeTypography.titleLarge
                )

                GodLifeButton(onClick = getOnClick) {
                    Text(text = "이전 목표 불러오기",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }


            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
            ) {
                itemsIndexed(selectedList) { index, item ->
                    SelectedTodoList(item)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /*
            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleMain
                )
                , modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f)){
                Text(text = "다음",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

             */

        }
    }
}



@Composable
fun CardTodoList(
    todoItem: TodoListForm,
    viewModel: CreateViewModel = hiltViewModel()
){

    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                //viewModel.addToSelectedList(title)
                viewModel.toggleSelect(todoItem)
            },
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .size(100.dp, 100.dp)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {

            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){

                Image(
                    painter = painterResource(id = todoItem.imgId),
                    contentDescription = ""
                )

            }


        }
        Text(text = todoItem.name)
    }



}

@Composable
fun SelectedCardTodoList(
    todoItem: TodoListForm,
    viewModel: CreateViewModel = hiltViewModel()
){

    Column(
        modifier = Modifier.padding(5.dp).clickable {
            //viewModel.addToSelectedList(title)
            viewModel.toggleSelect(todoItem)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(){
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                // Use custom label for accessibility services to communicate button's action to user.
                // Pass null for action to only override the label and not the actual action.
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp),
            ){
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(id = todoItem.imgId),
                        contentDescription = ""
                    )
                }

            }
            Box(modifier = Modifier
                .size(100.dp, 100.dp)
                .padding(16.dp)
                .background(CheckColor, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center){
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "",
                    tint = Color.White
                )
            }

        }


        Text(text = todoItem.name)
    }

}

@Composable
fun SelectedTodoList(name: String){

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleMain),
        // Use custom label for accessibility services to communicate button's action to user.
        // Pass null for action to only override the label and not the actual action.
        modifier = Modifier
            .size(100.dp, 30.dp)
            .padding(2.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp),
    ){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){

            Text(text = name,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            )
        }



    }


}

@Preview(showBackground = true)
@Composable
fun CreateTodoListScreen1Preview(){
    GodLifeTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "오늘 달성할 목표를 선택해주세요.",
                style = GodLifeTypography.titleLarge
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "리스트 부분",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .align(Alignment.CenterHorizontally)
            )

            Row(modifier = Modifier
                .fillMaxWidth()
            ){

                Text(text = "선택한 목표",
                    style = GodLifeTypography.titleLarge
                )

                GodLifeButton(onClick = { /*TODO*/ }) {
                    Text(text = "이전 목표 불러오기",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }



            Text(text = "리스트 부분",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))



        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardTodoListPreview(){

    Column(
        modifier = Modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            // Use custom label for accessibility services to communicate button's action to user.
            // Pass null for action to only override the label and not the actual action.
            modifier = Modifier
                .size(100.dp, 100.dp)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp),
        ){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Image(
                    painter = painterResource(id = R.drawable.carbon_clean),
                    contentDescription = ""
                )
            }



        }

        Text(text = "dasdas")
    }

}

@Preview(showBackground = true)
@Composable
fun SelectedCardTodoListPreview(){

    Column(
        modifier = Modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(){
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                // Use custom label for accessibility services to communicate button's action to user.
                // Pass null for action to only override the label and not the actual action.
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp),
            ){
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(id = R.drawable.carbon_clean),
                        contentDescription = ""
                    )
                }

            }
            Box(modifier = Modifier
                .size(100.dp, 100.dp)
                .padding(16.dp)
                .background(CheckColor, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center){
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "",
                    tint = Color.White
                )
            }

        }


        Text(text = "dasdas")
    }

}

@Preview(showBackground = true)
@Composable
fun SelectedTodoListPreview(){

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleMain),
        // Use custom label for accessibility services to communicate button's action to user.
        // Pass null for action to only override the label and not the actual action.
        modifier = Modifier
            .size(100.dp, 30.dp)
            .padding(2.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp),
    ){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){

            Text(text = "선택한 목표",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            )
        }



    }

}



