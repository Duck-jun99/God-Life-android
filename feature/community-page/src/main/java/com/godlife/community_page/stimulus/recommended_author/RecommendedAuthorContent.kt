package com.godlife.community_page.stimulus.recommended_author

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.community_page.BuildConfig
import com.godlife.community_page.R
import com.godlife.community_page.stimulus.RecommendUserItem
import com.godlife.community_page.stimulus.StimulusPostItem
import com.godlife.community_page.stimulus.recommended_author_post.RecommendedAuthorStimulusPostContent
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.network.model.RecommendPostBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun RecommendedAuthorInfoContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecommendedAuthorViewModel = hiltViewModel()
){

    val user = viewModel.authorInfo.collectAsState()

    Box(
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){

        GlideImage(
            imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + user.value?.backgroundUrl },
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
                        color = PurpleMain
                    )

                }

            },
            failure = {
                Image(
                    painter = painterResource(id = R.drawable.category4),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        )

        user.value?.let {
            RecommendUserProfile(
                user = it
            )
        }

    }

    //Spacer(modifier.height(10.dp))

    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 10.dp, start = 20.dp),
        text = "${user.value?.nickname}님의 게시물",
        style = TextStyle(
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    )

    RecommendedAuthorStimulusPostContent(
        navController = navController
    )

}

@Composable
fun RecommendUserProfile(
    modifier: Modifier = Modifier,
    user: RecommendPostBody
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = "추천 작가",
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Start
        )

        Spacer(modifier.size(20.dp))

        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){

            Column(
                modifier = modifier
                    .weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GlideImage(
                    imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + user.profileUrl },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .size(150.dp)
                        .clip(shape = CircleShape),
                    loading = {
                        Box(
                            modifier = modifier
                                .background(GrayWhite3)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){

                            CircularProgressIndicator(
                                color = PurpleMain
                            )

                        }

                    },
                    failure = {
                        Box(
                            modifier = modifier
                                .background(GrayWhite3)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){

                            Image(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )

                        }

                    }
                )

                Spacer(modifier.size(20.dp))

                Text(
                    text = user.nickname,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = modifier
                    .weight(0.5f)
            ){

                Text(
                    modifier = modifier
                        .padding(start = 20.dp, end = 20.dp),
                    text = "\"${user.whoAmI}\"",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )

            }



        }


    }

}


@Preview
@Composable
fun RecommendedAuthorInfoContentPreview(
    modifier: Modifier = Modifier
){

    val user = RecommendUserItem(
        nickname = "치킨 러버",
        introText = "치킨을 좋아하는 사람입니다.",
        profileImg = R.drawable.category3,
        backgroundImg = R.drawable.category4,
        postItem = listOf(
            StimulusPostItem(
                title = "이것은 제목!",
                writer = "치킨 러버",
                coverImg = R.drawable.category3,
                introText = "대충 이러이러한 내용이라고 소개하는 내용"
            ),
            StimulusPostItem(
                title = "나도 제목!",
                writer = "초코 러버",
                coverImg = R.drawable.category4,
                introText = "대충 이러이러한 내용이라고 소개하는 내용"
            ),
            StimulusPostItem(
                title = "제목 등장",
                writer = "라면 좋아",
                coverImg = R.drawable.category3,
                introText = "대충 이러이러한 내용이라고 소개하는 내용"
            ),
            StimulusPostItem(
                title = "제모오오오옥",
                writer = "헬로우",
                coverImg = R.drawable.category4,
                introText = "대충 이러이러한 내용이라고 소개하는 내용"
            )
        )
    )

    Box(
        modifier = modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){


        Image(
            modifier = modifier
                .fillMaxSize()
                .blur(
                    radiusX = 15.dp, radiusY = 15.dp
                ),
            painter = painterResource(id = user.backgroundImg),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.6f
        )

        RecommendUserProfilePreview()

    }

}

@Preview
@Composable
fun RecommendUserProfilePreview(
    modifier: Modifier = Modifier,
    item: RecommendUserItem = RecommendUserItem(nickname = "치킨 러버", introText = "치킨을 좋아하는 사람입니다.", profileImg = R.drawable.category3, backgroundImg = R.drawable.category4, postItem = emptyList())
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = "추천 작가",
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Start
        )

        Spacer(modifier.size(20.dp))

        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){

            Column(
                modifier = modifier
                    .weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = modifier
                        .size(150.dp)
                        .clip(shape = CircleShape),
                    painter = painterResource(id = item.profileImg),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier.size(20.dp))

                Text(
                    text = item.nickname,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = modifier
                    .weight(0.5f)
            ){

                Text(
                    modifier = modifier
                        .padding(start = 20.dp, end = 20.dp),
                    text = "\"${item.introText}\"",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )

            }



        }


    }

}
