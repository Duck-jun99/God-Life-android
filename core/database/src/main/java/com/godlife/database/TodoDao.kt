package com.godlife.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.godlife.database.model.TodoEntity

@Dao
interface TodoDao {
    @Insert
    fun insertTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos")
    fun getAllTodoList() : List<TodoEntity>
}