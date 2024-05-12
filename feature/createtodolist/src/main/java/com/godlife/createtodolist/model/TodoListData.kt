package com.godlife.createtodolist.model

import com.godlife.createtodolist.R

data class TodoListForm(
    val name: String,
    val imgId: Int,
    val description: String = ""
)

class TodoList{
    private val todoList:List<TodoListForm> = listOf(
        TodoListForm(name = "아침 식사", imgId = R.drawable.mdi_rice),
        TodoListForm(name = "점심 식사", imgId = R.drawable.mdi_rice),
        TodoListForm(name = "저녁 식사", imgId = R.drawable.mdi_rice),

        TodoListForm(name = "공부", imgId = R.drawable.solar_pen_bold),
        TodoListForm(name = "운동", imgId = R.drawable.material_symbols_sports_gymnastics),
        TodoListForm(name = "명상", imgId = R.drawable.mdi_meditation),

        TodoListForm(name = "일기 작성", imgId = R.drawable.mingcute_diary_line),
        TodoListForm(name = "독서", imgId = R.drawable.tabler_book),
        TodoListForm(name = "뷰티", imgId = R.drawable.material_symbols_self_care),

        TodoListForm(name = "문화 생활", imgId = R.drawable.mdi_art),
        TodoListForm(name = "동아리/단체", imgId = R.drawable.pepicons_pencil_people),
        TodoListForm(name = "경제 활동", imgId = R.drawable.mingcute_pig_money_fill),

        TodoListForm(name = "청소", imgId = R.drawable.carbon_clean),
        TodoListForm(name = "건강", imgId = R.drawable.solar_health_bold),
        TodoListForm(name = "여행", imgId = R.drawable.material_symbols_light_trip),
    )

    fun getTodoList() :List<TodoListForm>{
        return todoList
    }
}
