package com.godlife.community_page.stimulus.recommended_author_post

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
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
import com.godlife.community_page.R
import com.godlife.community_page.stimulus.RecommendUserItem
import com.godlife.community_page.stimulus.StimulusLoadingScreen
import com.godlife.community_page.stimulus.StimulusPostItem
import com.godlife.community_page.stimulus.StimulusPostUiState
import com.godlife.community_page.stimulus.latest_post.LatestStimulusItem

@Composable
fun RecommendedAuthorStimulusPostContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecommendedAuthorStimulusPostViewModel = hiltViewModel()
){

    val item = viewModel.postList.collectAsState().value

    val uiState = viewModel.uiState.collectAsState().value

    when(uiState){
        is StimulusPostUiState.Loading -> {
            StimulusLoadingScreen()
        }
        is StimulusPostUiState.Success -> {
            LazyHorizontalGrid(
                modifier = modifier
                    .fillMaxWidth()
                    .height(500.dp),
                rows = GridCells.Fixed(3)
            ) {

                items(item.size){
                    item[it]?.let { it1 -> LatestStimulusItem(item = it1, navController = navController)  }
                }
            }
        }
        is StimulusPostUiState.Error -> {

        }
    }

}
