package com.example.bookfix.model

data class BookDetail (
    var bookTitle       : String,
    var bookImage       : String,
    var bookDescription : String,
    var bookAuthor      : String,
    var bookPublisher   : String,
    var amazonBookUrl   : String,
    var bookIsbn        : String,
    var bookRank        : Int
)
