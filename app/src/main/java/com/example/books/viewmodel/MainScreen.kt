package com.example.books.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.books.viewmodel.BookViewModel
import com.google.android.mediahome.books.BookItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.books.ui.component.NasNavigationBar
import com.example.books.utils.BookItem

@ExperimentalCoroutinesApi
@Composable
fun MainScreen(viewModel: BookViewModel, navController: NavHostController) {
    Scaffold(
        bottomBar = { NasNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var searchText by remember { mutableStateOf(TextFieldValue("")) }
            SearchBar(searchText) { newText ->
                searchText = newText
            }
            BookList(viewModel, searchText.text, navController)
        }
    }
}

@Composable
fun SearchBar(searchText: TextFieldValue, onSearchTextChange: (TextFieldValue) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (searchText.text.isEmpty()) {
                    Text(
                        text = "Search",
                        style = TextStyle(color = Color.Gray, fontSize = 18.sp)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun BookList(viewModel: BookViewModel, filterText: String, navController: NavHostController) {
    val books by viewModel.bookListStateFlow.collectAsState()
    val uniqueBooks = books.distinctBy { it.bookTitle }
    val filteredBooks = uniqueBooks.filter {
        it.bookTitle.contains(filterText, ignoreCase = true)
    }
    val context = LocalContext.current

    LazyColumn {
        items(filteredBooks) { book ->
            BookItem(book = book, context = context) {
                navController.navigate("detail/${book.bookTitle}")
            }
        }
    }
}