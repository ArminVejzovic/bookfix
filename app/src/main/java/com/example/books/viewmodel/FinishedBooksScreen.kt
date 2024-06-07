package com.example.books.viewmodel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.books.ui.component.NasNavigationBar
import com.example.books.utils.BookItem

@Composable
fun FinishedBooksScreen(viewModel: BookViewModel, navController: NavHostController) {
    val books by viewModel.bookListStateFlow.collectAsState()
    val finishedBooks = books
        .filter { viewModel.getBookState(it.bookTitle).value.isFinished }
        .distinctBy { it.bookTitle } // Filtriranje jedinstvenih knjiga po naslovu

    Scaffold(
        bottomBar = { NasNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Finished Books",
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                items(finishedBooks) { book ->
                    BookItem(book = book, context = LocalContext.current) {
                        navController.navigate("detail/${book.bookTitle}")
                    }
                }
            }
        }
    }
}