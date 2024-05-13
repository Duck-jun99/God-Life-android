package com.godlife.data

import com.godlife.data.repository.LocalDatabaseRepository
import com.godlife.database.TodoDao
import com.godlife.database.model.TodoEntity
import javax.inject.Inject

class LocalDatabaseRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
): LocalDatabaseRepository {
    override suspend fun insertTodo(todo: TodoEntity) {
        return todoDao.insertTodo(todo)
    }

    override suspend fun getAllTodoList(): List<TodoEntity> {
        return todoDao.getAllTodoList()
    }

}