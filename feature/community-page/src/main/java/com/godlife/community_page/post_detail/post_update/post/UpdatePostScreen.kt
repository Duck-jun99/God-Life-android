package com.godlife.community_page.post_detail.post_update.post

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.community_page.post_detail.PostDetailViewModel
import com.godlife.create_post.post.AddButton
import com.godlife.create_post.post.TagItemPreview
import com.godlife.create_post.post.convertResizeImage
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.godlife.designsystem.view.GodLifeLoadingScreen
import com.godlife.model.community.TagItem
import com.godlife.network.model.PostDetailBody
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UpdatePostScreen(
    modifier: Modifier = Modifier,
    postDetail: PostDetailBody,
    postDetailViewModel: PostDetailViewModel,
    postDetailContext: Context,
    viewModel: UpdatePostViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    Log.e("UpdatePostScreen", uiState.toString())


    val context = LocalContext.current

    LaunchedEffect(key1 = postDetail) {
        viewModel.init(postDetail, context)
    }

    val selectedImgList by viewModel.selectedImgUri.collectAsState()

    val title by remember { viewModel.title }
    val text by remember { viewModel.text }

    var isDialogVisble by remember { mutableStateOf(false) }


    // 갤러리에서 사진 가져오기
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            var resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {
                viewModel.saveImg(resizeUri)

                val file = File(resizeUri.path)
                Log.e("이미지 사이즈", file.length().toString())
            }
        }

    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ){

        if(uiState == UpdatePostUiState.Loading ||
            uiState == UpdatePostUiState.Init ||
            uiState == UpdatePostUiState.SendLoading
        ){

            Column(
                modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .statusBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "굿생 인증하기", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }

                LazyColumn(
                    modifier.padding(20.dp)
                ) {

                    item{
                        Text(text = "제목", style = GodLifeTypography.titleMedium)

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }


                    item{

                        Box(
                            modifier
                                .padding(10.dp)
                                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = title,
                                onTextChanged = { viewModel.updateTitle(it) },
                                hint = "제목을 입력해주세요.",
                                singleLine = true
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${title.length}/30",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.End
                        )


                        Spacer(modifier.padding(10.dp))
                    }

                    item{
                        Text(text = "내용", style = GodLifeTypography.titleMedium)

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }

                    item{

                        Box(
                            modifier
                                .padding(10.dp)
                                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                                .heightIn(min = 200.dp)
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = text,
                                onTextChanged = { viewModel.updateText(it) },
                                hint = "내용을 입력해주세요.\n달성한 투두리스트에 대해 설명해주시면 좋아요.",
                                singleLine = false
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${text.length}/1000",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.End
                        )


                        Spacer(modifier.padding(10.dp))
                    }


                    item{
                        Text(text = "투두 목록", style = GodLifeTypography.titleMedium)

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }


                    item{
                        FlowRow {
                            TagItemPreview()
                            TagItemPreview()
                            TagItemPreview()
                            TagItemPreview()
                            TagItemPreview()
                        }
                    }

                    item{ Spacer(modifier.padding(10.dp)) }

                    //item{ Text(text = "이미지", style = GodLifeTypography.titleMedium) }

                    item{

                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(text = "이미지", style = GodLifeTypography.titleMedium)
                            }

                            Spacer(modifier = Modifier.size(20.dp))

                            AddButton(
                                onClick = { launcher.launch("image/*") }
                            )
                        }
                    }

                    item{
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            thickness = 2.dp,
                            color = GrayWhite2
                        )
                    }

                    item{
                        val state = rememberReorderableLazyListState(onMove = { from, to ->
                            viewModel.onMove(from.index, to.index)
                        })

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .detectReorderAfterLongPress(state)
                        ) {

                            LazyRow(
                                state = state.listState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .reorderable(state)
                            ) {
                                selectedImgList?.let { imgList ->
                                    items(
                                        items = imgList,
                                        key = { it.hashCode() } // URI나 복잡한 객체의 경우 고유한 식별자를 사용
                                    ) { item ->
                                        ReorderableItem(
                                            reorderableState = state,
                                            key = item.hashCode()
                                        ) { isDragging ->
                                            val elevation = animateDpAsState(if (isDragging) 8.dp else 0.dp)
                                            SelectImage(
                                                index = imgList.indexOf(item),
                                                imageUri = item,
                                                context = LocalContext.current,
                                                viewModel = viewModel,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            }

                        }



                    }

                    item{ Spacer(modifier.padding(10.dp)) }

                    item{ Text(text = "- 첫 번째 이미지는 대표 이미지로 설정돼요.", style = GodLifeTypography.bodySmall) }
                    item{ Text(text = "- 이미지는 최대 5장까지 올릴 수 있어요.", style = GodLifeTypography.bodySmall) }

                    item{ Spacer(modifier.padding(10.dp)) }

                    item{ Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically){

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)){

                            Card(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        if (title == "" || text == "") Toast
                                            .makeText(
                                                context,
                                                "제목과 내용을 모두 입력해주세요.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        else isDialogVisble = !isDialogVisble
                                    },
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(7.dp),
                                colors = CardDefaults.cardColors(PurpleMain)
                            ) {
                                Text(
                                    text = "작성 완료",
                                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    } }

                }


            }

            if(isDialogVisble){
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { isDialogVisble = !isDialogVisble },
                    title = {
                        Text(text = "게시물을 수정할까요?", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    },
                    text = {
                        Text(text = "수정이 완료되었으면 '게시하기' 버튼을 눌러주세요."
                            ,
                            style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal)
                        )
                    },
                    confirmButton = {
                        GodLifeButtonWhite(
                            onClick = {
                                viewModel.updatePost()
                                isDialogVisble = !isDialogVisble

                            },
                            text = { Text(text = "게시하기", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    },
                    dismissButton = {
                        GodLifeButtonWhite(
                            onClick = { isDialogVisble = !isDialogVisble },
                            text = { Text(text = "취소", style = TextStyle(color = PurpleMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                        )
                    }
                )
            }

        }



        when(uiState){
            is UpdatePostUiState.Loading -> {
                GodLifeLoadingScreen()
            }
            is UpdatePostUiState.Init -> {

            }
            is UpdatePostUiState.SendLoading -> {
                GodLifeLoadingScreen(
                    text = "게시물을 수정중이에요."
                )
            }
            is UpdatePostUiState.Success -> {

                LaunchedEffect(key1 = true){
                    Toast.makeText(postDetailContext, "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    postDetailViewModel.refreshPostDetail()
                }

            }
            is UpdatePostUiState.Error -> {
                GodLifeErrorScreen(
                    errorMessage = (uiState as UpdatePostUiState.Error).message,
                    buttonEnabled = false
                )
            }

        }

    }


}

@Composable
fun TagItem(tagItem: TagItem, modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Box(
            modifier
                .size(70.dp, 30.dp)
                .background(color = PurpleMain, shape = RoundedCornerShape(7.dp))
                .padding(2.dp)
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(text = tagItem.tagName,
                style = TextStyle(color = Color.White),
                textAlign = TextAlign.Center,
            )
        }
    }

}

@Composable
fun SelectImage(
    index:Int,
    imageUri: Uri,
    context: Context,
    viewModel: UpdatePostViewModel,
    modifier: Modifier = Modifier
){
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    Box(modifier = Modifier
        .padding(end = 10.dp)
        .size(150.dp)
        .border(width = 5.dp, color = if (index == 0) Color.Yellow else Color.White)
    ) {

        //Image 부분
        val imageModifier: Modifier = Modifier
            .size(150.dp, 150.dp)
            .fillMaxSize()

        Glide.with(context)
            .asBitmap()
            .load(imageUri)
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
                modifier = imageModifier
            )
        }

        Box(
            modifier = Modifier
                .padding(7.dp)
                .size(30.dp)
                .background(color = OpaqueDark, shape = CircleShape)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier
                    .clickable { viewModel.removeImg(imageUri) },
                imageVector = Icons.Default.Clear,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}