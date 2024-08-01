package com.godlife.community_page.post_detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.post_detail.post_update.post.UpdatePostScreen
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeCreateCommentBar
import com.godlife.designsystem.component.GodLifeCreateCommentBarPreview
import com.godlife.designsystem.list.TagItemView
import com.godlife.designsystem.theme.CheckColor
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.godlife.designsystem.view.GodLifeLoadingScreen
import com.godlife.network.model.CommentBody
import com.godlife.network.model.PostDetailBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postId: String,
    navController: NavController,
    parentNavController: NavController,
    postDetailViewModel: PostDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = postId) {
        postDetailViewModel.initPostDetailInfo(postId = postId)
    }


    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)

    val cScope = rememberCoroutineScope()

    val context = LocalContext.current

    //Ui State 관찰
    val uiState by postDetailViewModel.uiState.collectAsState()

    Log.e("uiState", uiState.toString())

    val postDetail by postDetailViewModel.postDetail.collectAsState()
    val comments by postDetailViewModel.comments.collectAsState()

    val writeComment by postDetailViewModel.writeComment.collectAsState()

    val isShowDialog = remember {
        mutableStateOf(false)
    }

    if(uiState is PostDetailUiState.Loading ||
        uiState is PostDetailUiState.Success)
    {

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
                                .background(GrayWhite3)
                                .fillMaxSize()) {

                            if (postDetail.body?.imagesURL?.isNotEmpty() == true){
                                item{ ImageBox(imgUriList = postDetail.body?.imagesURL!!) }
                            }
                            else{
                                item{
                                    Box(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .height(30.dp)
                                            .background(color = Color.White)
                                    )
                                }
                            }

                            postDetail.body?.let {
                                Log.e("postDetail", it.toString())
                                item{
                                    Content(
                                        postDetailBody = it,
                                        parentNavController = parentNavController,
                                        viewModel = postDetailViewModel,
                                        isShowDialog = isShowDialog,
                                        owner = it.boardOwner
                                    )
                                }
                            }

                            postDetail.body?.let {

                                item{
                                    Content2(
                                        memberLikedBoard = it.memberLikedBoard,
                                        owner = it.boardOwner,
                                        viewModel = postDetailViewModel
                                    )
                                }

                            }

                            item {
                                Comments(
                                    comments = comments,
                                    snackbarHostState = snackBarHostState,
                                    cScope = cScope,
                                    parentNavController = parentNavController,
                                    postDetailViewModel =  postDetailViewModel
                                )
                            }


                        }

                    }

                    GodLifeCreateCommentBar(
                        comment = writeComment,
                        onTextChanged = { postDetailViewModel.onWriteCommentChange(it) },
                        onPostClicked = { postDetailViewModel.createComment() },
                        context = context
                    )


                }



            }

            if(isShowDialog.value){

                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { isShowDialog.value = !isShowDialog.value },
                    title = {
                        Text(text = "해당 게시물을 삭제하시겠어요?", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    },
                    text = {
                        Text(text = "사용자님의 굿생 인증 게시물을 삭제하시면 굿생 점수 2점이 차감됩니다.", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
                    },
                    confirmButton = {
                        GodLifeButtonWhite(
                            onClick = {
                                postDetailViewModel.deletePost()
                            },
                            text = { Text(text = "삭제하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    },
                    dismissButton = {
                        GodLifeButtonWhite(
                            onClick = { isShowDialog.value= !isShowDialog.value },
                            text = { Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                )
            }

            if(uiState is PostDetailUiState.Loading){

                when((uiState as PostDetailUiState.Loading).type) {

                    LoadingType.POST ->
                        GodLifeLoadingScreen(
                            text = "게시물을 불러오고 있어요.\n잠시만 기다려주세요."
                        )

                    LoadingType.DELETE ->
                        GodLifeLoadingScreen(
                            text = "게시물을 삭제하고 있어요."
                        )
                }

            }

        }

    }

    else if(uiState is PostDetailUiState.DeleteSuccess){
        DeleteSuccessScreen(navController = navController)
    }

    else if(uiState is PostDetailUiState.Error){
        GodLifeErrorScreen(
            errorMessage = (uiState as PostDetailUiState.Error).message,
            buttonEnabled = false
        )
    }

    else if(uiState is PostDetailUiState.Update){
        UpdatePostScreen(
            postDetail  = postDetail.body!!,
            postDetailViewModel = postDetailViewModel,
            postDetailContext = context
        )
    }


}

@Composable
fun ImageBox(
    modifier: Modifier = Modifier,
    imgUriList: List<String>
){
    val imgCount  = imgUriList.size
    var imgIndex by remember { mutableIntStateOf(1) }

    var width by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current

    val initialPage by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { imgUriList.size })

    Box(
        modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){

        /*

        LazyRow {
            itemsIndexed(imgUriList) {index, item ->
                ImageView(modifier, LocalContext.current, item)
                imgIndex = index+1
            }
        }
         */

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    width = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
        ) {index ->

            imgIndex = index

            imgUriList.getOrNull(
                index%(imgUriList.size)
            )?.let{
                item ->
                ImageView(imgUri = item, width = width)

            }

        }

        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                val size = if (pagerState.currentPage == iteration) 10.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(size)
                )
            }
        }


    }
}

