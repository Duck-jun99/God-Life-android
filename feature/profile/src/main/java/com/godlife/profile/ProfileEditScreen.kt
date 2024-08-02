package com.godlife.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
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
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.profile.navigation.ProfileScreenRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileEditViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val selectedImageType by viewModel.selectedImageType.collectAsState()
    val userId by viewModel.userId.collectAsState()

    val context = LocalContext.current

    Log.e("ProfileEditScreen", "uiState : $uiState")
    val workList by viewModel.workList.collectAsState()
    Log.e("ProfileEditBox", "workList : $workList")

    //갤러리에서 사진 가져오기
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        Log.e("ProfileEditScreen", "uri : $uri")

        if (uri != null) {
            var resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {

                when(selectedImageType){
                    "profile" -> viewModel.updataProfileImage(resizeUri)
                    "background" -> viewModel.updateBackgroundImage(resizeUri)
                }

                val file = File(resizeUri.path)
                Log.e("이미지 사이즈", file.length().toString())
            }
        }

    }

    Box(

    ){

        if(uiState !is ProfileEditUiState.Error)
        {
            ProfileEditBox(
                viewModel = viewModel,
                launcher = launcher
            )
        }

        when(uiState){
            is ProfileEditUiState.Loading -> {
                ProfileEditLoadingScreen()
            }
            is ProfileEditUiState.Init -> {

            }
            is ProfileEditUiState.SendLoading -> {
                ProfileEditSendLoadingScreen()
            }
            is ProfileEditUiState.Success -> {

                ProfileEditSuccessScreen()

                LaunchedEffect(true) {
                    snackbarHostState.showSnackbar(message = (uiState as ProfileEditUiState.Success).data)
                    delay(1000L)

                    navController.popBackStack()

                    /*
                    navController.navigate("${ProfileScreenRoute.route}/${userId}"){
                        popUpTo(ProfileScreenRoute.route)
                        launchSingleTop = true
                    }

                     */

                }

            }
            is ProfileEditUiState.Error -> {
                ProfileEditErrorScreen(viewModel = viewModel, navController = navController)
            }
        }

    }

    /*

    GodLifeTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            when (val state = uiState) {

                is ProfileEditUiState.Loading -> {

                }

                is ProfileEditUiState.Init -> {
                    /* TODO */
                }

                is ProfileEditUiState.SendLoading -> {

                }

                is ProfileEditUiState.Success -> {
                    ProfileEditBox(viewModel = viewModel, launcher = launcher)
                }
                is ProfileEditUiState.Error -> {
                    /* TODO */
                }
                is ProfileEditUiState.Result -> {

                    SideEffect {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = state.message,
                                withDismissAction = true
                            )
                            delay(2000L)


                        }
                    }
                }
            }
        }
    }
    */
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditBox(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModel,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
){

    LaunchedEffect(Unit) {
        
    }

    val profileImg by viewModel.profileImage.collectAsState()
    val backgroundImg by viewModel.backgroundImage.collectAsState()
    val nickname by viewModel.nickname.collectAsState()
    val introduction by viewModel.introduce.collectAsState()

    val showIntroduceChangeViewState by viewModel.showIntroduceChangeViewState.collectAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    Log.e("ProfileEditBox", "${bottomSheetScaffoldState.bottomSheetState.currentValue}")

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        //배경 사진
        val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
        val imageModifier: Modifier = modifier
            .fillMaxSize()

        Glide.with(LocalContext.current)
            .asBitmap()
            .load(if(backgroundImg.toString() != "") backgroundImg else R.drawable.category3)
            .error(R.drawable.category3)
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
            )   //bitmap이 없다면
        } ?: Image(
            painter = painterResource(id = R.drawable.category3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )

        //배경 사진 필터
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OpaqueDark)
        )
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(10.dp)
                    .statusBarsPadding(),
                contentAlignment = Alignment.TopEnd
            ){

                //완료 버튼
                Icon(
                    modifier = modifier
                        .clickable {
                            viewModel.completeUpdate()
                        },
                    imageVector = Icons.Default.Check,
                    contentDescription = "Complete",
                    tint = Color.White
                )
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                //프로필 사진
                val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                val imageModifier: Modifier = modifier
                    .size(130.dp)
                    .clip(CircleShape)

                Glide.with(LocalContext.current)
                    .asBitmap()
                    .load(if(profileImg.toString() != "") profileImg else R.drawable.category4)
                    .error(R.drawable.category4)
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
                    )   //bitmap이 없다면
                } ?: Image(
                    painter = painterResource(id = R.drawable.category4),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )


                Spacer(modifier = modifier.size(20.dp))

                //닉네임
                Text(text = nickname,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.size(10.dp))

                //소개글
                Text(text = introduction,
                    style = TextStyle(
                        color = GrayWhite2,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.size(30.dp))

                Box(
                    modifier = modifier
                        .fillMaxSize()
                    , contentAlignment = Alignment.BottomCenter
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "",
                            tint = GrayWhite2
                        )

                        Spacer(modifier.size(5.dp))

                        Text(text = "프로필 설정 옵션",
                            style = TextStyle(
                                color = GrayWhite2,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Thin
                            ),
                            textAlign = TextAlign.Center
                        )

                    }
                }

            }


        }

        BottomSheetScaffold(
            modifier = modifier
                .fillMaxWidth(),
            sheetContainerColor = OpaqueDark,
            scaffoldState = bottomSheetScaffoldState,
            sheetShape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 20.dp,
                topEnd = 20.dp
            ),
            sheetContent = {  EditOptionBox(viewModel = viewModel, launcher = launcher, bottomSheetScaffoldState = bottomSheetScaffoldState) }
        ) {

        }

        //소개글 수정 뷰
        if(showIntroduceChangeViewState){
            EditIntroduceBox(viewModel = viewModel)

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOptionBox(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModel,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    bottomSheetScaffoldState: BottomSheetScaffoldState
){
    val cScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {

        //프로필 사진 변경 옵션
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable {
                    cScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
                    viewModel.updateImageType("profile")
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "프로필 사진 변경하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable {
                    cScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
                    viewModel.updateImageType("background")
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "배경 사진 변경하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable {
                    cScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }

                    viewModel.updateShowIntroduceChangeViewState()

                },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "소개글 수정하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable {
                    viewModel.initUpdate()
                },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "초기화하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

    }

}

