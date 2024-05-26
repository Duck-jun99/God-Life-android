package com.godlife.create_post

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.godlife.create_post.CreatePostScreen

@AndroidEntryPoint
class CreatePostActivity: ComponentActivity() {
    private val createPostViewModel: CreatePostViewModel by viewModels<CreatePostViewModel>()

    lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 현재 기기에 설정된 쓰기 권한을 가져오기 위한 변수
        var writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE )

        // 현재 기기에 설정된 읽기 권한을 가져오기 위한 변수
        var readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        // 읽기 권한과 쓰기 권한에 대해서 설정이 되어있지 않다면
        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            // 읽기, 쓰기 권한을 요청합니다.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        }

        setContent {

            CreatePostUI(createPostViewModel, this)
        }

    }

}

@Composable
fun CreatePostUI(createPostViewModel:CreatePostViewModel, createPostActivity: CreatePostActivity){
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Column(
        modifier = Modifier.fillMaxWidth()
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