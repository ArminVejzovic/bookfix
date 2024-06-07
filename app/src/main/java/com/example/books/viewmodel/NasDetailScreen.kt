package com.example.books.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.books.model.BookDetail
import com.example.books.viewmodel.BookViewModel
import com.example.books.ui.component.NasNavigationBar

@Composable
fun NasDetailScreen(
    book: BookDetail,
    navController: NavHostController,
    context: Context,
    viewModel: BookViewModel
) {
    val bookState by viewModel.getBookState(book.bookTitle).collectAsState()

    Scaffold(
        bottomBar = { NasNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = rememberImagePainter(data = book.bookImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = book.bookTitle, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                Text(text = "Author: ${book.bookAuthor}", style = TextStyle(fontSize = 20.sp))
                Text(text = "Publisher: ${book.bookPublisher}", style = TextStyle(fontSize = 20.sp))
                Text(text = "ISBN: ${book.bookIsbn}", style = TextStyle(fontSize = 20.sp))
                Text(text = "Rank: ${book.bookRank}", style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = book.bookDescription, style = TextStyle(fontSize = 16.sp))
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazonBookUrl))
                    context.startActivity(intent)
                }) {
                    Text(text = "Buy on Amazon")
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (bookState.isFinished) {
                    Button(onClick = {
                        viewModel.removeFromFinished(book.bookTitle)
                    }) {
                        Text(text = "Unfinish Book")
                    }
                } else {
                    Button(onClick = {
                        viewModel.markAsFinished(book.bookTitle)
                    }) {
                        Text(text = "Mark as Finished")
                    }
                }

                if (bookState.isInWishlist) {
                    Button(onClick = {
                        viewModel.removeFromWishlist(book.bookTitle)
                    }) {
                        Text(text = "Remove from Wishlist")
                    }
                } else {
                    Button(onClick = {
                        viewModel.addToWishlist(book.bookTitle)
                    }) {
                        Text(text = "Add to Wishlist")
                    }
                }

                if (bookState.isReading) {
                    Button(onClick = {
                        viewModel.removeFromReading(book.bookTitle)
                    }) {
                        Text(text = "Mark as Not Reading")
                    }
                } else {
                    Button(onClick = {
                        viewModel.markAsReading(book.bookTitle)
                    }) {
                        Text(text = "Mark as Reading")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    shareBookDetails(context, book)
                }) {
                    Text(text = "Share Book")
                }
            }
        }
    }
}

fun shareBookDetails(context: Context, book: BookDetail) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this book:\n\nTitle: ${book.bookTitle}\nAuthor: ${book.bookAuthor}\nPublisher: ${book.bookPublisher}\nISBN: ${book.bookIsbn}\nRank: ${book.bookRank}\n\n${book.bookDescription}\n\nFind it on Amazon: ${book.amazonBookUrl}")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share book via"))
}
