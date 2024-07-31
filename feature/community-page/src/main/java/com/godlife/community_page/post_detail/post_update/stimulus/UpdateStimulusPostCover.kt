package com.godlife.community_page.post_detail.post_update.stimulus

import android.content.Context
import android.net.Uri
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.godlife.create_post.R
import com.godlife.create_post.post.convertResizeImage
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun UpdateStimulusPostCover(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UpdateStimulusPostViewModel,
    context: Context
){
    val uiState = viewModel.uiState.collectAsState()

    val coverImg = viewModel.coverImg.collectAsState()
    val title = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()

    val buttonEnabled: Boolean
            = if(coverImg== Uri.EMPTY || title.value == "" || description.value == "") false else true


    val cScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            val resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {

                cScope.launch {
                    viewModel.setCoverImg(
                        uri = resizeUri,
                        context,
                        false
                    )
                }


            }
        }

    }

    if(uiState.value !is UpdateStimulusUiState.Error){

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
                                imageModel = { coverImg.value },
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


                        UpdateStimulusCoverItem(
                            title = title.value,
                            coverImg = coverImg.value.toString()
                        )


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
                                navController.navigate(UpdateStimulusPostContentRoute.route)
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

            if(uiState.value is UpdateStimulusUiState.Loading){
                Log.e("UpdateStimulusUiState", (uiState.value as UpdateStimulusUiState.Loading).type.name)

                /*
                GodLifeLoadingScreen(
                    text = if((uiState.value as UpdateStimulusUiState.Loading).type == UiType.SET_COVER_IMG) "잠시만 기다려주세요.\n커버를 제작하는 중이에요." else "잠시만 기다려주세요."
                )

                 */
            }

        }

    }

    else if(uiState.value is UpdateStimulusUiState.Error){
        GodLifeErrorScreen(
            errorMessage = (uiState.value as UpdateStimulusUiState.Error).message,
            buttonEvent = {navController.popBackStack()},
            buttonText = "돌아가기"
        )
    }

}

@Composable
fun UpdateStimulusCoverItem(
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
            imageModel = { coverImg },
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
                        color = PurpleMain
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