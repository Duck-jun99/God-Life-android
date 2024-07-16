package com.godlife.create_post.post

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.TagItem
import com.godlife.navigator.MainNavigator
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePostScreen(
    viewModel: CreatePostViewModel,
    createPostActivity: CreatePostActivity,
    navController: NavController,
    mainNavigator: MainNavigator,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    Log.e("CreatePostScreen", uiState.toString())

    viewModel.getUserInfo()

    val context = LocalContext.current

    val selectedImgList by viewModel.selectedImgUri.collectAsState()

    val title by remember { viewModel.title }
    val text by remember { viewModel.text }

    var isDialogVisble by remember { mutableStateOf(false) }


    // 갤러리에서 사진 가져오기
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            var resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {
                viewModel.saveImg(resizeUri)

                val file = File(resizeUri.path)
                Log.e("이미지 사이즈", file.length().toString())
            }
        }

    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ){

        if(uiState == CreatePostUiState.Loading ||
            uiState == CreatePostUiState.Init ||
            uiState == CreatePostUiState.SendLoading){

            Column(
                modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .statusBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "굿생 인증하기", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }

                LazyColumn(
                    modifier.padding(20.dp)
                ) {

                    item{
                        Text(text = "제목", style = GodLifeTypography.titleMedium)

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }


                    item{

                        Box(
                            modifier
                                .padding(10.dp)
                                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = title,
                                onTextChanged = { viewModel.updateTitle(it) },
                                hint = "제목을 입력해주세요.",
                                singleLine = true
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${title.length}/30",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.End
                        )


                        Spacer(modifier.padding(10.dp))
                    }

                    item{
                        Text(text = "내용", style = GodLifeTypography.titleMedium)

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }

                    item{

                        Box(
                            modifier
                                .padding(10.dp)
                                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                                .heightIn(min = 200.dp)
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = text,
                                onTextChanged = { viewModel.updateText(it) },
                                hint = "내용을 입력해주세요.\n달성한 투두리스트에 대해 설명해주시면 좋아요.",
                                singleLine = false
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${text.length}/1000",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.End
                        )


                        Spacer(modifier.padding(10.dp))
                    }


                    item{
                        Text(text = "투두 목록", style = GodLifeTypography.titleMedium)

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }


                    item{
                        FlowRow {
                            TagItemPreview()
                            TagItemPreview()
                            TagItemPreview()
                            TagItemPreview()
                            TagItemPreview()
                        }
                    }

                    item{ Spacer(modifier.padding(10.dp)) }

                    //item{ Text(text = "이미지", style = GodLifeTypography.titleMedium) }

                    item{

                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(text = "이미지", style = GodLifeTypography.titleMedium)
                            }

                            Spacer(modifier = Modifier.size(20.dp))

                            AddButton(
                                onClick = { launcher.launch("image/*") }
                            )
                        }
                    }

                    item{
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }

                    item{
                        LazyRow {
                            selectedImgList?.let {
                                itemsIndexed(it){ index, item ->
                                    Log.e("fbjkkjhsad",index.toString())
                                    SelectImage(index, item, LocalContext.current, viewModel)
                                }
                            }

                        }
                    }

                    item{ Spacer(modifier.padding(10.dp)) }

                    item{ Text(text = "- 첫 번째 이미지는 대표 이미지로 설정돼요.", style = GodLifeTypography.bodySmall) }
                    item{ Text(text = "- 이미지는 최대 5장까지 올릴 수 있어요.", style = GodLifeTypography.bodySmall) }

                    item{ Spacer(modifier.padding(10.dp)) }

                    item{ Row(
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
                                    .clickable {
                                        navController.navigate("CreatePostPreviewScreen") {
                                            launchSingleTop = true
                                        }
                                    },
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(7.dp),
                                colors = CardDefaults.cardColors(Color.White)
                            ) {
                                Text(
                                    text = "미리 보기",
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
                                    .fillMaxWidth()
                                    .clickable {
                                        if( title == "" || text == "") Toast.makeText(context, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                                        else isDialogVisble = !isDialogVisble
                                    },
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
                    } }

                }


            }

            if(isDialogVisble){
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { isDialogVisble = !isDialogVisble },
                    title = {
                        Text(text = "게시물을 게시할까요?", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    },
                    text = {
                        Text(text = "아래의 내용을 꼭 확인해주세요!\n\n" +
                                "  굿생 점수 2점을 획득하실 수 있어요.\n" +
                                "  추후 해당 게시물 삭제 시 1점이 차감됩니다.\n" +
                                "  해당 게시물은 굿생 서비스를 이용하는 누구나 볼 수 있어요.\n" +
                                "  모니터링 또는 신고를 당하실 경우 서비스 이용에 제한이 있을 수 있어요.\n\n" +
                                "위 내용을 모두 확인하셨으면 '게시하기' 버튼을 눌러주세요."
                                ,
                            style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
                    },
                    confirmButton = {
                        GodLifeButtonWhite(
                            onClick = {

                                viewModel.createPost()

                                isDialogVisble = !isDialogVisble

                            },
                            text = { Text(text = "게시하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    },
                    dismissButton = {
                        GodLifeButtonWhite(
                            onClick = { isDialogVisble = !isDialogVisble },
                            text = { Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                )
            }

        }

        

        when(uiState){
            is CreatePostUiState.Loading -> {
                CreatePostLoadingScreen()
            }
            is CreatePostUiState.Init -> {

            }
            is CreatePostUiState.SendLoading -> {
                CreatePostSendLoadingScreen()
            }
            is CreatePostUiState.Success -> {

                CreatePostSuccessScreen(
                    createPostActivity = createPostActivity,
                    viewModel = viewModel,
                    mainNavigator = mainNavigator
                )
            }
            is CreatePostUiState.Error -> {
                CreatePostErrorScreen(
                    createPostActivity = createPostActivity,
                    viewModel = viewModel,
                    mainNavigator = mainNavigator
                )
            }

        }

    }


}

@Composable
fun TagItem(tagItem: TagItem, modifier: Modifier = Modifier){
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
            Text(text = tagItem.tagName,
                style = TextStyle(color = Color.White),
                textAlign = TextAlign.Center,
            )
        }
    }

}

@Composable
fun SelectImage(
    index:Int,
    imageUri: Uri,
    context: Context,
    viewModel: CreatePostViewModel
){
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    Box(modifier = Modifier
        .padding(end = 10.dp)
        .size(150.dp)
        .border(width = 5.dp, color = if (index == 0) Color.Yellow else Color.White)
    ) {

        //Image 부분
        val imageModifier: Modifier = Modifier
            .size(150.dp, 150.dp)
            .fillMaxSize()

        Glide.with(context)
            .asBitmap()
            .load(imageUri)
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
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        }

        Box(
            modifier = Modifier
                .padding(7.dp)
                .size(30.dp)
                .background(color = OpaqueDark, shape = CircleShape)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier
                    .clickable { viewModel.removeImg(imageUri) },
                imageVector = Icons.Default.Clear,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CreatePostSuccessScreen(
    modifier: Modifier = Modifier,
    viewModel: CreatePostViewModel,
    createPostActivity: CreatePostActivity,
    mainNavigator: MainNavigator
){
    val userInfo = viewModel.userInfo.collectAsState()
    val godLifeScore = userInfo.value!!.godLifeScore

    // 애니메이션을 위한 상태 변수
    var animationPlayed by remember { mutableStateOf(false) }
    val score by animateIntAsState(
        targetValue = if (animationPlayed) godLifeScore+2 else 0,
        animationSpec = tween(
            durationMillis = 2000, // 애니메이션 지속 시간 (2초)
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
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "${score}점",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "굿생 인증이 완료되었어요.\n오늘도 고생하셨어요!",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { mainNavigator.navigateFrom(activity = createPostActivity, withFinish = true) },
            text = { Text(text = "메인으로 돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )



    }
}

@Composable
fun CreatePostErrorScreen(
    modifier: Modifier = Modifier,
    viewModel: CreatePostViewModel,
    createPostActivity: CreatePostActivity,
    mainNavigator: MainNavigator
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
            imageVector = Icons.Outlined.Warning,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "오류가 발생했어요.\n잠시 후 다시 시도해주세요.",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "에러 메시지: ",
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { mainNavigator.navigateFrom(activity = createPostActivity, withFinish = true) },
            text = { Text(text = "메인으로 돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )



    }
}

fun convertResizeImage(imageUri: Uri, context: Context):Uri? {

    val bitmap: Bitmap

    if (Build.VERSION.SDK_INT >= 29) {

        val source: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, imageUri!!)

        try {
            bitmap = ImageDecoder.decodeBitmap(source)

            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

            val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()

            return Uri.fromFile(tempFile)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    } else {

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            //val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

            val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()

            return Uri.fromFile(tempFile)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return null
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun CreatePostScreenPreview(modifier: Modifier = Modifier){

    var title by remember { mutableStateOf("제목을 입력해주세요.") }
    var text by remember { mutableStateOf("내용을 입력해주세요.") }

    GodLifeTheme {
        Column(
            modifier
                .fillMaxSize()
                .background(Color.White)) {

            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "게시물 작성", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }

            LazyColumn(
                modifier.padding(20.dp)
            ) {

                item{ Text(text = "제목", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }


                item{
                    GodLifeTextFieldGray(
                        text = title,
                        onTextChanged = { title = it },
                        hint = "제목을 입력해주세요."
                    )
                }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "내용", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }


                item{ GodLifeTextFieldGray(text = text, onTextChanged = { text = it }) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "투두 목록", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }

                item{
                    FlowRow {
                        TagItemPreview()
                        TagItemPreview()
                        TagItemPreview()
                        TagItemPreview()
                        TagItemPreview()
                    }
                }

                item{ Spacer(modifier.padding(10.dp)) }

                //item{ Text(text = "이미지", style = GodLifeTypography.titleMedium) }
                
                item{

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Box(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(text = "이미지", style = GodLifeTypography.titleMedium)
                        }

                        Spacer(modifier = Modifier.size(20.dp))

                        AddButton(
                            onClick = { /* Todo */ }
                        )
                    }
                }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }

                item{ Box(
                    modifier
                        .background(GrayWhite)
                        .size(150.dp)) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "- 첫 번째 이미지는 대표 이미지로 설정돼요.", style = GodLifeTypography.bodySmall) }
                item{ Text(text = "* 이미지는 최대 5장까지 올릴 수 있어요.", style = GodLifeTypography.bodySmall) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Row(
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
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(7.dp),
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Text(
                                text = "미리 보기",
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
                } }
            }
        }
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

@Composable
fun AddButton(onClick: () -> Unit) {
    GodLifeButtonWhite(
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Add, // Choose an appropriate icon
                contentDescription = "Add icon",
                tint = PurpleMain,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {Text("추가하기", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold))}
    )
}

@Preview
@Composable
fun AddButtonPreview() {
    GodLifeButtonWhite(
        onClick = { /* TODO: Implement onClick */ },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Add, // Choose an appropriate icon
                contentDescription = "Add icon",
                tint = PurpleMain,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {Text("추가하기", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold))}
    )
}

@Preview
@Composable
fun SelectImagePreview(){
    Box(modifier = Modifier.padding(end = 10.dp)) {

        //Image 부분
        Box(modifier = Modifier
            .size(150.dp)
            .background(GrayWhite)
        ){

        }
        Box(modifier = Modifier
            .size(50.dp)
            .background(PurpleMain, shape = CircleShape)
        ){
            Text(text = "1", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) ,modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
fun RowButton(){
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
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Text(
                    text = "미리 보기",
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

@Preview
@Composable
fun CreatePostLoadingScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpaqueDark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)
        Spacer(modifier.size(10.dp))
        Text(text = "유저님의 정보를 받아오고 있어요.", style = TextStyle(color = Color.White), textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun CreatePostSendLoadingScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpaqueDark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)
        Spacer(modifier.size(10.dp))
        Text(text = "게시물을 등록하는 중이에요.\n잠시만 기다려주세요!", style = TextStyle(color = Color.White), textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun CreatePostErrorScreenPreview(
    modifier: Modifier = Modifier
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
            imageVector = Icons.Outlined.Warning,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "오류가 발생했어요.\n잠시 후 다시 시도해주세요.",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "에러 메시지: ",
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { /*TODO*/ },
            text = { Text(text = "메인으로 돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )



    }
}

@Preview
@Composable
fun CreatePostSuccessScreenPreview(
    modifier: Modifier = Modifier
){

    // 애니메이션을 위한 상태 변수
    var animationPlayed by remember { mutableStateOf(false) }
    val score by animateIntAsState(
        targetValue = if (animationPlayed) 502 else 500,
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
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription = "",
            tint = PurpleMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "${score}점",
            style = TextStyle(
                color = PurpleMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "굿생 인증이 완료되었어요.\n오늘도 고생하셨어요!",
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { /*TODO*/ },
            text = { Text(text = "메인으로 돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )



    }
}