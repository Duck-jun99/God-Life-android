package com.godlife.create_post.stimulus

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.create_post.R
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextField
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OpaqueLight
import com.godlife.designsystem.theme.PurpleMain

@Composable
fun CreateStimulusPostScreen(
    modifier: Modifier = Modifier,
    bottomBarVisibleState: MutableState<Boolean>,
    fabVisibleState: MutableState<Boolean>,
    viewModel: CreateStimulusPostViewModel = hiltViewModel()
){
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    val cScope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = CreateStimulusPostCoverRoute.route){


        composable(CreateStimulusPostCoverRoute.route){
            bottomBarVisibleState.value = false
            fabVisibleState.value = false

            viewModel.getTempBoardId()

            when(uiState){

                is CreateStimulusUiState.Loading -> {

                }
                is CreateStimulusUiState.Success -> {

                    CreateStimulusPostCover(navController = navController, viewModel = viewModel, cScope = cScope)

                }
                is CreateStimulusUiState.Error -> {

                }

            }

        }

        composable(CreateStimulusPostLoading.route){
            bottomBarVisibleState.value = false
            fabVisibleState.value = false

            CreateStimulusPostLoading(navController = navController, viewModel = viewModel)
        }

        composable(
            route = CreateStimulusPostContent.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ){

            bottomBarVisibleState.value = false
            fabVisibleState.value = false

            CreateStimulusPostContent(navController = navController, viewModel = viewModel, cScope = cScope)

        }

    }
}


@Preview(showBackground = true)
@Composable
fun CreateStimulusPostScreenPreview(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CreateStimulusPostCoverRoute.route){
        composable(CreateStimulusPostCoverRoute.route){

            CreateStimulusPostCoverPreview( )
        }

        composable(CreateStimulusPostContent.route){

            CreateStimulusPostContentPreview( )
        }

    }
}

object CreateStimulusPostCoverRoute{
    const val route = "CreateStimulusPostCover"
}

object CreateStimulusPostLoading{
    const val route = "CreateStimulusPostLoading"
}

object CreateStimulusPostContent{
    const val route = "CreateStimulusPostContent"
}



