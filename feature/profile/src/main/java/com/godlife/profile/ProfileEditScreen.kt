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
import com.godlife.designsystem.component.GodLifeTextField
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
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
    viewModel: ProfileEditViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val selectedImageType by viewModel.selectedImageType.collectAsState()

    val context = LocalContext.current

    Log.e("ProfileEditScreen", "uiState : ${uiState.toString()}")

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
                    ProfileEditBox(innerPadding = innerPadding, viewModel = viewModel, launcher = launcher)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditBox(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModel,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    innerPadding: PaddingValues = PaddingValues(10.dp),
    navController: NavController? = null
){

    LaunchedEffect(Unit) {
        
    }

    val profileChangeState by viewModel.profileChangeState.collectAsState()
    val backgroundChangeState by viewModel.backgroundChangeState.collectAsState()
    val introduceChangeState by viewModel.introduceChangeState.collectAsState()

    Log.e("ProfileEditBox", "profileChangeState : $profileChangeState")
    Log.e("ProfileEditBox", "backgroundChangeState : $backgroundChangeState")
    Log.e("ProfileEditBox", "introduceChangeState : $introduceChangeState")

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xCB000000))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "소개글 수정하기", style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        Spacer(modifier = modifier.size(20.dp))

        GodLifeTextField(text = text, onTextChanged = { text = it } )

        Spacer(modifier = modifier.size(20.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Box(modifier = modifier
                .weight(0.5f)
                .padding(start = 10.dp, end = 10.dp),
                contentAlignment = Alignment.Center
            ){
                GodLifeButtonWhite(
                    modifier = modifier
                        .width(130.dp),
                    onClick = {
                        viewModel.updateIntroduce(text)
                        viewModel.updateShowIntroduceChangeViewState()
                              },
                    text = {
                        Text(text = "작성 완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }
                )
            }

            Box(modifier = modifier
                .weight(0.5f)
                .padding(start = 10.dp, end = 10.dp),
                contentAlignment = Alignment.Center
            ){
                GodLifeButtonWhite(
                    modifier = modifier
                        .width(130.dp),
                    onClick = { viewModel.updateShowIntroduceChangeViewState() },
                    text = {
                        Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }
                )
            }
        }
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
    val text = "Hello World!"
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpaqueDark)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "소개글 수정하기", style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        Spacer(modifier = modifier.size(20.dp))

        GodLifeTextField(text = text, onTextChanged = { it -> text } )

        Spacer(modifier = modifier.size(20.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Box(modifier = modifier
                .weight(0.5f)
                .padding(start = 10.dp, end = 10.dp),
                contentAlignment = Alignment.Center
            ){
                GodLifeButtonWhite(
                    modifier = modifier
                        .width(130.dp),
                    onClick = { /*TODO*/ },
                    text = {
                        Text(text = "작성 완료", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }
                )
            }

            Box(modifier = modifier
                .weight(0.5f)
                .padding(start = 10.dp, end = 10.dp),
                contentAlignment = Alignment.Center
            ){
                GodLifeButtonWhite(
                    modifier = modifier
                        .width(130.dp),
                    onClick = { /*TODO*/ },
                    text = {
                        Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }
                )
            }
        }

    }
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