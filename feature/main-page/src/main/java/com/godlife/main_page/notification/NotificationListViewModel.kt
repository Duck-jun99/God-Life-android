package com.godlife.main_page.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetNotificationListUseCase
import com.godlife.domain.PatchNotificationReadUseCase
import com.godlife.main_page.ErrorType
import com.godlife.main_page.MainPageUiState
import com.godlife.network.model.NotificationListBody
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    private val getNotificationListUseCase: GetNotificationListUseCase,
    private val patchNotificationReadUseCase: PatchNotificationReadUseCase
): ViewModel() {

    // 전체 UI 상태
    private val _uiState = MutableStateFlow<MainPageUiState>(MainPageUiState.Loading)
    val uiState: StateFlow<MainPageUiState> = _uiState

    // 알림 리스트 불러온 상태
    val notificationListLoaded = MutableStateFlow<Boolean>(false)

    // 읽음 처리 상태
    val notificationRead = MutableStateFlow<Boolean>(false)

    //알림 리스트
    private val _notificationList = MutableStateFlow<List<NotificationListBody>>(emptyList())
    val notificationList: StateFlow<List<NotificationListBody>> = _notificationList


    init{
        getNotificationList()
    }

    // 알림 리스트 가져오기
    private fun getNotificationList(){
        if(!notificationListLoaded.value){
            notificationListLoaded.value = true

            viewModelScope.launch {
                getNotificationListUseCase.executeGetNotificationList()

                    .onSuccess {
                        _notificationList.value = data.body.reversed()

                        _uiState.value = MainPageUiState.Success("Success")
                    }
                    .onError {

                        // 토큰 만료시
                        if(this.response.code() == 401){
                            _uiState.value = MainPageUiState.Error(ErrorType.REFRESH_TOKEN_EXPIRED)
                        }
                        else{
                            // UI State Error로 변경
                            _uiState.value = MainPageUiState.Error(ErrorType.SERVER_ERROR)
                        }

                    }
                    .onException {

                        // UI State Error로 변경
                        _uiState.value = MainPageUiState.Error(ErrorType.UNKNOWN_ERROR)
                    }
            }

        }

    }

    fun patchNotificationRead(notificationId: Int){
        notificationRead.value = false
        if(!notificationRead.value){
            notificationRead.value = true

            viewModelScope.launch {
                patchNotificationReadUseCase.executePatchNotificationRead(notificationId)
                    .onSuccess {
                        notificationListLoaded.value = false
                        getNotificationList()
                    }
                    .onError {

                    }
                    .onException {

                    }
            }

        }
    }
}