package com.godlife.profile

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark

@Preview
@Composable
fun ImageZoomInScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OpaqueDark)
            .clickable { }
    ){

        val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    }
}