@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    imgUri: String,
    width: Dp
){

    GlideImage(
        imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + imgUri },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center
        ),
        modifier = modifier
            .size(width = width, height = 400.dp),
        loading = {
            Box(
                modifier = modifier
                    .background(GrayWhite3)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){

                CircularProgressIndicator(
                    color = PurpleMain
                )

            }

        },
        failure = {
            Image(
                painter = painterResource(id = R.drawable.category3),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    postDetailBody: PostDetailBody,
    parentNavController: NavController,
    viewModel: PostDetailViewModel,
    owner: Boolean,
    isShowDialog: MutableState<Boolean>
){

    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically){

            //프로필 이미지 부분
            GlideImage(
                imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + postDetailBody.profileURL },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = modifier
                    .size(50.dp, 50.dp)
                    .clip(CircleShape)
                    .fillMaxSize()
                    .background(color = GrayWhite2)
                    .clickable { parentNavController.navigate("${"ProfileScreen"}/${postDetailBody.writerId}") },
                loading = {
                    Box(
                        modifier = modifier
                            .background(GrayWhite3)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){

                        CircularProgressIndicator(
                            color = PurpleMain
                        )

                    }

                },
                failure = {
                    Image(
                        painter = painterResource(id = R.drawable.category3),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
            )

            Spacer(modifier.size(10.dp))

            Column(
                modifier = modifier
                    .weight(0.8f)
            ) {
                Text(
                    text = postDetailBody.nickname,
                    style = TextStyle(
                        color = GrayWhite,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Text(
                    text = postDetailBody.whoAmI,
                    style = TextStyle(
                        color = GrayWhite,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Box(
                modifier = modifier
                    .weight(0.2f),
                contentAlignment = Alignment.CenterEnd
            ){

                IconButton(
                    onClick = { expanded.value = !expanded.value }
                ) {

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Content Menu",
                        tint = GrayWhite
                    )

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(Color.White)
                    ) {

                        when(postDetailBody.boardOwner){
                            true ->
                                ContentDropDownBoardOwnerItem(
                                    expanded = expanded,
                                    isShowDialog = isShowDialog,
                                    viewModel = viewModel
                                )
                            false ->
                                ContentDropDownNotBoardOwnerItem(
                                    postDetailViewModel= viewModel,
                                    parentNavController = parentNavController,
                                    expanded = expanded
                                )
                        }

                    }
                    
                }



            }


        }

        //Spacer(modifier.size(20.dp))

        Text(text = postDetailBody.title, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(
            text = postDetailBody.body,
            style = TextStyle(
                color = GrayWhite,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        )

        Spacer(modifier.size(20.dp))

        FlowRow {
            postDetailBody.tags.forEach {
                TagItemView(tagItem = it)
            }
        }

        Spacer(modifier.size(20.dp))

        HorizontalDivider()

        Spacer(modifier.size(20.dp))

        Row(
            modifier = modifier
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = modifier
                    .size(20.dp),
                painter = painterResource(id = R.drawable.schedule_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                tint = GrayWhite
            )

            Spacer(modifier.width(2.dp))

            Text(
                text = postDetailBody.writtenAt,
                style = TextStyle(
                    color = GrayWhite,
                    fontWeight = FontWeight.Normal, fontSize = 15.sp
                )
            )

        }


        Spacer(modifier.size(20.dp))

        Row(
            modifier = modifier
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = modifier
                    .size(20.dp),
                painter = painterResource(id = R.drawable.visibility_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                tint = GrayWhite
            )

            Spacer(modifier.width(2.dp))

            Text(
                text = "${postDetailBody.views}",
                style = TextStyle(
                    color = GrayWhite,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier.width(10.dp))

            Icon(
                modifier = modifier
                    .size(20.dp),
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "",
                tint = GrayWhite
            )

            Spacer(modifier.width(2.dp))

            Text(
                text = "${postDetailBody.godScore-2}",
                style = TextStyle(
                    color = GrayWhite,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
            )

        }

        Spacer(modifier.size(20.dp))

        //RowButton2()

    }
}

@Composable
fun Content2(
    modifier: Modifier = Modifier,
    memberLikedBoard: Boolean = true,
    owner: Boolean,
    viewModel: PostDetailViewModel
){
    if(!owner){

        Column(
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {

            if(!memberLikedBoard){
                Text(
                    text = "작성자님의 게시물을 읽어보셨나요?\n굿생을 인정하신다면, 아래 버튼을 눌러주세요!",
                    style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal)
                )

                Spacer(modifier.size(20.dp))

                GodLifeButtonWhite(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally),
                    leadingIcon = {Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "")},
                    onClick = { viewModel.agreeGodLife() },
                    text = { Text(text = "굿생 인정!") }
                )
            }

            else{
                Icon(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = "",
                    tint = PurpleMain
                )

                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = "유저님께서 굿생을 인정하신 글이에요!",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
            }


        }

    }

}



@Composable
fun Comments(
    modifier: Modifier = Modifier,
    comments: List<CommentBody>,
    snackbarHostState: SnackbarHostState,
    cScope: CoroutineScope,
    parentNavController: NavController,
    postDetailViewModel: PostDetailViewModel
){

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = GrayWhite3)
            .padding(10.dp)
    ) {
        Text(text = "댓글 ${comments.size}개", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier = modifier.size(10.dp))

        if(comments.isNotEmpty()){

            comments.forEach {

                CommentBox(commentBody = it, snackbarHostState = snackbarHostState, cScope = cScope, parentNavController = parentNavController, postDetailViewModel =  postDetailViewModel)

            }

        }



    }
}

@Composable
fun CommentBox(
    modifier: Modifier = Modifier,
    commentBody: CommentBody,
    snackbarHostState: SnackbarHostState,
    cScope: CoroutineScope,
    parentNavController: NavController,
    postDetailViewModel: PostDetailViewModel){

    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(10.dp)
    ){

        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){

            Row(modifier.weight(0.7f),
                verticalAlignment = Alignment.CenterVertically){

                //프로필 이미지 부분
                GlideImage(
                    imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + commentBody.profileURL },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .size(40.dp, 40.dp)
                        .clip(CircleShape)
                        .fillMaxSize()
                        .background(color = GrayWhite2),
                    loading = {
                        Box(
                            modifier = modifier
                                .background(GrayWhite3)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){

                            CircularProgressIndicator(
                                color = PurpleMain
                            )

                        }

                    },
                    failure = {
                        Image(
                            painter = painterResource(id = R.drawable.category3),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                )

                Spacer(modifier.size(10.dp))

                Text(
                    text = commentBody.nickname,
                    style = TextStyle(
                        color = GrayWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier.size(10.dp))


            }
            Box(modifier.weight(0.2f), contentAlignment = Alignment.TopCenter){
                Text(text = commentBody.writtenAt, style = TextStyle(color = GrayWhite, fontSize = 15.sp))
            }

            IconButton(modifier = modifier.weight(0.1f), onClick = { expanded.value = !expanded.value} ){
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Comment Menu", tint = GrayWhite)

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.background(Color.White)
                ) {

                    if(commentBody.commentOwner) CommentDropDownDeleteItem(snackbarHostState = snackbarHostState, cScope = cScope, postDetailViewModel =  postDetailViewModel, commentBody = commentBody, expanded = expanded)
                    else CommentDropDownDeclareItem(parentNavController = parentNavController, commentBody = commentBody, expanded = expanded)

                }
            }



        }

        Spacer(modifier.size(10.dp))

        Text(text = commentBody.comment, style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))


    }

}

@Composable
fun DeleteSuccessScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = modifier
                .size(40.dp),
            imageVector = Icons.Outlined.Delete,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "게시물 삭제가 완료되었어요.",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = {
                navController.popBackStack()
                      },
            text = { Text(text = "돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )

    }
}

@Composable
fun ContentDropDownNotBoardOwnerItem(
    modifier: Modifier = Modifier,
    postDetailViewModel: PostDetailViewModel,
    parentNavController: NavController,
    expanded: MutableState<Boolean>
){
    val postId = postDetailViewModel.postId.collectAsState().value
    val postDetail by postDetailViewModel.postDetail.collectAsState()
    val writerNickname = postDetail.body?.nickname
    val writerId = postDetail.body?.writerId
    val category = "board"
    val comment = "no"

    DropdownMenuItem(
        text = { Text(text = "신고하기", style = TextStyle(color = GrayWhite)) },
        onClick = {
            expanded.value = !expanded.value
            parentNavController.navigate("${"ReportScreen"}/${postId}/${writerNickname}/${writerId}/${category}/${comment}"){
                launchSingleTop = true
            }

        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = "신고하기", tint = GrayWhite)
        },
        colors = MenuDefaults.itemColors(Color.White)
    )
}

