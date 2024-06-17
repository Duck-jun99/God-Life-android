package com.godlife.data

import com.godlife.data.repository.LocalDatabaseRepository
import com.godlife.database.TodoDao
import com.godlife.database.model.TodoEntity
import com.godlife.model.todo.TodoList
import com.godlife.model.todo.TodoTimeData
import java.time.LocalDateTime
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

    override suspend fun deleteTodoList(id: Int) {
        return todoDao.deleteTodoList(id)
    }

    override suspend fun updateTodoList(todo: TodoEntity) {
        return todoDao.updateTodoList(todo.id, todo.todoList, todo.notificationBoolean, todo.notificationTime, todo.isCompleted)
    }

    override suspend fun getTodayTodoList(): TodoEntity {

        val date = TodoTimeData(
            y = LocalDateTime.now().year,
            m = LocalDateTime.now().monthValue,
            d = LocalDateTime.now().dayOfMonth
        )

        return todoDao.getTodayTodoList(date)
    }

}