package com.example.bookfix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfix.model.BookDetail
import com.example.bookfix.network.BookApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val _bookListStateFlow: MutableStateFlow<List<BookDetail>> = MutableStateFlow(emptyList())
    val bookListStateFlow: StateFlow<List<BookDetail>> = _bookListStateFlow
    fun fetchBooks(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieApiService = BookApiService.provideBookApi()
            try {
                val bookList = movieApiService.getBooksList(apiKey)
                val books = bookList
                _bookListStateFlow.value = books
                println("Dohvaćena lista filmova:")
                books.forEach { book ->
                    println("Title: ${book.bookTitle}")
                }
            } catch (e: Exception) {
                println("Greška prilikom dohvatanja lista knjiga: ${e.message}")
            }
        }
    }
}