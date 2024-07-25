package com.godlife.main_page.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.database.model.TodoEntity
import com.godlife.domain.DeleteNotificationTimeUseCase
import com.godlife.domain.LocalDatabaseUseCase
import com.godlife.domain.PatchNotificationTimeUseCase
import com.godlife.domain.PostNotificationTimeUseCase
import com.godlife.main_page.MainPageViewModel
import com.godlife.model.todo.NotificationTimeData
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditAlarmViewModel @Inject constructor(
    private val patchNotificationTimeUseCase: PatchNotificationTimeUseCase,
    private val deleteNotificationTimeUseCase: DeleteNotificationTimeUseCase,
    private val postNotificationTimeUseCase: PostNotificationTimeUseCase,
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel() {

    // 오늘 투두리스트 불러온 플래그
    val getTodayTodoListFlag = mutableStateOf<Boolean>(false)

    // 작업 완료 플래그
    val completeFlag = mutableStateOf<Boolean>(false)


    //오늘 투두 정보
    private val _todayTodoInfo = MutableStateFlow<TodoEntity?>(null)
    val todayTodoInfo: StateFlow<TodoEntity?> = _todayTodoInfo

    //알람 설정 유무
    private val _notificationFlag = MutableStateFlow<Boolean>(false)
    val notificationFlag: StateFlow<Boolean> = _notificationFlag

    private val _notificationTime = MutableStateFlow<NotificationTimeData>(NotificationTimeData(0,0,0,0,0))
    val notificationTime: StateFlow<NotificationTimeData> = _notificationTime

    init{
        viewModelScope.launch(Dispatchers.IO) {

            getTodayTodoList()

            getNotificationTime()
        }
    }

    //오늘 설정한 투두 리스트를 로컬 데이터베이스에서 가져오기
    private suspend fun getTodayTodoList(){

        if(!getTodayTodoListFlag.value){
            getTodayTodoListFlag.value = true

            _todayTodoInfo.value = localDatabaseUseCase.getTodayTodoList()

        }
    }

    //오늘 알람 시간을 설정했다면 가져오기
    private suspend fun getNotificationTime(){

        if(todayTodoInfo.value!!.notificationBoolean){
            _notificationFlag.value = true

            _notificationTime.value = todayTodoInfo.value!!.notificationTime
        }
    }

    fun updateNotificationTime(time: NotificationTimeData) {

        _notificationTime.value = time

    }

    fun completeNotification(
        mainPageViewModel: MainPageViewModel,
        onUpdateComplete: () -> Unit
    ) {

        if(!completeFlag.value){
            completeFlag.value = true

            //알람 설정한 경우
            if(notificationFlag.value){

                //내부 DB 업데이트
                //localDatabaseUseCase.

                //알람 설정이 최초가 아닌경우 업데이트 하기
                if(todayTodoInfo.value!!.notificationBoolean){

                    viewModelScope.launch(Dispatchers.IO) {
                        patchNotificationTimeUseCase.executePatchNotificationTime(notificationTime.value)
                            .onSuccess {
                                mainPageViewModel.setUpdateAlertDialogFlag()
                                onUpdateComplete()
                                onCleared()
                            }
                            .onError {

                            }
                            .onException {

                            }
                    }
                }

                //알람 설정이 최초인 경우 알람 생성 하기
                else{

                    viewModelScope.launch(Dispatchers.IO) {
                        postNotificationTimeUseCase.executePostNotificationTime(notificationTime.value)
                            .onSuccess {
                                mainPageViewModel.setUpdateAlertDialogFlag()
                                onUpdateComplete()
                                onCleared()
                            }
                            .onError {

                            }
                            .onException {

                            }
                    }

                }

            }

            //알람 설정X 또는 알람 삭제
            else{

                viewModelScope.launch(Dispatchers.IO) {

                    deleteNotificationTimeUseCase.executeDeleteNotificationTime()
                        .onSuccess {
                            mainPageViewModel.setUpdateAlertDialogFlag()
                            onUpdateComplete()
                            onCleared()
                        }
                        .onError {

                        }
                        .onException {

                        }


                }


            }


        }

    }

    //설정된 시간값이 현재 시간보다 이후인지 확인
    fun checkFunc(): Boolean{

        if(notificationTime.value.hour < LocalDateTime.now().hour){
            return false
        }

        else if(notificationTime.value.hour == LocalDateTime.now().hour){

            if(notificationTime.value.minute < LocalDateTime.now().minute){
                return false
            }

        }
        return true

    }

    fun setnotificationFlag() {
        _notificationFlag.value = !_notificationFlag.value
    }

    fun setCleared(){
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
    }


}