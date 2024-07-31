package com.godlife.community_page.post_detail.post_update.stimulus

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.composable

@Composable
fun UpdateStimulusPostScreen(
    postId: String,
    parentNavController: NavController,
    viewModel: UpdateStimulusPostViewModel = hiltViewModel()
){
    val navController = rememberNavController()

    val context = LocalContext.current

    viewModel.getStimulusPost(postId, context)

    NavHost(navController = navController, startDestination = UpdateStimulusPostCoverRoute.route){

        composable(UpdateStimulusPostCoverRoute.route){
            UpdateStimulusPostCover(
                navController = navController,
                viewModel = viewModel,
                context = context
            )
        }

        composable(UpdateStimulusPostContentRoute.route){
            UpdateStimulusPostContent(
                navController = navController,
                context = context,
                parentNavController = parentNavController,
                viewModel = viewModel
            )
        }
    }
}

object UpdateStimulusPostScreenRoute{
    const val route = "UpdateStimulusPostScreen"
}

object UpdateStimulusPostCoverRoute{
    const val route = "UpdateStimulusPostCover"
}

object UpdateStimulusPostContentRoute{
    const val route = "UpdateStimulusPostContent"
}

