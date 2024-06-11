package com.godlife.profile

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.profile.navigation.ImageZoomInScreenRoute
import com.godlife.profile.navigation.ProfileScreenRoute


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    GodLifeTheme {

        Scaffold(

        ) { innerPadding ->

            NavHost(navController = navController, startDestination = ProfileScreenRoute.route) {

                composable(ProfileScreenRoute.route){
                    ProfileBox(innerPadding = innerPadding, navController = navController, previewMode = false)
                }

                composable(ImageZoomInScreenRoute.route){
                    ImageZoomInScreen(navController = navController)
                }

                /*
                //PostDeatil Screen
                composable("${PostDetailRoute.route}/{postId}", arguments = listOf(navArgument("postId"){type = NavType.StringType})){
                    val postId = it.arguments?.getString("postId")
                    if (postId != null) {
                        PostDetailScreen(postId = postId)
                    }
                }

                 */

            }

            ProfileBox(innerPadding = innerPadding, previewMode = false, navController = navController)

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileBox(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(10.dp),
    previewMode: Boolean = true,
    navController: NavController? = null
){
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        //배경 사진
        if(!previewMode){

            val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
            val imageModifier: Modifier = modifier
                .fillMaxSize()
                .clickable { /*navController?.navigate(ImageZoomInScreenRoute.route)*/ }

            Glide.with(LocalContext.current)
                .asBitmap()
                .load(R.drawable.category3)
                .error(R.drawable.category3)
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
                )   //bitmap이 없다면
            } ?: Image(
                painter = painterResource(id = R.drawable.category3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        }

        else{
            Image(
                modifier = modifier
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.category3),
                contentDescription = "background",
                contentScale = ContentScale.Crop
            )
        }

        //배경 사진 필터
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OpaqueDark)
        )
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(10.dp)
                    .statusBarsPadding(),
                contentAlignment = Alignment.TopEnd
            ){

                //본인의 프로필이 아니면 아래 아이콘, 본인의 프로필이면 설정 아이콘
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                //프로필 사진
                if(!previewMode){

                    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                    val imageModifier: Modifier = modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .clickable { /*navController?.navigate(ImageZoomInScreenRoute.route)*/ }

                    Glide.with(LocalContext.current)
                        .asBitmap()
                        .load(R.drawable.category4)
                        .error(R.drawable.category4)
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
                        )   //bitmap이 없다면
                    } ?: Image(
                        painter = painterResource(id = R.drawable.category4),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier
                    )
                }

                else{
                    Image(
                        modifier = modifier
                            .size(130.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.category4),
                        contentDescription = "background",
                        contentScale = ContentScale.Crop
                    )
                }


                Spacer(modifier = modifier.size(20.dp))

                //닉네임
                Text(text = "닉네임",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.size(10.dp))

                //소개글
                Text(text = "안녕하세요! 갓생을 꿈꾸는 유저입니다.",
                    style = TextStyle(
                        color = GrayWhite2,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = modifier.size(30.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = modifier
                            .weight(0.33f)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "갓생 티어",
                            style = TextStyle(
                                color = GrayWhite2,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier.size(5.dp))

                        HorizontalDivider(
                            modifier
                                .background(GrayWhite2)
                                .width(70.dp))

                        Spacer(modifier.size(10.dp))

                        //티어 보일 공간
                        Text(text = "마스터",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        modifier = modifier
                            .weight(0.33f)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "갓생 점수",
                            style = TextStyle(
                                color = GrayWhite2,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier.size(5.dp))

                        HorizontalDivider(
                            modifier
                                .background(GrayWhite2)
                                .width(70.dp))

                        Spacer(modifier.size(10.dp))

                        //티어 보일 공간
                        Text(text = "630점",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        modifier = modifier
                            .weight(0.33f)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "게시물 수",
                            style = TextStyle(
                                color = GrayWhite2,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier.size(5.dp))

                        HorizontalDivider(
                            modifier
                                .background(GrayWhite2)
                                .width(70.dp))

                        Spacer(modifier.size(10.dp))

                        //티어 보일 공간
                        Text(text = "173개",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }


                }

                Box(
                    modifier = modifier
                        .fillMaxSize()
                    , contentAlignment = Alignment.BottomCenter
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "",
                            tint = GrayWhite2
                        )

                        Spacer(modifier.size(5.dp))

                        Text(text = "위로 올려서 게시물을 확인하세요.",
                            style = TextStyle(
                                color = GrayWhite2,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Thin
                            ),
                            textAlign = TextAlign.Center
                        )

                    }
                }


            }


        }

        BottomSheetScaffold(
            modifier = modifier
                .fillMaxWidth(),
            sheetContainerColor = OpaqueDark,
            sheetShape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 20.dp,
                topEnd = 20.dp
            ),
            sheetContent = {  ProfileContentBox() }
        ) {

        }


    }
}

@Preview(showBackground = true)
@Composable
fun ProfileContentBox(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ){
        Text(text = "닉네임님의 게시물", style = TextStyle(color = GrayWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        LazyColumn {

            items(10){
                PostListPreview()
            }
        }
    }
}

@Preview
@Composable
fun PostListPreview(modifier: Modifier = Modifier){

    Row(
        modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        //대표 이미지 보일 부분
        Box(
            modifier = modifier
                .size(70.dp)
                .background(GrayWhite2, shape = RoundedCornerShape(15.dp))
        )

        Spacer(modifier.size(10.dp))

        Column {

            // 게시물 제목
            Text(text = "게시물 제목", style = TextStyle(color = GrayWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier.size(5.dp))

            //날짜
            Text(text = "1일 전", style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal))

            Spacer(modifier.size(5.dp))

            //조회수, 댓글 수
            Text(text = "조회 수: 100, 댓글 수: 10", style = TextStyle(color = GrayWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal))

        }


    }
}
