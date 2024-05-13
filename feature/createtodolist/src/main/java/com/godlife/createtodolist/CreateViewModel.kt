package com.godlife.createtodolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.model.todo.EndTimeData
import com.godlife.model.todo.NotificationTimeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor() :ViewModel(){

    private val _selectedList = MutableStateFlow<List<String>>(emptyList())
    val selectedList: StateFlow<List<String>> = _selectedList

    private val _endTime = MutableStateFlow<EndTimeData?>(null)
    val endTime: StateFlow<EndTimeData?> = _endTime

    private val _notificationTime = MutableStateFlow<NotificationTimeData?>(null)
    val notificationTime: StateFlow<NotificationTimeData?> = _notificationTime




    fun addToSelectedList(todo: String) {
        viewModelScope.launch {
            _selectedList.value += todo
        }
    }

    fun updateEndTime(time: EndTimeData) {
        _endTime.value = time
    }

    fun updateNotificationTime(time: NotificationTimeData) {
        _notificationTime.value = time
    }



}