package com.example.books.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finished_books")
data class FinishedBook(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bookTitle: String,
    val bookImage: String,
    val bookDescription: String,
    val bookAuthor: String,
    val bookPublisher: String,
    val amazonBookUrl: String,
    val bookIsbn: String,
    val bookRank: Int
)