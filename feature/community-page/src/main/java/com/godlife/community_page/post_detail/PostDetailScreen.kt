package com.godlife.community_page.post_detail

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeCreateCommentBar
import com.godlife.designsystem.theme.CheckColor
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.CommentBody
import com.godlife.network.model.PostDetailBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postId: String,
    postDetailViewModel: PostDetailViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)

    val cScope = rememberCoroutineScope()

    //Ui State 관찰
    val uiState by postDetailViewModel.uiState.collectAsState()

    val postDetail by postDetailViewModel.postDetail.collectAsState()
    val comments by postDetailViewModel.comments.collectAsState()

    val writeComment by postDetailViewModel.writeComment.collectAsState()

    when(uiState){
        is PostDetailUiState.Loading -> {

            LoadingPostDetailScreen()

            postDetailViewModel.initPostDetailInfo(postId = postId)

        }

        is PostDetailUiState.Success -> {

            GodLifeTheme {

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    },
                ) {



                    Column {

                        Box(
                            modifier = modifier.weight(0.8f)
                        ) {
                            LazyColumn(
                                modifier
                                    .background(Color.White)
                                    .fillMaxSize()) {

                                if (postDetail.body?.imagesURL?.isNotEmpty() == true){
                                    item{ ImageBox(imgUriList = postDetail.body?.imagesURL!!) }
                                }

                                postDetail.body?.let {
                                    Log.e("postDetail", it.toString())
                                    item{ Content(postDetailBody = it) }
                                }

                                postDetail.body?.let {

                                    //갓생 인정을 안한 게시물이라면
                                    if(!it.memberLikedBoard){

                                        item {

                                            GodLifeButtonWhite(
                                                onClick = { postDetailViewModel.agreeGodLife() },
                                                text = { Text(text = "갓생, 인정하시나요?") }
                                            )
                                        }
                                    }
                                    //이미 갓생 인정을 한 게시물이라면
                                    else{

                                        item {

                                            GodLifeButton(
                                                onClick = { null },
                                                text = { Text(text = "이미 갓생 인정하신 글이에요.") }
                                            )
                                        }

                                    }


                                }

                                item { Comments(comments = comments, snackbarHostState = snackBarHostState, cScope = cScope, postDetailViewModel =  postDetailViewModel) }


                            }

                        }

                        GodLifeCreateCommentBar(
                            comment = writeComment,
                            onTextChanged = { postDetailViewModel.onWriteCommentChange(it) },
                            onPostClicked = { postDetailViewModel.createComment() }
                        )


                    }

                }


            }

        }
        is PostDetailUiState.Error -> {

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

                Text(text = postDetailBody.whoAmI, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp))
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


@Composable
fun Comments(modifier: Modifier = Modifier, comments: List<CommentBody>, snackbarHostState: SnackbarHostState, cScope: CoroutineScope, postDetailViewModel: PostDetailViewModel){

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(text = "댓글 ${comments.size}개", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier = modifier.size(10.dp))

        HorizontalDivider()

        Spacer(modifier = modifier.size(10.dp))

        comments.forEach {

            CommentBox(commentBody = it, snackbarHostState = snackbarHostState, cScope = cScope, postDetailViewModel =  postDetailViewModel)

        }

    }
}

@Composable
fun CommentBox(modifier: Modifier = Modifier, commentBody: CommentBody, snackbarHostState: SnackbarHostState, cScope: CoroutineScope, postDetailViewModel: PostDetailViewModel){

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)
    ){

        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){

            Row(modifier.weight(0.7f),
                verticalAlignment = Alignment.CenterVertically){

                //프로필 이미지 부분
                val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                val imageModifier: Modifier = modifier
                    .size(40.dp, 40.dp)
                    .clip(CircleShape)
                    .fillMaxSize()
                    .background(color = GrayWhite)

                Glide.with(LocalContext.current)
                    .asBitmap()
                    .load(BuildConfig.SERVER_IMAGE_DOMAIN + commentBody.profileURL)
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

                Text(text = commentBody.nickname, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                Spacer(modifier.size(10.dp))


            }
            Box(modifier.weight(0.2f), contentAlignment = Alignment.TopCenter){
                Text(text = commentBody.writtenAt, style = TextStyle(color = GrayWhite, fontSize = 15.sp))
            }

            IconButton(modifier = modifier.weight(0.1f), onClick = { expanded = !expanded} ){
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Comment Menu", tint = GrayWhite)

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {

                    if(commentBody.commentOwner) DropDownDeleteItem(snackbarHostState = snackbarHostState, cScope = cScope, postDetailViewModel =  postDetailViewModel, commentBody = commentBody)
                    else DropDownDeclareItem(snackbarHostState = snackbarHostState, cScope = cScope, postDetailViewModel=  postDetailViewModel, commentBody = commentBody)

                }
            }



        }

        Spacer(modifier.size(10.dp))

        Text(text = commentBody.comment, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))


    }

}


