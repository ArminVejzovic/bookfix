package com.example.books.model

data class BookState(
    val isFinished: Boolean = false,
    val isInWishlist: Boolean = false,
    val isReading: Boolean = false
)