package com.godlife.createtodolist.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.godlife.createtodolist.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedPreLoader(modifier: Modifier = Modifier) {
    //리소스 가져오기
    val preLoaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.progress1
        )
    )
    val isPlayLottie = remember { mutableStateOf(true) }

    //애니메이션 동작 설정
    val preLoaderProgress = animateLottieCompositionAsState(
        preLoaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlayLottie.value
    )

    val context = LocalContext.current

    /*
    LaunchedEffect(key1 = true) {
        delay(5000)
        isPlayLottie.value = false

        //val intent = Intent(context, MainActivity::class.java)
        //context.startActivity(intent)
        //(context as? Activity)?.finish()
    }

     */

    LottieAnimation(
        composition = preLoaderLottieComposition,
        progress = preLoaderProgress.value,
        modifier = modifier
    )
}
