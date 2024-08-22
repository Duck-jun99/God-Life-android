package com.godlife.community_page.post_detail.post_update.stimulus

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.godlife.community_page.navigation.StimulusPostDetailRoute
import com.godlife.create_post.R
import com.godlife.create_post.stimulus.QuillInterface
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueLight
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.godlife.designsystem.view.GodLifeLoadingScreen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import java.io.File

class QuillInterface(
    val launcher: ManagedActivityResultLauncher<String, Uri?>
) {
    @JavascriptInterface
    fun getHtml(html: String) {
        // HTML 데이터를 가져오는 메소드
    }

    @JavascriptInterface
    fun openImagePicker() {
        // 이미지 URL을 삽입하는 메소드
        launcher.launch("image/*")
    }

    @JavascriptInterface
    fun setHtml(html: String){
        // HTML 데이터 구성
    }
}

@Composable
fun UpdateStimulusPostContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    parentNavController: NavController,
    context: Context,
    viewModel: UpdateStimulusPostViewModel
){

    val completeDialogVisible = remember{ mutableStateOf(false) }
    val backDialogVisible = remember{ mutableStateOf(false) }

    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    val uiState = viewModel.uiState.collectAsState().value

    val coverImg = viewModel.coverImg.collectAsState()

    val postId = viewModel.boardId.collectAsState().value

    BackHandler {
        backDialogVisible.value = !backDialogVisible.value
    }

    // 갤러리에서 사진 가져오기
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            val resizeUri = viewModel.convertResizeImage(uri, context)

            if (resizeUri != null) {

                //이때 이미지를 서버로 통신 후 받은 값을 이미지 태크 안에 보여줌
                //insertImageToWebView(webViewRef.value!!, resizeUri)
                viewModel.insertImageToWebView(webViewRef.value!!, resizeUri, false)

                val file = File(resizeUri.path)
                Log.e("이미지 사이즈", file.length().toString())
                Log.e("이미지 URI", resizeUri.toString())

            }
        }

    }

    if(uiState is UpdateStimulusUiState.Loading
        || uiState is UpdateStimulusUiState.Success
        || uiState is UpdateStimulusUiState.SendSuccess){

        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){


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

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(10.dp)
            ) {

                item {
                    Column(
                        modifier = modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .background(color = OpaqueLight, shape = RoundedCornerShape(18.dp))
                            .padding(10.dp)
                    ){

                        Text(
                            text = "글 작성시 꼭 기억해주세요!",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Box(
                            modifier = modifier
                                .padding(10.dp)
                        ){

                            Text(
                                text =  stringResource(id = R.string.content_guide),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )

                        }

                    }
                }

                item{

                    AndroidView(
                        modifier = modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(18.dp)),
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                webChromeClient = WebChromeClient()
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.allowFileAccess = true  // 파일 접근 허용
                                settings.allowContentAccess = true  // 콘텐츠 접근 허용
                                settings.allowFileAccessFromFileURLs = true
                                settings.allowUniversalAccessFromFileURLs = true

                                addJavascriptInterface(QuillInterface(launcher), "QuillInterface")

                                loadUrl("file:///android_asset/editor.html")

                                webViewRef.value = this


                                /*
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)

                                        Log.e("WebView", "Page loaded")


                                    }
                                }

                                 */


                            }
                        },
                        update = { webView ->
                            // 필요 시 WebView 업데이트 로직 추가
                            viewModel.setHtmlToWebView(webViewRef.value!!)

                        }
                    )



                }

                item{
                    GodLifeButtonWhite(
                        onClick = {
                            webViewRef.value?.let { viewModel.getHtmlFromWebView(it) }
                            completeDialogVisible.value = !completeDialogVisible.value
                        },
                        text = { Text(text = "수정 완료", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                    )
                }

            }

            if(completeDialogVisible.value){
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { completeDialogVisible.value = !completeDialogVisible.value },
                    title = {
                        Text(text = "수정이 완료되셨나요?", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    },
                    text = {
                        Text(text = "주의사항을 확인하셨다면 '글 게시하기' 버튼을 눌러주세요.", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
                    },
                    confirmButton = {
                        GodLifeButtonWhite(
                            onClick = {

                                viewModel.updateCoverImg()

                                completeDialogVisible.value = !completeDialogVisible.value

                            },
                            text = { Text(text = "글 게시하기", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    },
                    dismissButton = {
                        GodLifeButtonWhite(
                            onClick = { completeDialogVisible.value = !completeDialogVisible.value },
                            text = { Text(text = "취소", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                )
            }

            if(backDialogVisible.value){
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { completeDialogVisible.value = !completeDialogVisible.value },
                    title = {
                        Text(text = "이전 단계로 돌아가시겠어요?", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    },
                    text = {
                        Text(text = "지금 작성하신 내용은 저장되지 않아요.", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
                    },
                    confirmButton = {
                        GodLifeButtonWhite(
                            onClick = {

                                navController.popBackStack(
                                    route = UpdateStimulusPostCoverRoute.route,
                                    inclusive = false
                                )

                                backDialogVisible.value = !backDialogVisible.value

                            },
                            text = { Text(text = "돌아가기", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    },
                    dismissButton = {
                        GodLifeButtonWhite(
                            onClick = { backDialogVisible.value = !backDialogVisible.value },
                            text = { Text(text = "취소", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                )
            }

            if(uiState is UpdateStimulusUiState.Loading){
                GodLifeLoadingScreen(
                    text = "작성하신 글을 완성중이에요.\n잠시만 기다려주세요."
                )
            }

            if(uiState is UpdateStimulusUiState.SendSuccess){
                GodLifeLoadingScreen(
                    text = "수정이 완료되었어요.\n잠시후 자동으로 이동할게요."
                )
                LaunchedEffect(true) {
                    delay(3000L)

                    parentNavController.navigate("${StimulusPostDetailRoute.route}/${postId}"){
                        popUpTo(parentNavController.graph.startDestinationId) {inclusive = false}
                    }
                }
            }

        }

    }

    else if(uiState is UpdateStimulusUiState.Error){
        GodLifeErrorScreen(
            errorMessage = uiState.message,
            buttonEvent = {parentNavController.popBackStack()},
            buttonText = "돌아가기"
        )
    }

}