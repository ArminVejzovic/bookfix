package com.example.books.repository

import android.content.Context
import com.example.books.database.BookDatabase
import com.example.books.model.FinishedBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(context: Context) {
    private val finishedBookDao = BookDatabase.getDatabase(context).finishedBookDao()

    suspend fun insertBook(finishedBook: FinishedBook) {
        withContext(Dispatchers.IO) {
            finishedBookDao.insertBook(finishedBook)
        }
    }

    suspend fun getAllFinishedBooks(): List<FinishedBook> {
        return withContext(Dispatchers.IO) {
            finishedBookDao.getAllFinishedBooks()
        }
    }
}