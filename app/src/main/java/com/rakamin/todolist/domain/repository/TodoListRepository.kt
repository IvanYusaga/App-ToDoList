package com.rakamin.todolist.domain.repository

import com.rakamin.todolist.domain.model.TodoList

interface TodoListRepository {
    suspend fun getList(): List<TodoList>
}
