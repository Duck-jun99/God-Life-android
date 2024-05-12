package com.godlife.createtodolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor() :ViewModel(){

    private val _selectedList = MutableStateFlow<List<String>>(emptyList())
    val selectedList: StateFlow<List<String>> = _selectedList

    fun addToSelectedList(todo: String) {
        viewModelScope.launch {
            _selectedList.value += todo
        }
    }



}