@Composable
fun ContentDropDownBoardOwnerItem(
    expanded: MutableState<Boolean>,
    isShowDialog: MutableState<Boolean>,
    viewModel: PostDetailViewModel
){

    Column {

        DropdownMenuItem(
            text = { Text(text = "수정하기", style = TextStyle(color = GrayWhite)) },
            onClick = {
                expanded.value = !expanded.value
                viewModel.updateUIState(PostDetailUiState.Update)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "수정하기", tint = GrayWhite)
            },
            colors = MenuDefaults.itemColors(Color.White)
        )

        DropdownMenuItem(
            text = { Text(text = "삭제하기", style = TextStyle(color = GrayWhite)) },
            onClick = {
                expanded.value = !expanded.value
                isShowDialog.value = !isShowDialog.value
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "삭제하기", tint = GrayWhite)
            },
            colors = MenuDefaults.itemColors(Color.White)
        )

    }


}



@Composable
fun CommentDropDownDeclareItem(
    parentNavController: NavController,
    commentBody: CommentBody,
    expanded: MutableState<Boolean>
){

    val postId = commentBody.comment_id
    val writerNickname = commentBody.nickname
    val writerId = commentBody.writer_id
    val category = "comment"
    val comment = if(commentBody.comment.length>15) commentBody.comment.substring(0,15) + "..." else commentBody.comment

    DropdownMenuItem(
        text = { Text(text = "신고하기", style = TextStyle(color = GrayWhite)) },
        onClick = {
            expanded.value = !expanded.value

            expanded.value = !expanded.value
            parentNavController.navigate("${"ReportScreen"}/${postId}/${writerNickname}/${writerId}/${category}/${comment}"){
                launchSingleTop = true
            }

        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = "신고하기", tint = GrayWhite)
        },
        colors = MenuDefaults.itemColors(Color.White)
    )
}

