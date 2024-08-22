package com.godlife.create_post.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.navigator.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostActivity: ComponentActivity() {
    //private val createPostViewModel: CreatePostViewModel by viewModels<CreatePostViewModel>()

    lateinit var getResult: ActivityResultLauncher<Intent>

    @Inject
    lateinit var mainNavigator: MainNavigator

    private var showExitDialog by mutableStateOf(false)

    //콜백 인스턴스 생성
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            // 뒤로 버튼 이벤트 처리
            showExitDialog = true

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.onBackPressedDispatcher.addCallback(this, callback)

        setContent {

            if (showExitDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showExitDialog = false },
                    title = {
                        Text(text = "작성을 종료할까요?", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    },
                    text = {
                        Text(text = "작성 중인 내용은 저장되지 않아요.", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
                    },
                    confirmButton = {
                        GodLifeButtonWhite(
                            onClick = { mainNavigator.navigateFrom(this, withFinish = true) },
                            text = { Text(text = "종료하기", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    },
                    dismissButton = {
                        GodLifeButtonWhite(
                            onClick = { showExitDialog = false },
                            text = { Text(text = "취소", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                )
            }

            CreatePostUI(this, mainNavigator)
        }

    }

    override fun onDestroy() {
        Log.e("CreatePostActivity", "onDestroy")
        super.onDestroy()
    }


}

@Composable
fun CreatePostUI(
    createPostActivity: CreatePostActivity,
    mainNavigator: MainNavigator,
    createPostViewModel: CreatePostViewModel = hiltViewModel()
){
    val navController = rememberNavController()

    //val navBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = navBackStackEntry?.destination?.route


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        NavHost(navController = navController, startDestination = "CreatePostScreen", modifier = Modifier.fillMaxSize()){
            composable("CreatePostScreen"){
                CreatePostScreen(viewModel = createPostViewModel, createPostActivity = createPostActivity, navController, mainNavigator)
            }
            composable("CreatePostPreviewScreen"){
                CreatePostPreviewScreen(viewModel = createPostViewModel, navController)
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