@Composable
fun EditIntroduceBox(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModel
){
    val introduce by viewModel.introduce.collectAsState()

    var text by remember { mutableStateOf(introduce) }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { viewModel.updateShowIntroduceChangeViewState() },
        title = {
            Text(text = "소개글 수정", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            Box(
                modifier
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .padding(5.dp)
            ){
                GodLifeTextFieldGray(
                    text = text,
                    hint = "소개글을 입력해주세요.",
                    onTextChanged = { text = it }
                )
            }

        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {
                    viewModel.updateIntroduce(text)
                    viewModel.updateShowIntroduceChangeViewState()
                          },
                text = { Text(text = "작성 완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
            )
        },
        dismissButton = {
            GodLifeButtonWhite(
                onClick = { viewModel.updateShowIntroduceChangeViewState() },
                text = { Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
            )
        }
    )

}

@Composable
fun ProfileEditErrorScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModel,
    navController: NavController
){

    val uiState by viewModel.uiState.collectAsState()

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
            text = "에러 메시지: ${(uiState as ProfileEditUiState.Error).message}",
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { navController.popBackStack() },
            text = { Text(text = "메인으로 돌아가기", style = TextStyle(color = PurpleMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )



    }
}

private fun convertResizeImage(imageUri: Uri, context: Context):Uri? {

    val bitmap: Bitmap

    if (Build.VERSION.SDK_INT >= 29) {

        val source: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, imageUri)

        try {
            bitmap = ImageDecoder.decodeBitmap(source)

            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

            val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()

            Log.e("convertResizeImage", "tempFile : $tempFile")
            Log.e("convertResizeImage", "Uri.fromFile(tempFile) : ${Uri.fromFile(tempFile)}")

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileEditBoxPreview(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(10.dp),
    previewMode: Boolean = true,
){
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        //배경 사진
        if(!previewMode){

            val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
            val imageModifier: Modifier = modifier
                .fillMaxSize()

            Glide.with(LocalContext.current)
                .asBitmap()
                .load(R.drawable.category3)
                .error(R.drawable.category3)
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
                )   //bitmap이 없다면
            } ?: Image(
                painter = painterResource(id = R.drawable.category3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        }

        else{
            Image(
                modifier = modifier
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.category3),
                contentDescription = "background",
                contentScale = ContentScale.Crop
            )
        }

        //배경 사진 필터
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OpaqueDark)
        )
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(10.dp)
                    .statusBarsPadding(),
                contentAlignment = Alignment.TopEnd
            ){

                //완료 버튼
                Icon(
                    modifier = modifier
                        .clickable {  },
                    imageVector = Icons.Default.Check,
                    contentDescription = "Complete",
                    tint = Color.White
                )
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                //프로필 사진
                if(!previewMode){

                    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                    val imageModifier: Modifier = modifier
                        .size(130.dp)
                        .clip(CircleShape)

                    Glide.with(LocalContext.current)
                        .asBitmap()
                        .load(R.drawable.category4)
                        .error(R.drawable.category4)
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
                        )   //bitmap이 없다면
                    } ?: Image(
                        painter = painterResource(id = R.drawable.category4),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier
                    )
                }

                else{
                    Image(
                        modifier = modifier
                            .size(130.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.category4),
                        contentDescription = "background",
                        contentScale = ContentScale.Crop
                    )
                }


                Spacer(modifier = modifier.size(20.dp))

                //닉네임
                Text(text = "닉네임",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.size(10.dp))

                //소개글
                Text(text = "안녕하세요! 갓생을 꿈꾸는 유저입니다.",
                    style = TextStyle(
                        color = GrayWhite2,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.size(30.dp))

                Box(
                    modifier = modifier
                        .fillMaxSize()
                    , contentAlignment = Alignment.BottomCenter
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "",
                            tint = GrayWhite2
                        )

                        Spacer(modifier.size(5.dp))

                        Text(text = "프로필 설정 옵션",
                            style = TextStyle(
                                color = GrayWhite2,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Thin
                            ),
                            textAlign = TextAlign.Center
                        )

                    }
                }

            }


        }

        BottomSheetScaffold(
            modifier = modifier
                .fillMaxWidth(),
            sheetContainerColor = OpaqueDark,
            sheetShape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 20.dp,
                topEnd = 20.dp
            ),
            sheetContent = {  EditOptionBoxPreview() }
        ) {

        }

    }
}