@Composable
fun CommentDropDownDeleteItem(
    snackbarHostState: SnackbarHostState,
    cScope: CoroutineScope,
    postDetailViewModel: PostDetailViewModel,
    commentBody: CommentBody,
    expanded: MutableState<Boolean>
){

    DropdownMenuItem(
        text = { Text(text = "삭제하기", style = TextStyle(color = GrayWhite)) },
        onClick = {
            expanded.value = !expanded.value
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
                GodLifeCreateCommentBarPreview()
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

@OptIn(ExperimentalLayoutApi::class)
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
                    .size(70.dp)
            )

            Spacer(modifier.size(10.dp))

            Column(
                modifier = modifier
                    .weight(0.8f)
            ) {
                Text(text = "Nickname", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Text(text = "Introduce", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 12.sp))
            }

            Box(
                modifier = modifier
                    .weight(0.2f),
                contentAlignment = Alignment.CenterEnd
            ){

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = GrayWhite
                )

            }
        }

        Spacer(modifier.size(20.dp))

        Text(text = "Title", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp))

        Spacer(modifier.size(20.dp))

        Text(text = "text", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        FlowRow {
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
            TagItemPreview()
        }

        Spacer(modifier.size(20.dp))

        Text(text = "yyyy-mm-dd(게시물 올린 날짜,시간)", style = TextStyle(color = GrayWhite, fontWeight = FontWeight.Normal, fontSize = 15.sp))

        Spacer(modifier.size(20.dp))

        //RowButton2()

    }
}

@Preview(showBackground = true)
@Composable
fun Content2Preview(
    modifier: Modifier = Modifier,
    memberLikedBoard: Boolean = true
){
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(color = GrayWhite3, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)
    ) {

        if(!memberLikedBoard){
            Text(
                text = "작성자님의 게시물을 읽어보셨나요?\n굿생을 인정하신다면, 아래 버튼을 눌러주세요!",
                style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal)
            )

            Spacer(modifier.size(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                leadingIcon = {Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "")},
                onClick = { "postDetailViewModel.agreeGodLife()" },
                text = { Text(text = "굿생 인정!") }
            )
        }

        else{
            Icon(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "",
                tint = PurpleMain
            )

            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = "유저님께서 굿생을 인정하신 글이에요!",
                style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Normal),
                textAlign = TextAlign.Center
            )
        }




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

    Box(
        modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "#${text}",
            style = TextStyle(color = Color.Black),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun DeleteSuccessScreenPreview(
    modifier: Modifier = Modifier
){

    // 애니메이션을 위한 상태 변수
    var animationPlayed by remember { mutableStateOf(false) }
    val score by animateIntAsState(
        targetValue = if (animationPlayed) 500 else 502,
        animationSpec = tween(
            durationMillis = 1000, // 애니메이션 지속 시간 (2초)
            easing = LinearEasing
        )
    )

    // 컴포지션이 완료되면 애니메이션 시작
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = modifier
                .size(40.dp),
            imageVector = Icons.Outlined.Delete,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "게시물 삭제가 완료되었어요.",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { /*TODO*/ },
            text = { Text(text = "돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )

    }
}
