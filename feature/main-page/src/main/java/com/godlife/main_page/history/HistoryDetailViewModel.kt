package com.godlife.main_page.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.database.model.TodoEntity
import com.godlife.domain.LocalDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val localDatabaseUseCase: LocalDatabaseUseCase
): ViewModel(){

    //투두 리스트 불러왔는지 플래그
    private val _isTodoListLoaded = MutableStateFlow<Boolean>(false)

    //평가 플래그
    private val _isEvaluated = MutableStateFlow<Boolean>(false)

    //삭제 플래그
    private val _isDeleted = MutableStateFlow<Boolean>(false)

    //선택된 투두 리스트
    private val _selectedTodoList = MutableStateFlow<TodoEntity?>(null)
    val selectedTodoList: StateFlow<TodoEntity?> = _selectedTodoList

    //완료한 투두리스트 사이즈
    private val _completedTodoListSize = MutableStateFlow<Int>(0)
    val completedTodoListSize: StateFlow<Int> = _completedTodoListSize

    //완료한 투두리스트
    private val _completedTodoList = MutableStateFlow<List<String>>(emptyList())
    val completedTodoList: StateFlow<List<String>> = _completedTodoList

    //미완료 투두리스트
    private val _uncompletedTodoList = MutableStateFlow<List<String>>(emptyList())
    val uncompletedTodoList: StateFlow<List<String>> = _uncompletedTodoList

    //평가된 글
    private val _evaluation = MutableStateFlow<String>("")
    val evaluation: StateFlow<String> = _evaluation


    fun getTodoList(id:Int){

        if(!_isTodoListLoaded.value){
            _isTodoListLoaded.value = true

            _completedTodoListSize.value = 0

            viewModelScope.launch(Dispatchers.IO) {
                _selectedTodoList.value = localDatabaseUseCase.getTodoList(id)

                selectedTodoList.value!!.todoList.forEach {
                    if(it.iscompleted){
                        _completedTodoListSize.value += 1
                        _completedTodoList.value += it.name
                    }
                    else{
                        _uncompletedTodoList.value += it.name
                    }

                }

                evaluation()
            }

        }

    }

    private suspend fun evaluation(){
        if(!_isEvaluated.value){
            _isEvaluated.value = true

            when(selectedTodoList.value?.isCompleted){

                true -> {
                    _evaluation.value = "모든 투두를 달성하셨군요. 대단해요!\n" +
                            "열심히 하고 계시는 모습은 저희 굿생팀도 배워야 할 자세라고 생각해요.\n" +
                            "사용자님의 Good Life를 항상 응원하겠습니다. 화이팅!"
                }
                false ->{

                    if(selectedTodoList.value?.todoList?.isNotEmpty() == true){

                        val totalSize = selectedTodoList.value!!.todoList.size

                        if(completedTodoList.value.size < (totalSize/2) ){

                            _evaluation.value = "${totalSize}개의 투두를 설정하셨지만 목표에 비해 달성하신 비율이 적은 것 같아요.\n" +
                                    "완료하지 못한 ${uncompletedTodoList.value.joinToString(", ")} 도 달성할 수 있도록 노력해보세요.\n" +
                                    "저희 굿생팀은 사용자님께서 더욱 노력하실거라 확신해요.\n" +
                                    "Good Life를 위해 화이팅!"
                        }

                        else{
                            _evaluation.value = "아쉽게도 모든 투두를 완료하지 못하셨네요.\n" +
                                    "그래도 많은 투두를 완료하셨으니 앞으로는 모든 투두를 완료하실 수 있을거에요!\n" +
                                    "다음번엔 완료하지 못한 ${uncompletedTodoList.value.joinToString(", ")} 도 달성할 수 있도록 노력해보세요.\n" +
                                    "저희 굿생팀은 사용자님께서 더욱 노력하실거라 확신해요.\n" +
                                    "Good Life를 위해 화이팅!"
                        }

                    }


                }

                null -> {

                }

            }
        }
    }

    fun deleteTodoList(){
        if(!_isDeleted.value){
            _isDeleted.value = true

            viewModelScope.launch(Dispatchers.IO) {
                localDatabaseUseCase.deleteTodoList(selectedTodoList.value!!.id)
            }
        }
    }
}