package hoods.com.todoapp

import android.content.Context
import hoods.com.todoapp.data.TodoDataSource
import hoods.com.todoapp.data.room.TodoDatabase

object Graph {
    lateinit var database: TodoDatabase
        private set
    val todoRepo by lazy {
        TodoDataSource(database.todoDao())
    }

    fun provide(context: Context) {
        database = TodoDatabase.getDatabase(context)
    }
}