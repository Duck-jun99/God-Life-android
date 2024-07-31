package com.godlife.data.repository

import com.godlife.database.model.TodoEntity
import com.godlife.model.todo.TodoList
import com.godlife.model.todo.TodoTimeData

interface LocalDatabaseRepository {
    suspend fun insertTodo(todo: TodoEntity)

    suspend fun getAllTodoList() : List<TodoEntity>

    suspend fun deleteTodoList(id : Int)

    suspend fun updateTodoList(todo: TodoEntity)

    suspend fun getTodoList(id: Int) : TodoEntity

    suspend fun getTodayTodoList() : TodoEntity
}