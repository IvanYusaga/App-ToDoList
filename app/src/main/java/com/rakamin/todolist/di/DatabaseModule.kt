package com.rakamin.todolist.di

import android.content.Context
import com.rakamin.todolist.data.AppDatabase
import com.rakamin.todolist.data.datasource.TodoListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideTodoListDao(database: AppDatabase): TodoListDao {
        return database.todoListDao()
    }
}
