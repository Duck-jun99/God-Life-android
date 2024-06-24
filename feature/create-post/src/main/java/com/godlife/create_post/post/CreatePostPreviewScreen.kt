package com.godlife.create_post.post

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.PurpleMain

@Composable
fun CreatePostPreviewScreen(
    createPostViewModel: CreatePostViewModel,
    navController: NavController,
    modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(modifier.background(Color.White)) {

            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "미리 보기", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }

            LazyColumn(
                modifier.fillMaxSize()
            ) {

                
                item { LazyRow(
                    modifier
                        .fillMaxWidth()
                        .height(400.dp)) {
                    createPostViewModel.selectedImgUri.value?.let {
                        itemsIndexed(it){index, item ->
                            ImageView(imgUri = item, context = LocalContext.current)
                        }
                    }

                } }


                item { Content(createPostViewModel, navController) }

            }
        }

    }
}

@Composable
fun Content(
    createPostViewModel: CreatePostViewModel,
    navController: NavController,
    modifier: Modifier = Modifier){
    Column(
        modifier
            .fillMaxWidth()
            .padding(20.dp)) {
        Row(
            modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically){

            Box(
                modifier
                    .background(PurpleMain, shape = CircleShape)
                    .size(70.dp))

            Spacer(modifier.size(10.dp))

            Column {
                Text(text = "Nickname", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Text(text = "Introduce", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp))
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = createPostViewModel.title.value, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(text = createPostViewModel.text.value, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            items(5) {
                TagItemPreview()
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = "yyyy-mm-dd(게시물 올린 날짜,시간)", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        RowButton2(navController)

    }
}

@Composable
fun ImageView(imgUri: Uri, context: Context){
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    //Image 부분
    val imageModifier: Modifier = Modifier
        .fillMaxSize()

    Glide.with(context)
        .asBitmap()
        .load(imgUri)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
        Box(Modifier.fillMaxSize().background(Color.Black)){

            Image(
                bitmap = fetchedBitmap,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreviewScreenPreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(modifier.background(Color.White)) {

            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "미리 보기", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }

            LazyColumn(
                modifier.fillMaxSize()
            ) {

                /*
                item { LazyRow(modifier.fillMaxWidth().height(500.dp)) {
                    itemsIndexed()
                } }

                 */

                item { Box(
                    modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(GrayWhite)){

                }}

                item { ContentPreview() }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview(modifier: Modifier = Modifier){
    Column(
        modifier
            .fillMaxWidth()
            .padding(20.dp)) {
        Row(
            modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically){

            Box(
                modifier
                    .background(PurpleMain, shape = CircleShape)
                    .size(70.dp))

            Spacer(modifier.size(10.dp))

            Column {
                Text(text = "Nickname", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Text(text = "Introduce", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp))
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = "Title", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(text = "text", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            items(5) {
                TagItemPreview()
            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = "yyyy-mm-dd(게시물 올린 날짜,시간)", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        //RowButton2()

    }
}

@Composable
fun RowButton2(navController: NavController){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically){
        Box(
            Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)){

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate("CreatePostScreen") },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Text(
                    text = "수정하기",
                    color = PurpleMain,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Box(
            Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)){

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                colors = CardDefaults.cardColors(PurpleMain)
            ) {
                Text(
                    text = "작성 완료",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EX(){
    Box(Modifier.size(300.dp)
        .background(Color.Black)){

        Box(Modifier.size(300.dp)
            .padding(10.dp)
            .background(Color.White)
            .fillMaxSize()){

        }
    }
}