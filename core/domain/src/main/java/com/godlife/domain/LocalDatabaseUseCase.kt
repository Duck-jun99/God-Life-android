package com.godlife.domain

import androidx.room.Insert
import androidx.room.Query
import com.godlife.data.repository.LocalDatabaseRepository
import com.godlife.database.model.TodoEntity
import javax.inject.Inject

class LocalDatabaseUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {

    suspend fun insertTodo(todo: TodoEntity) = localDatabaseRepository.insertTodo(todo = todo)


    suspend fun getAllTodoList() : List<TodoEntity> = localDatabaseRepository.getAllTodoList()
}