package com.godlife.community_page.post_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.community_page.R
import com.godlife.community_page.navigation.StimulusPostRoute
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OpaqueLight
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.designsystem.theme.PurpleSecond

@Preview
@Composable
fun StimulusDetailScreen(
    modifier: Modifier = Modifier
) {

    var height by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current

    GodLifeTheme {

        Box(modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { height = with(localDensity){
                it.size.height.toDp()
            } }
        ){

            Image(painter = painterResource(id = R.drawable.category3), contentDescription = "", modifier.fillMaxSize(), contentScale = ContentScale.Crop)

            LazyColumn{


                item { StimulusPostCover(height = height) }

                //item { Text(text = height.toString())}

                item { PostContentPreview() }

            }
        }


    }
}

@Preview
@Composable
fun StimulusPostCover(
    modifier: Modifier = Modifier,
    height: Dp = 800.dp
) {

    Box(modifier = modifier
        .height(height)
        .fillMaxWidth()
        .background(
            brush = Brush.verticalGradient(listOf(OpaqueLight, OpaqueDark, Color(0xD9000000)))
        ),
        contentAlignment = Alignment.BottomStart
    ){

        Column(modifier = modifier
            .height(300.dp)
            .padding(10.dp),
            verticalArrangement = Arrangement.Center){

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.ic_person), contentDescription = "",
                    modifier
                        .background(color = GrayWhite, shape = CircleShape)
                        .size(30.dp))

                Spacer(modifier.size(10.dp))

                Text(text = "Nickname", style = TextStyle(color = Color.White))
            }

            Spacer(modifier.size(20.dp))

            Text(text = "Title", style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }

    }

}

@Preview
@Composable
fun PostContentPreview(
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xD9000000))
            .padding(20.dp)
    ) {

        Text(
            text = "대충 뭐라고 갈겨 쓰는 소제목 글",
            style = TextStyle(color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 ",
            style = TextStyle(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 ",
            style = TextStyle(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 ",
            style = TextStyle(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "대충 뭐라고 갈겨 쓰는 소제목 글",
            style = TextStyle(color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier.size(20.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ){
            Image(painter = painterResource(id = R.drawable.category3), contentDescription = "", modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
        }

        Text(
            text = "대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 ",
            style = TextStyle(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 대충 뭐라고 갈겨 쓰는 글 ",
            style = TextStyle(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        )

        Spacer(modifier.size(20.dp))


        Spacer(modifier.size(20.dp))

        Row {

            Text(
                text = "조회수: 100",
                style = TextStyle(color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Normal)
            )

            Spacer(modifier.size(20.dp))

            Text(
                text = "댓글: 33",
                style = TextStyle(color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Normal)
            )
        }

        Spacer(modifier.size(20.dp))


        Column(modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End){

            Row(verticalAlignment = Alignment.CenterVertically) {

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(text = "작성자 닉네임", style = TextStyle(color = Color.White))

                    Text(text = "대충 나를 이렇게 소개한다는 내용", style = TextStyle(color = Color.White))

                }


                Spacer(modifier.size(30.dp))

                Image(painter = painterResource(id = R.drawable.ic_person), contentDescription = "",
                    modifier
                        .background(color = GrayWhite, shape = CircleShape)
                        .size(50.dp))

            }

        }

        Spacer(modifier.size(20.dp))

        HorizontalDivider()

        Spacer(modifier.size(20.dp))

        AnotherPostPreview()

        Spacer(modifier.size(20.dp))

    }
}

@Preview
@Composable
fun AnotherPostPreview(
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.Start
    ){

        Text(
            text = "닉네임 님, 이런 글은 어때요?",
            style = TextStyle(color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        )



    }

}