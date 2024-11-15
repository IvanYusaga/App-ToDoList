package com.rakamin.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rakamin.todolist.data.datasource.TodoListDao
import com.rakamin.todolist.domain.model.TodoList
import java.util.concurrent.Executors

@Database(
    entities = [
        TodoList::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters
abstract class AppDatabase : RoomDatabase() {
    // --------------------------------- Provide DAO ----------------------
    abstract fun todoListDao(): TodoListDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        /*private val  MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movie ADD COLUMN status_film VARCHAR(50)")
            }
        }*/

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Update this when do migration
        // ref : https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        // https://infinum.com/handbook/books/android/common-android/room-migrations
        // https://code.luasoftware.com/tutorials/android/android-room-upgrade-database-new-table/
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "TodoList")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                //.fallbackToDestructiveMigration()
                //.addMigrations(MIGRATION_1_2)
                .setQueryCallback({ sqlQuery, bindArgs ->
                    if (sqlQuery.isNotEmpty()) {
                        println("### > SQL Query: $sqlQuery SQL Params: $bindArgs")
                    }
                }, Executors.newSingleThreadExecutor())
                .build()
        }
    }
}
