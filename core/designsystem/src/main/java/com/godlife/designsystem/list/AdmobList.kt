package com.godlife.designsystem.list

import android.content.Context
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.R
import com.godlife.designsystem.component.GodLifeButtonOrange
import com.godlife.designsystem.theme.GrayWhite
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.NativeAd
import com.skydoves.landscapist.rememberDrawablePainter

@Composable
fun AdMobListView(
    modifier: Modifier = Modifier,
    ad: NativeAd,
    view: View,
    context: Context
){
    Column(
        modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ){

        Box(
            modifier = modifier
                .fillMaxWidth()
        ){

            Icon(
                painter = painterResource(id = R.drawable.ad_badge),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier
                    .align(Alignment.TopStart)
            )

        }

        Spacer(modifier = modifier.height(10.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            ad.icon?.drawable?.let {
                Image(
                    painter = rememberDrawablePainter(it),
                    contentDescription = null,
                    modifier = modifier
                        .size(70.dp)
                )
            }

            Spacer(modifier = modifier.width(10.dp))


            ad.headline?.let {
                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = it,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        Spacer(modifier = modifier.height(10.dp))

        ad.body?.let {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(max = 100.dp)
                    .padding(horizontal = 5.dp),
                text = it,
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = modifier.height(10.dp))

        GodLifeButtonOrange(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            onClick = { view.performClick() },
            text = {
                ad.callToAction?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }
        )

    }
}


// TODO: native 광고가 돋보이게 그림자 효과 필요
@Preview
@Composable
fun AdMobListViewPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ){

        Box(
            modifier = modifier
                .fillMaxWidth()
        ){

            Icon(
                painter = painterResource(id = R.drawable.ad_badge),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier
                    .align(Alignment.TopStart)
            )

            Icon(
                painter = painterResource(id = R.drawable.ad_badge),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier
                    .align(Alignment.TopEnd)
            )

        }

        Spacer(modifier = modifier.height(10.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Icon(
                painter = painterResource(id = R.drawable.ad_badge),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier
                    .size(70.dp)
            )

            Spacer(modifier = modifier.width(10.dp))

            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = "광고 제목",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                overflow = TextOverflow.Ellipsis
            )

        }

        Spacer(modifier = modifier.height(10.dp))

        Text(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = 100.dp)
                .padding(horizontal = 5.dp),
            text = "광고 설명",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = modifier.height(10.dp))

        GodLifeButtonOrange(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            onClick = { /*TODO*/ },
            text = {
                Text(
                    text = "설치하기",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        )

    }
}