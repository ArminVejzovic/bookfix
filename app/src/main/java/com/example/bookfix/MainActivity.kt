package com.example.bookfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.bookfix.model.BookDetail
import com.example.bookfix.network.BookApiService
import com.example.bookfix.ui.theme.BookfixTheme
import com.example.bookfix.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[BookViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookfixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookListScreen(viewModel)
                }
            }
        }
        viewModel.fetchBooks(BookApiService.API_KEY)
    }
}

@Composable
fun BookListScreen(viewModel: BookViewModel) {
    val books by viewModel.bookListStateFlow.collectAsState()

    LazyColumn {
        items(books) { book ->
            BookItem(book)
        }
    }
}

@Composable
fun BookItem(book: BookDetail) {
    Column {
        Text(text = "Title: ${book.bookTitle}")
        Text(text = "Author: ${book.bookAuthor}")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val viewModel = BookViewModel()
    BookfixTheme {
        BookListScreen(viewModel)
    }
}
