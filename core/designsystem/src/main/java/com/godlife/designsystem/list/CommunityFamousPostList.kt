package com.godlife.designsystem.list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.R
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.TagItem
import com.godlife.network.BuildConfig
import com.godlife.network.model.PostDetailBody
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommunityFamousPostList(
    modifier: Modifier = Modifier,
    famousPostItem: PostDetailBody,
    clickOption: () -> Unit
){
    Column(
        modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .width(220.dp)
            .height(350.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .shadow(7.dp, shape = RoundedCornerShape(10.dp))
            .clickable { clickOption() }
    ){
        Box(
            modifier
                .height(150.dp)
                .fillMaxWidth()
                .background(
                    color = GrayWhite2,
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
        ){
            GlideImage(
                imageModel = { if(famousPostItem.imagesURL?.isNotEmpty() == true) BuildConfig.SERVER_IMAGE_DOMAIN + famousPostItem.imagesURL?.get(0).toString() else R.drawable.category3 },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                ,
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
                        painter = painterResource(id = R.drawable.category3),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )


                }
            )
        }
        Column(
            modifier
                .background(color = Color.White)
                .height(200.dp)
                .fillMaxWidth()
                .padding(5.dp)) {

            Row(
                modifier
                    .height(30.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){

                GlideImage(
                    imageModel = { BuildConfig.SERVER_IMAGE_DOMAIN + famousPostItem.profileURL },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .size(30.dp)
                        .clip(CircleShape),
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
                            modifier = modifier
                                .background(color = GrayWhite2),
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                )

                Spacer(modifier.width(10.dp))

                Text(
                    text = famousPostItem.nickname,
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    overflow = TextOverflow.Ellipsis
                )

            }

            Text(
                modifier = modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                text = famousPostItem.title,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                overflow = TextOverflow.Ellipsis
            )


            Text(
                modifier = modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                text = famousPostItem.body,
                style = GodLifeTypography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )

            FlowRow(
                modifier = modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                overflow = FlowRowOverflow.expandOrCollapseIndicator(
                    expandIndicator = { Text(text = "...") },
                    collapseIndicator = { Text(text = "...") }
                )
            ){
                famousPostItem.tags.forEach {
                    TagItemView(it)
                }

            }

        }
    }

}


@Composable
fun TagItemView(
    tagItem: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(5.dp)
    ) {
        Box(
            modifier
                .background(color = GrayWhite3, shape = RoundedCornerShape(7.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp)
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(text = "#${tagItem}",
                style = TextStyle(color = Color.Black),
                textAlign = TextAlign.Center,
            )
        }
    }

}


@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun CommunityFamousPostListPreview(modifier: Modifier = Modifier){
    Box(modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)){
        Column(
            modifier
                .width(220.dp)
                .height(350.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .shadow(7.dp, shape = RoundedCornerShape(10.dp))
        ){
            Box(
                modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(
                        color = GrayWhite2,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
            ){
                GlideImage(
                    imageModel = {  },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        ,
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
                            painter = painterResource(id = R.drawable.category3),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )


                    }
                )
            }
            Column(
                modifier
                    .background(color = Color.White)
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(5.dp)) {

                Row(
                    modifier
                        .height(30.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    GlideImage(
                        imageModel = {  },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        ),
                        modifier = modifier
                            .size(30.dp)
                            .clip(CircleShape),
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
                                modifier = modifier
                                    .background(color = GrayWhite2),
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                        }
                    )

                    Spacer(modifier.width(10.dp))

                    Text(
                        text = "name",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        overflow = TextOverflow.Ellipsis
                    )

                }

                Text(
                    modifier = modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    text = "Title",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    overflow = TextOverflow.Ellipsis
                )


                Text(
                    modifier = modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    text = "Text",
                    style = GodLifeTypography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )

                FlowRow(
                    modifier = modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    overflow = FlowRowOverflow.expandOrCollapseIndicator(
                        expandIndicator = { Text(text = "...") },
                        collapseIndicator = { Text(text = "...") }
                    )
                ){
                    repeat(5){
                        TagItemPreview()
                    }

                }

            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TagItemPreview(
    modifier: Modifier = Modifier,
    text:String = "Tag"
){
    Column(
        modifier = modifier
            .padding(5.dp)
    ) {
        Box(
            modifier
                .background(color = GrayWhite3, shape = RoundedCornerShape(7.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp)
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(text = "#${text}",
                style = TextStyle(color = Color.Black),
                textAlign = TextAlign.Center,
            )
        }
    }

}