package com.godlife.data.repository

import com.godlife.database.model.TodoEntity

interface LocalDatabaseRepository {
    suspend fun insertTodo(todo: TodoEntity)

    suspend fun getAllTodoList() : List<TodoEntity>
}