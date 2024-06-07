package com.example.books.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.books.dao.FinishedBookDao
import com.example.books.model.FinishedBook

@Database(entities = [FinishedBook::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun finishedBookDao(): FinishedBookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}