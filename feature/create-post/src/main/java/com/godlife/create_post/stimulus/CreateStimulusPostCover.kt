package com.godlife.create_post.stimulus

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
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
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.create_post.R
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.godlife.designsystem.view.GodLifeLoadingScreen
import com.godlife.network.BuildConfig
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun CreateStimulusPostCover(
    modifier: Modifier = Modifier,
    navController: NavController,
    cScope: CoroutineScope,
    viewModel: CreateStimulusPostViewModel
){
    //val cScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    Log.d("CreateStimulusPostCover", "Current uiState: ${uiState.value}")

    val coverImg = viewModel.coverImg.collectAsState()
    val title = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()

    val buttonEnabled: Boolean
    = if(coverImg==Uri.EMPTY || title.value == "" || description.value == "") false else true

    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            val resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {

                cScope.launch {
                    viewModel.setCoverImg(uri = resizeUri)
                }



            }
        }

    }

    if(uiState.value !is CreateStimulusUiState.Error){

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                item {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(GrayWhite)
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ){

                        if(coverImg!= Uri.EMPTY){

                            GlideImage(
                                imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + coverImg.value },
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                ),
                                modifier = modifier
                                    .fillMaxSize()
                                    .blur(
                                        radiusX = 15.dp, radiusY = 15.dp
                                    ),
                                loading = {
                                    Box(
                                        modifier = modifier
                                            .background(GrayWhite3)
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ){

                                        CircularProgressIndicator(
                                            color = OrangeMain
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

                        else{

                            Image(
                                modifier = modifier
                                    .fillMaxSize()
                                    .blur(
                                        radiusX = 15.dp, radiusY = 15.dp
                                    ),
                                painter = painterResource(id = R.drawable.category3),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                alpha = 0.7f
                            )

                        }


                        StimulusCoverItem(title = title.value, coverImg = coverImg.value)


                    }
                }

                item{
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        Spacer(modifier.size(10.dp))

                        GodLifeButtonWhite(
                            modifier = modifier
                                .fillMaxWidth(),
                            leadingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = "") },
                            onClick = {
                                launcher.launch("image/*")
                            },
                            text = { Text(text = "커버 이미지 선택", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                }

                item {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(text = "제목", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

                        Spacer(modifier.size(10.dp))

                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = title.value,
                                onTextChanged = { viewModel.setTitle(it) },
                                hint = "제목을 작성해주세요.",
                                singleLine = true
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${title.value.length}/15",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.End
                        )


                    }
                }

                item {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(text = "소개글", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

                        Spacer(modifier.size(10.dp))

                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = description.value,
                                onTextChanged = { viewModel.setDescription(it) },
                                hint = "굿생러분들이 관심을 가질 수 있도록 소개글을 작성해주세요.",
                                singleLine = false
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${description.value.length}/30",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.End
                        )


                    }


                }

                item {

                    Box(
                        modifier = modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){

                        GodLifeButton(
                            onClick = {
                                navController.navigate(
                                    route = CreateStimulusPostLoading.route){
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f),
                            enabled = buttonEnabled
                        ) {
                            Text(text = "다음",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                    }


                }

            }

            if(uiState.value is CreateStimulusUiState.Loading){
                Log.e("CreateStimulusPostCover", (uiState.value as CreateStimulusUiState.Loading).type.name)
                GodLifeLoadingScreen(
                    text = if((uiState.value as CreateStimulusUiState.Loading).type == UiType.SET_COVER_IMG) "잠시만 기다려주세요.\n커버를 제작하는 중이에요." else "잠시만 기다려주세요."
                )
            }

        }

    }

    else if(uiState.value is CreateStimulusUiState.Error){
        GodLifeErrorScreen(
            errorMessage = (uiState.value as CreateStimulusUiState.Error).message,
            buttonEvent = {navController.popBackStack()},
            buttonText = "돌아가기"
        )
    }




}

@Composable
fun StimulusCoverItem(
    modifier: Modifier = Modifier,
    title: String,
    coverImg: String
){
    Box(
        modifier = modifier
            .padding(10.dp)
            .size(width = 200.dp, height = 250.dp)
            .shadow(10.dp),
        contentAlignment = Alignment.Center
    ){

        GlideImage(
            imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + coverImg },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = modifier
                .fillMaxSize(),
            loading = {
                Box(
                    modifier = modifier
                        .background(GrayWhite3)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){

                    CircularProgressIndicator(
                        color = OrangeMain
                    )

                }

            },
            failure = {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(GrayWhite3)
                )
            }
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(30.dp)
                .background(color = OpaqueDark)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ){

            Text(text = title,
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

        }

    }
}


@Preview
@Composable
fun CreateStimulusPostCoverPreview(
    modifier: Modifier = Modifier,
    imageUri: Uri = Uri.EMPTY
){
    val context = LocalContext.current

    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }



    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ){

                if(imageUri!= Uri.EMPTY){

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
                            contentScale = ContentScale.FillWidth,
                            modifier = modifier
                                .fillMaxSize()
                                .blur(
                                    radiusX = 15.dp, radiusY = 15.dp
                                )
                        )
                    }
                }

                else{

                    Image(
                        modifier = modifier
                            .fillMaxSize()
                            .blur(
                                radiusX = 15.dp, radiusY = 15.dp
                            ),
                        painter = painterResource(id = R.drawable.category3),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        alpha = 0.7f
                    )

                }


                StimulusCoverItemPreview()


            }
        }

        item{
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "커버 이미지를 추가해주세요!", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal))

                Spacer(modifier.size(10.dp))

                GodLifeButtonWhite(
                    modifier = modifier
                        .fillMaxWidth(),
                    leadingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = "") },
                    onClick = { /*TODO*/ },
                    text = { Text(text = "사진 선택", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                )
            }
        }

        item {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "제목", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

                Spacer(modifier.size(10.dp))

                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
                ){

                    GodLifeTextFieldGray(
                        text = "",
                        onTextChanged = {},
                        hint = "이곳에 제목을 작성해주세요."
                    )

                }


            }
        }

        item {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "소개글", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

                Spacer(modifier.size(10.dp))

                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = GrayWhite3, shape = RoundedCornerShape(18.dp))
                ){

                    GodLifeTextFieldGray(
                        text = "",
                        onTextChanged = {},
                        hint = "소개글을 통해 사용자분들께서 관심을 가질 수 있도록 소개글을 작성해주세요."
                    )

                }


            }


        }

        item {

            Box(
                modifier = modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){

                GodLifeButton(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "다음",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }


        }

    }
}

@Preview
@Composable
fun StimulusCoverItemPreview(
    modifier: Modifier = Modifier,
    item: StimulusPostItem = StimulusPostItem(title = "이것이 제목이다", writer = "치킨 러버", coverImg = R.drawable.category3, introText = "")
){
    Box(
        modifier = modifier
            .padding(10.dp)
            .size(width = 200.dp, height = 250.dp)
            .shadow(10.dp),
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = modifier
                .fillMaxWidth(),
            painter = painterResource(id = item.coverImg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(30.dp)
                .background(color = OpaqueDark)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ){

            Text(text = item.title,
                style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

        }

    }
}



data class StimulusPostItem(
    val title: String,
    val writer: String,
    val coverImg: Int,
    val introText: String

)

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