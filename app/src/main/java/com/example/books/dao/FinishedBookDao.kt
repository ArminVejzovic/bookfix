package com.example.books.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.books.model.FinishedBook

@Dao
interface FinishedBookDao {
    @Insert
    suspend fun insertBook(finishedBook: FinishedBook)

    @Query("SELECT * FROM finished_books")
    suspend fun getAllFinishedBooks(): List<FinishedBook>
}