package com.godlife.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.godlife.database.model.TodoEntity
import com.godlife.model.todo.NotificationTimeData
import com.godlife.model.todo.TodoList
import com.godlife.model.todo.TodoTimeData

@Dao
interface TodoDao {
    @Insert
    fun insertTodo(todo: TodoEntity)

    //모든 데이터베이스 가져오기
    @Query("SELECT * FROM todos")
    fun getAllTodoList() : List<TodoEntity>

    //특정 데이터베이스 삭제하기
    @Query("DELETE FROM todos WHERE id = :id")
    fun deleteTodoList(id : Int)

    //특정 데이터베이스 수정하기
    @Query("UPDATE todos SET todoList = :todoList, notificationBoolean = :notificationBoolean, notificationTime = :notificationTime ,isCompleted = :isCompleted WHERE id = :id")
    fun updateTodoList(id : Int, todoList : List<TodoList>, notificationBoolean : Boolean, notificationTime : NotificationTimeData, isCompleted : Boolean)


    //특정 데이터베이스 가져오기
    @Query("SELECT * FROM todos WHERE id = :id")
    fun getTodoList(id : Int) : TodoEntity

    //오늘 날짜 데이터 가져오기
    @Query("SELECT * FROM todos WHERE date = :date")
    fun getTodayTodoList(date : TodoTimeData) : TodoEntity

}