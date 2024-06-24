package com.godlife.create_post.post

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.TagItem
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun CreatePostScreen(
    createPostViewModel: CreatePostViewModel,
    createPostActivity: CreatePostActivity,
    navController: NavController,
    modifier: Modifier = Modifier
){

    val context = LocalContext.current

    val selectedImgList by createPostViewModel.selectedImgUri.collectAsState()

    var title by remember { createPostViewModel.title }
    var text by remember { createPostViewModel.text }


    // 갤러리에서 사진 가져오기
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null) {
            var resizeUri = convertResizeImage(uri, context)

            if (resizeUri != null) {
                createPostViewModel.saveImg(resizeUri)

                val file = File(resizeUri.path)
                Log.e("이미지 사이즈", file.length().toString())
            }
        }

    }

    GodLifeTheme {
        Column(
            modifier
                .fillMaxSize()
        ) {

            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "게시물 작성", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }

            LazyColumn(
                modifier.padding(20.dp)
            ) {

                item{ Text(text = "제목", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }


                item{ GodLifeTextFieldGray(text = title, onTextChanged = { title = it }) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "내용", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }


                item{ GodLifeTextFieldGray(text = text, onTextChanged = { text = it }) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "투두 목록", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }

                item{
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        items(5) {
                            TagItemPreview()
                        }
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

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }

                item{
                    LazyRow {
                        selectedImgList?.let {
                            itemsIndexed(it){ index, item ->
                                Log.e("fbjkkjhsad",index.toString())
                                SelectImage(index, item, LocalContext.current)
                            }
                        }

                    }
                }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "* 이미지는 최대 5장까지 올릴 수 있어요.", style = GodLifeTypography.bodySmall) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)){

                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                                .clickable { navController.navigate("CreatePostPreviewScreen") },
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(7.dp),
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Text(
                                text = "미리 보기",
                                color = PurpleMain,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)){

                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                                .clickable { createPostViewModel.createPost() },
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(7.dp),
                            colors = CardDefaults.cardColors(PurpleMain)
                        ) {
                            Text(
                                text = "작성 완료",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                                modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                } }

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
fun SelectImage(index:Int, imageUri: Uri, context: Context){
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    val num = index+1

    Box(modifier = Modifier
        .padding(end = 10.dp)
        .size(150.dp)) {

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
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier
            )
        }


        Box(modifier = Modifier
            .size(50.dp)
            .background(PurpleMain, shape = CircleShape)
        ){
            Text(text = num.toString(), style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) ,modifier = Modifier.align(Alignment.Center))
        }
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

            return Uri.fromFile(tempFile)

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

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return null
}

@Preview
@Composable
fun CreatePostScreenPreview(modifier: Modifier = Modifier){

    var title by remember { mutableStateOf("제목을 입력해주세요.") }
    var text by remember { mutableStateOf("내용을 입력해주세요.") }

    GodLifeTheme {
        Column(
            modifier
                .fillMaxSize()
                .background(Color.White)) {

            Surface(shadowElevation = 7.dp) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "게시물 작성", style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }

            LazyColumn(
                modifier.padding(20.dp)
            ) {

                item{ Text(text = "제목", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }


                item{ GodLifeTextFieldGray(text = title, onTextChanged = { title = it }) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "내용", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }


                item{ GodLifeTextFieldGray(text = text, onTextChanged = { text = it }) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "투두 목록", style = GodLifeTypography.titleMedium) }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }

                item{
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        items(5) {
                            TagItemPreview()
                        }
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
                            onClick = { /* Todo */ }
                        )
                    }
                }

                item{ Divider(
                    color = GrayWhite2,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                ) }

                item{ Box(
                    modifier
                        .background(GrayWhite)
                        .size(150.dp)) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Text(text = "* 이미지는 최대 5장까지 올릴 수 있어요.", style = GodLifeTypography.bodySmall) }

                item{ Spacer(modifier.padding(10.dp)) }

                item{ Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)){

                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(7.dp),
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Text(
                                text = "미리 보기",
                                color = PurpleMain,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)){

                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(7.dp),
                            colors = CardDefaults.cardColors(PurpleMain)
                        ) {
                            Text(
                                text = "작성 완료",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                                modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                } }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagItemPreview(modifier: Modifier = Modifier, text:String = "Tag"){
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
            Text(text = text,
                style = TextStyle(color = Color.White),
                textAlign = TextAlign.Center,
            )
        }
    }

}

@Composable
fun AddButton(onClick: () -> Unit) {
    GodLifeButtonWhite(
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Add, // Choose an appropriate icon
                contentDescription = "Add icon",
                tint = PurpleMain,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {Text("추가하기", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold))}
    )
}

@Preview
@Composable
fun AddButtonPreview() {
    GodLifeButtonWhite(
        onClick = { /* TODO: Implement onClick */ },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Add, // Choose an appropriate icon
                contentDescription = "Add icon",
                tint = PurpleMain,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {Text("추가하기", style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold))}
    )
}

@Preview
@Composable
fun SelectImagePreview(){
    Box(modifier = Modifier.padding(end = 10.dp)) {

        //Image 부분
        Box(modifier = Modifier
            .size(150.dp)
            .background(GrayWhite)){

        }
        Box(modifier = Modifier
            .size(50.dp)
            .background(PurpleMain, shape = CircleShape)
        ){
            Text(text = "1", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) ,modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
fun RowButton(){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically){
        Box(
            Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)){

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Text(
                    text = "미리 보기",
                    color = PurpleMain,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
                )
            }
        }

        Box(
            Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)){

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                colors = CardDefaults.cardColors(PurpleMain)
            ) {
                Text(
                    text = "작성 완료",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}