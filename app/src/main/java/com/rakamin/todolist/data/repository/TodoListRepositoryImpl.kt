package com.rakamin.todolist.data.repository

import com.rakamin.todolist.data.datasource.TodoListDao
import com.rakamin.todolist.domain.model.TodoList
import com.rakamin.todolist.domain.repository.TodoListRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoListRepositoryImpl @Inject constructor(
    private val dao: TodoListDao,
): TodoListRepository {
    override suspend fun getList(): List<TodoList> {
        return dao.getAllTodoList()
    }

}
