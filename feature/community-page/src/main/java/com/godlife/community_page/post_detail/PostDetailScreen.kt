package com.godlife.community_page.post_detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.designsystem.theme.CheckColor
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.PostDetailQuery

@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postId: String,
    postDetailViewModel: PostDetailViewModel = hiltViewModel()
) {

    postDetailViewModel.getPostDetail(postId)

    val postDetail by postDetailViewModel.postDetail.collectAsState()

    GodLifeTheme {

        LazyColumn(modifier.background(Color.White).fillMaxSize()) {

            if (postDetail.body?.imagesURL?.isNotEmpty() == true){
                item{ ImageBox(imgUriList = postDetail.body?.imagesURL!!) }
            }

            postDetail.body?.let {
                Log.e("postDetail", it.toString())
                item{ Content(postDetailBody = it) }
            }

        }





    }
}

@Composable
fun ImageBox(modifier: Modifier = Modifier, imgUriList: List<String>){
    val imgCount  = imgUriList.size
    var imgIndex by remember { mutableStateOf(1) }

    Box(
        modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.Black)
    ){

        LazyRow {
            itemsIndexed(imgUriList) {index, item ->
                ImageView(modifier, LocalContext.current, item)
                imgIndex = index+1
            }
        }

        Box(
            modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)){

            Box(modifier = modifier
                .size(width = 50.dp, height = 30.dp)
                .background(color = CheckColor, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${imgIndex}/${imgCount}", style = TextStyle(Color.White, fontSize = 15.sp), textAlign = TextAlign.Center)
            }

        }



    }
}

@Composable
fun ImageView(modifier: Modifier = Modifier, context: Context, imgUri: String){

    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    Glide.with(context)
        .asBitmap()
        .load(BuildConfig.SERVER_IMAGE_DOMAIN + imgUri)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
        
        Box(
            modifier
                .background(Color.Black),
            contentAlignment = Alignment.Center){

            Image(
                bitmap = fetchedBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .padding(10.dp)
            )
        }

    }
}

@Composable
fun Content(modifier: Modifier = Modifier,
            postDetailBody: PostDetailBody){

    Column(
        modifier
            .fillMaxWidth()
            .padding(20.dp)) {
        Row(
            modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically){

            //프로필 이미지 부분
            val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
            val imageModifier: Modifier = modifier
                .size(50.dp, 50.dp)
                .clip(CircleShape)
                .fillMaxSize()
                .background(color = GrayWhite)

            Glide.with(LocalContext.current)
                .asBitmap()
                .load(BuildConfig.SERVER_IMAGE_DOMAIN + postDetailBody.profileURL)
                .error(R.drawable.ic_person)
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
                    contentScale = ContentScale.FillWidth,
                    modifier = imageModifier
                )   //bitmap이 없다면
            } ?: Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier
            )

            Spacer(modifier.size(10.dp))

            Column {
                Text(text = postDetailBody.nickname, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Text(text = "postDetailBody.whoAmI", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp))
            }


        }

        Spacer(modifier.size(20.dp))

        Text(text = postDetailBody.title, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(text = postDetailBody.body, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

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

        Text(text = postDetailBody.writtenAt, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        //RowButton2()

    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(modifier.background(Color.White)) {


            LazyColumn(
                modifier.fillMaxSize()
            ) {

                /*
                item { LazyRow(modifier.fillMaxWidth().height(500.dp)) {
                    itemsIndexed()
                } }

                 */

                item {
                    ImageBoxPreview()
                }

                item { ContentPreview() }

            }
        }

    }
}

@Preview
@Composable
fun ImageBoxPreview(modifier: Modifier = Modifier){
    Box(
        modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.Black)
    ){



        Box(
            modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)){

            Box(modifier = modifier
                .size(width = 50.dp, height = 30.dp)
                .background(color = CheckColor, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "1/5", style = TextStyle(Color.White, fontSize = 15.sp), textAlign = TextAlign.Center)
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

@Preview(showBackground = true)
@Composable
fun TagItemPreview(modifier: Modifier = Modifier, text:String = "Tag"){
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Box(
            modifier
                .size(70.dp, 30.dp)
                .background(color = PurpleMain, shape = RoundedCornerShape(7.dp))
                .padding(2.dp)
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(text = text,
                style = TextStyle(color = Color.White),
                textAlign = TextAlign.Center,
            )
        }
    }

}