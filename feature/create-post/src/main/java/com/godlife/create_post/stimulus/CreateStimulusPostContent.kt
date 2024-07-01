package com.godlife.create_post.stimulus

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.create_post.R
import com.godlife.designsystem.theme.OpaqueLight
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CreateStimulusPostContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CreateStimulusPostViewModel
) {

    val coverImg = viewModel.coverImg.collectAsState()
    val title = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()

    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    val context = LocalContext.current

    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    // 갤러리에서 사진 가져오기
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            val resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {

                //이때 이미지를 서버로 통신 후 받은 값을 이미지 태크 안에 보여줌
                //insertImageToWebView(webViewRef.value!!, resizeUri)
                insertImageTest(webViewRef.value!!, resizeUri)


                val file = File(resizeUri.path)
                Log.e("이미지 사이즈", file.length().toString())
                Log.e("이미지 URI", resizeUri.toString())

            }
        }

    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){



        val context = LocalContext.current

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
                    }
                )



            }

        }

    }


}

fun insertImageToWebView(webView: WebView, imageUri: Uri) {
    val contentResolver = webView.context.contentResolver
    val inputStream = contentResolver.openInputStream(imageUri)
    val bytes = inputStream?.readBytes()
    inputStream?.close()

    val image = imageUri.toString()

    //webView.evaluateJavascript("javascript:insertImage('$dataUrl');", null)
    webView.evaluateJavascript("javascript:insertImage('$image');") { result ->
        Log.e("WebView", "JavaScript execution result: $result")
    }


    /*
    if (bytes != null) {

        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        val dataUrl = "data:image/jpeg;base64,$base64"

        //webView.evaluateJavascript("javascript:insertImage('$dataUrl');", null)
        webView.evaluateJavascript("javascript:insertImage('$dataUrl');") { result ->
            Log.e("WebView", "JavaScript execution result: $result")
        }
        webView.evaluateJavascript("javascript:showHtml();", null)
    }

     */
}

fun insertImageTest(webView: WebView, imageUri: Uri){

    /*
    서버에 이미지를 업로드 후 이미지 URL을 받아오는 코드 부분
    여기서 imgTestUrl은 서버에서 받아온 URL로 가정
    */

    val imgTestUrl = "https://storage.googleapis.com/god-life-bucket-image/31aeb647-74d7-4c9d-937f-e001f85179fe"

    webView.evaluateJavascript("javascript:insertImage('$imgTestUrl');") { result ->
        Log.e("WebView", "JavaScript execution result: $result")
    }

}

fun getHtmlFromWebView(webView: WebView, callback: (String) -> Unit) {

    webView.evaluateJavascript("javascript:getHtml();") { html ->
        callback(html)
    }
}

private fun convertResizeImage(imageUri: Uri, context: Context):Uri? {

    val bitmap: Bitmap

    if (Build.VERSION.SDK_INT >= 29) {

        val source: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, imageUri!!)

        try {
            bitmap = ImageDecoder.decodeBitmap(source)

            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

            val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)


            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()



            /*
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                tempFile
            )

             */


            return Uri.fromFile(tempFile)
            //return contentUri

        } catch (e: IOException) {
            e.printStackTrace()
        }
    } else {

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            //val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

            val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()

            return Uri.fromFile(tempFile)
            //return contentUri

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return null
}

@Composable
fun CreateStimulusPostContentPreview(
    modifier: Modifier = Modifier
){

}