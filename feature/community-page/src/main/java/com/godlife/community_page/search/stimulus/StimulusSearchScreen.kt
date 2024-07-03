package com.godlife.community_page.search.stimulus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.component.GodLifeSearchBar

@Composable
fun StimulusSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: StimulusSearchViewModel = hiltViewModel()
){
    Column(
        modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Checkbox(checked = true, onCheckedChange = {})
            Text(text = "게시물")

            Checkbox(checked = false, onCheckedChange = {})
            Text(text = "작가")

        }


        GodLifeSearchBar(
            hint = "검색어를 2자 이상 입력해주세요.",
            onTextChanged = {  }
        )

        Spacer(modifier.height(16.dp))

        LazyColumn {

        }

    }
}

@Preview
@Composable
fun StimulusSearchScreenPreview(
    modifier: Modifier = Modifier
){
    Column(
        modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            Checkbox(checked = true, onCheckedChange = {})
            Text(text = "게시물")

            Checkbox(checked = false, onCheckedChange = {})
            Text(text = "작가")

        }


        GodLifeSearchBar(
            hint = "검색어를 2자 이상 입력해주세요.",
            onTextChanged = {  }
        )

        Spacer(modifier.height(16.dp))

        LazyColumn {

        }

    }
}