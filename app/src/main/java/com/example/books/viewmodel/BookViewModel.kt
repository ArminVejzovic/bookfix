package com.example.books.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.model.BookDetail
import com.example.books.model.BookState
import com.example.books.network.BookApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val _bookListStateFlow: MutableStateFlow<List<BookDetail>> = MutableStateFlow(emptyList())
    val bookListStateFlow: StateFlow<List<BookDetail>> = _bookListStateFlow

    private val _bookStates = mutableMapOf<String, MutableStateFlow<BookState>>()

    init {
        fetchBooks(BookApiService.API_KEY)
    }

    fun fetchBooks(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieApiService = BookApiService.provideBookApi()
            try {
                val bookList = movieApiService.getBooksList(apiKey)
                _bookListStateFlow.value = bookList
                bookList.forEach { book ->
                    _bookStates[book.bookTitle] = MutableStateFlow(BookState())
                }
            } catch (e: Exception) {
                println("Error fetching books: ${e.message}")
            }
        }
    }

    fun getBookState(bookTitle: String): StateFlow<BookState> {
        return _bookStates[bookTitle] ?: MutableStateFlow(BookState())
    }

    fun markAsFinished(bookTitle: String) {
        _bookStates[bookTitle]?.value = _bookStates[bookTitle]?.value?.copy(isFinished = true) ?: BookState(isFinished = true)
        removeFromWishlist(bookTitle)
        removeFromReading(bookTitle)
    }

    fun addToWishlist(bookTitle: String) {
        _bookStates[bookTitle]?.value = _bookStates[bookTitle]?.value?.copy(isInWishlist = true) ?: BookState(isInWishlist = true)
        removeFromFinished(bookTitle)
        removeFromReading(bookTitle)
    }

    fun markAsReading(bookTitle: String) {
        _bookStates[bookTitle]?.value = _bookStates[bookTitle]?.value?.copy(isReading = true) ?: BookState(isReading = true)
        removeFromWishlist(bookTitle)
        removeFromFinished(bookTitle)
    }

    fun removeFromFinished(bookTitle: String) {
        _bookStates[bookTitle]?.value = _bookStates[bookTitle]?.value?.copy(isFinished = false) ?: BookState()
    }

    fun removeFromWishlist(bookTitle: String) {
        _bookStates[bookTitle]?.value = _bookStates[bookTitle]?.value?.copy(isInWishlist = false) ?: BookState()
    }

    fun removeFromReading(bookTitle: String) {
        _bookStates[bookTitle]?.value = _bookStates[bookTitle]?.value?.copy(isReading = false) ?: BookState()
    }
}