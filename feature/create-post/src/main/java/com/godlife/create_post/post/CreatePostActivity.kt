package com.godlife.create_post.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostActivity: ComponentActivity() {
    //private val createPostViewModel: CreatePostViewModel by viewModels<CreatePostViewModel>()

    lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            CreatePostUI(this)
        }

    }

}

@Composable
fun CreatePostUI(
    createPostActivity: CreatePostActivity,
    createPostViewModel: CreatePostViewModel = hiltViewModel()
){
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Column(
        modifier = Modifier.fillMaxWidth().statusBarsPadding()
    ) {
        NavHost(navController = navController, startDestination = "CreatePostScreen", modifier = Modifier.fillMaxSize()){
            composable("CreatePostScreen"){
                CreatePostScreen(createPostViewModel = createPostViewModel, createPostActivity = createPostActivity, navController)
            }
            composable("CreatePostPreviewScreen"){
                CreatePostPreviewScreen(createPostViewModel = createPostViewModel, navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreatePostActivityPreview(
    createPostViewModel: CreatePostViewModel = hiltViewModel()
){
    //CreatePostScreen(createPostViewModel, )
}