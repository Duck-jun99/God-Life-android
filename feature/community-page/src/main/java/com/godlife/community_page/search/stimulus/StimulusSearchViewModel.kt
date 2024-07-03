package com.godlife.community_page.search.stimulus

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StimulusSearchViewModel @Inject constructor(

): ViewModel() {

    //검색어
    private val _searchText = MutableStateFlow<String>("")
    val searchText: StateFlow<String> = _searchText

    //검색 카테고리
    private val _searchCategory = MutableStateFlow<String>("post")
    val searchCategory: StateFlow<String> = _searchCategory
}