@Preview
@Composable
fun EditOptionBoxPreview(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {

        //프로필 사진 변경 옵션
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable { },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "프로필 사진 변경하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable { },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "배경 사진 변경하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable { },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "소개글 수정하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable { },
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = "초기화하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

    }

}

@Preview
@Composable
fun EditIntroduceBoxPreview(modifier: Modifier = Modifier){
    val text = ""

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {  },
        title = {
            Text(text = "소개글 수정", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
        },
        text = {
            Box(
                modifier
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .padding(5.dp)
            ){
                GodLifeTextFieldGray(
                    text = text,
                    hint = "소개글을 입력해주세요.",
                    onTextChanged = {  }
                )
            }

        },
        confirmButton = {
            GodLifeButtonWhite(
                onClick = {
                },
                text = { Text(text = "작성 완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
            )
        },
        dismissButton = {
            GodLifeButtonWhite(
                onClick = {  },
                text = { Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
            )
        }
    )
}

@Preview
@Composable
fun ProfileEditLoadingScreen(
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
fun ProfileEditSendLoadingScreen(
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
        Text(text = "변경사항을 적용중이에요.\n잠시만 기다려주세요!", style = TextStyle(color = Color.White), textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun ProfileEditSuccessScreen(
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
        Text(text = "프로필 변경이 완료되었어요!\n잠시뒤 이전 화면으로 이동합니다.", style = TextStyle(color = Color.White), textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun ProfileEditErrorScreenPreview(
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