@Composable
fun DropDownDeclareItem(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState, cScope: CoroutineScope, postDetailViewModel: PostDetailViewModel, commentBody: CommentBody){

    DropdownMenuItem(
        text = { Text(text = "신고하기", style = TextStyle(color = GrayWhite)) },
        onClick = {
            cScope.launch {
                val result =
                    snackbarHostState.showSnackbar(
                        "해당 댓글을 신고할까요?",
                        actionLabel = "신고하기",
                        duration = SnackbarDuration.Long
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        /* Handle snackbar action performed */
                    }
                    SnackbarResult.Dismissed -> {
                        /* Handle snackbar dismissed */
                    }
                }
            }
        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = "신고하기", tint = GrayWhite)
        },
        colors = MenuDefaults.itemColors(Color.White)
    )
}

@Composable
fun DropDownDeleteItem(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState, cScope: CoroutineScope, postDetailViewModel: PostDetailViewModel, commentBody: CommentBody){

    DropdownMenuItem(
        text = { Text(text = "삭제하기", style = TextStyle(color = GrayWhite)) },
        onClick = {
            cScope.launch {
                val result =
                    snackbarHostState.showSnackbar(
                        "해당 댓글을 삭제할까요?",
                        actionLabel = "삭제하기",
                        duration = SnackbarDuration.Long
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        postDetailViewModel.deleteComment(commentBody.comment_id)
                    }
                    SnackbarResult.Dismissed -> {
                        /* Handle snackbar dismissed */
                    }
                }
            }
        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "삭제하기", tint = GrayWhite)
        },
        colors = MenuDefaults.itemColors(Color.White)
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingPostDetailScreen(modifier: Modifier = Modifier){
    GodLifeTheme {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview(modifier: Modifier = Modifier){
    GodLifeTheme {

        Column(modifier.background(Color.White)) {

            Box(
                modifier = modifier.weight(0.8f)
            ) {

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

                    item { CommentsPreview() }

                }
            }

            Box {
                GodLifeCreateCommentBar()
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

@Preview
@Composable
fun CommentsPreview(modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(text = "댓글 0개", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier = modifier.size(10.dp))

        HorizontalDivider()

        Spacer(modifier = modifier.size(10.dp))

        CommentBoxPreview()

        CommentBoxPreview()

        CommentBoxPreview()

    }
}

@Preview
@Composable
fun CommentBoxPreview(modifier: Modifier = Modifier){

    Column(
        modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)
    ){

        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){

            Row(modifier.weight(0.7f),
                verticalAlignment = Alignment.CenterVertically){

                //프로필 이미지 부분
                Box(
                    modifier
                        .size(40.dp)
                        .background(Color.Gray, shape = CircleShape)){
                }

                Spacer(modifier.size(10.dp))

                Text(text = "Name", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

                Spacer(modifier.size(10.dp))


            }
            Box(modifier.weight(0.2f), contentAlignment = Alignment.TopCenter){
                Text(text = "39분전", style = TextStyle(color = GrayWhite, fontSize = 15.sp))
            }

            IconButton(modifier = modifier.weight(0.1f), onClick = { } ){
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Comment Menu", tint = GrayWhite)
            }

        }

        Spacer(modifier.size(10.dp))

        Text(text = "Text", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))


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