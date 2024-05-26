package com.godlife.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.community.FamousPostItem
import com.godlife.model.community.TagItem

@Composable
fun CommunityFamousPostList(modifier: Modifier = Modifier,
                            famousPostItem: FamousPostItem
){
    Box(modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)){
        Column(
            modifier
                .width(320.dp)
                .height(520.dp)
                .shadow(7.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
        ){
            Box(
                modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
            ){
                Text(text = "IMAGE", modifier.align(Alignment.Center))
            }
            Column(
                modifier
                    .weight(0.6f)
                    .fillMaxWidth()
                    .padding(20.dp)) {

                Row(modifier.fillMaxWidth()){
                    Text(text = famousPostItem.name, style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold))

                    Spacer(modifier.size(10.dp))

                    //티어 보여줄 부분
                    Text(text = famousPostItem.rank, style = TextStyle(color = Color.Magenta, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                }

                Spacer(modifier.size(15.dp))

                Text(text = famousPostItem.title, style = GodLifeTypography.titleMedium)

                Spacer(modifier.size(20.dp))

                Text(text = famousPostItem.text, style = GodLifeTypography.bodyMedium)

                Spacer(modifier.size(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    itemsIndexed(famousPostItem.tagItem) { index, item ->
                        TagItem(item)
                    }
                }


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


@Preview
@Composable
fun CommunityFamousPostListPreview(modifier: Modifier = Modifier){
    Box(modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)){
        Column(
            modifier
                .width(320.dp)
                .height(520.dp)
                .shadow(7.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
        ){
            Box(
                modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
            ){
                Text(text = "IMAGE", modifier.align(Alignment.Center))
            }
            Column(
                modifier
                    .weight(0.6f)
                    .fillMaxWidth()
                    .padding(20.dp)) {

                Row(modifier.fillMaxWidth()){
                    Text(text = "NAME", style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold))

                    Spacer(modifier.size(10.dp))

                    //티어 보여줄 부분
                    Text(text = "마스터", style = TextStyle(color = Color.Magenta, fontWeight = FontWeight.Bold, fontSize = 15.sp))

                }


                Spacer(modifier.size(15.dp))

                Text(text = "Title", style = GodLifeTypography.titleMedium)

                Spacer(modifier.size(20.dp))

                Text(text = "Text", style = GodLifeTypography.bodyMedium)

                Spacer(modifier.size(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(5) { item ->
                        TagItemPreview()
                    }
                }


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