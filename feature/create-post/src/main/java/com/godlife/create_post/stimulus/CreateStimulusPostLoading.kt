package com.godlife.create_post.stimulus

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Slide
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.theme.OpaqueLight
import kotlinx.coroutines.delay

@Composable
fun CreateStimulusPostLoading(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CreateStimulusPostViewModel
){

    val coverImg = viewModel.coverImg.collectAsState()
    val title = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()

    val context = LocalContext.current
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    val coverVisible = remember { mutableStateOf(false) }
    val coverTextVisible = remember { mutableStateOf(false) }
    val guideVisible = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        delay(1000L)
        coverVisible.value = true

        delay(1000L)
        coverTextVisible.value = true

        delay(1000L)
        guideVisible.value = true

        delay(5000L)
        navController.navigate(CreateStimulusPostContent.route)
    }


    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Glide.with(context)
            .asBitmap()
            .load(coverImg.value)
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
                modifier = modifier
                    .fillMaxSize()
                    .blur(
                        radiusX = 15.dp, radiusY = 15.dp
                    )
            )
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item{
                AnimatedVisibility(
                    visible = coverVisible.value ,
                    enter = fadeIn(initialAlpha = 0.4f)
                ) {
                    StimulusCoverItem(title = title.value, coverImg = bitmap.value)
                }
            }

            item {
                AnimatedVisibility(
                    visible = coverTextVisible.value ,
                    enter = fadeIn(initialAlpha = 0.4f)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        Spacer(modifier.size(5.dp))

                        Text(
                            text = description.value,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        )



                        Spacer(modifier.size(5.dp))

                        HorizontalDivider()

                        Spacer(modifier.size(5.dp))

                        /* User 이름 들어갈 부분

                        Text(
                            text = "by.${userName}" /* Todo */,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        )

                         */

                        Spacer(modifier.size(50.dp))

                    }



                }
            }


            item {
                AnimatedVisibility(
                    visible = guideVisible.value ,
                    enter = fadeIn(initialAlpha = 0.4f)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        Box(
                            modifier = modifier
                                .background(color = OpaqueLight, shape = RoundedCornerShape(18.dp))
                                .padding(10.dp)
                        ){

                            Text(
                                text = "작가님이 작성하실 글의 커버가 완성되었어요.\n" +
                                        "이제 작가님의 멋있는 글을 작성해주세요!",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start
                                )
                            )

                        }

                        Spacer(modifier.size(30.dp))

                        CircularProgressIndicator(color = Color.White)

                    }

                }
            }








        }

    }
}